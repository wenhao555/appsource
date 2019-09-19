package com.chd.gateway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    public static int DEV_NUM = 5;
    private ArrayList<EndDeviceBean> mSearchList = new ArrayList<>();
    private EndAdapter endAdapter;
    private ListView listView;
    private TextView tv_title;
    private TextView tv_ctrl;
    private TextView tv_ctrl_back;
    private TextView setUser;
    private Context mContext;
    private HttpUtil httpUtil;
    private String httpcmd = "";
    private boolean lightChangeSuccess = true;

    public void setDeviceData( int position, String httpResult ) {
        int start = position * 8;
        if ( httpResult.length() < start + 8  ){
            return;
        }
        String dt = httpResult.substring( start, start + 8 );
        Log.i( "ycc", "ycc:setDeviceData==" + position + "==" + dt  );
        //1366910113769010138691001396910003167101
        mSearchList.get( position ).setStatus( dt.substring( 0, 1 )  );
        mSearchList.get( position ).setWdval( dt.substring( 1, 3 )  );
        mSearchList.get( position ).setSdval( dt.substring( 3, 5 )  );
        mSearchList.get( position ).setMq2val( dt.substring( 5, 6 )  );
        mSearchList.get( position ).setHumval( dt.substring( 6, 7 )  );
        mSearchList.get( position ).setLightval( dt.substring( 7, 8 )  );

        String temp = dt.substring( 7, 8 ).equals("1") ? "ON" : "OFF";
        int devNum = position + 1;
        String lightStatus = getCtrlCmd( devNum, temp );
        Log.i( "ycc", "ycc:setDeviceData==httpcmd==aaa=" + httpcmd );
        Log.i( "ycc", "ycc:setDeviceData==httpcmd==bbb=" + lightStatus );
        //zigbee终端灯已响应
        if ( lightStatus.equals( httpcmd ) ){
            setCtrlBack( true );
            lightChangeSuccess = true;
        }
    }

    private void setCtrlTips(int position, String status){
        String s = getCtrlText( position, status );
        tv_ctrl.setText( s );
        setCtrlBack( false );
    }

    private void setCtrlBack( boolean hadChange ){
        if ( hadChange ){
            tv_ctrl_back.setText("已响应");
            tv_ctrl_back.setTextColor( mContext.getResources().getColor( R.color.color_66 ) );
            return;
        }
        tv_ctrl_back.setText("未响应");
        tv_ctrl_back.setTextColor( mContext.getResources().getColor( R.color.color_red ) );
    }

    public String getCtrlText(int devNum, String status){
        String s = status.equals("OFF") ? "关灯" : "开灯";
        s = s + devNum;
        return s;
    }

    private String getCtrlCmd(int devNum, String status){
        String ctrl = "L" + devNum + status;
        return ctrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initView();
        addEndItem( DEV_NUM );
        initHttpUtil();
        String uid = SharedPreferencesUtil.getUserUid();
        String key = SharedPreferencesUtil.getUserKey();
        if ( !uid.equals("") && !key.equals("") ){
            httpUtil.getHttpData( Common.HTTP_GET_ENDDEVICE_DATA, "" );
        }
    }

    private void initHttpUtil(){
        httpUtil = new HttpUtil( );
        httpUtil.setHttpUtilInterface(new HttpUtil.IHttpUtil() {
            @Override
            public void onSuccess(int type, String httpResult) {
                dealHttpResult( type, httpResult );
            }
        });
    }

    private void dealHttpResult( int type, String httpResult ){

        int gatewayStatus = Common.GWSTATUS_ONLINE;
        if ( httpResult.indexOf( "account_error" ) >= 0  ){
            gatewayStatus = Common.GWSTATUS_LINK_ERROR;
        }else if ( httpResult.indexOf( "gateway_offline" ) >= 0  ){
            gatewayStatus = Common.GWSTATUS_OFFLINE;
        }
        Log.i( "ycc", "httpResultbbbatus=" + httpResult );
        if(type == Common.HTTP_GET_ENDDEVICE_DATA){
            int position = 0;
            for ( ; position < DEV_NUM; position++ ){
                setDeviceData( position, httpResult );
            }
            if ( gatewayStatus == Common.GWSTATUS_ONLINE ){
                tv_title.setText( Html.fromHtml( "状态：(网关在线)" ) );
            }else if ( gatewayStatus == Common.GWSTATUS_OFFLINE ){
                tv_title.setText( Html.fromHtml( "状态：(网关离线)" ) );
            }else if ( gatewayStatus == Common.GWSTATUS_LINK_ERROR ){
                tv_title.setText( Html.fromHtml( "状态：(未连接, 账号或密码错误)" ) );
            }
            endAdapter.notifyDataSetChanged();
        }else if(type == Common.HTTP_CHANGE_LIGHT_STATUS){
            Log.i( "ycc", "httpResult_change_light_status=" + httpResult );
        }
    }

    private void initView(){
        tv_title = ( TextView ) findViewById( R.id.tv_title );
        tv_ctrl = ( TextView ) findViewById( R.id.tv_ctrl );
        tv_ctrl_back = (TextView) findViewById( R.id.tv_ctrl_back );
        setUser = (TextView) findViewById( R.id.setUser );
        setUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SetActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        listView = (ListView) findViewById(R.id.lv_content);
        tv_title.setText( Html.fromHtml( "状态：(手机未连接服务器)" ) );
    }

    private void addEndItem( int num ){
        for (int i = 0; i < num; i++){
            EndDeviceBean endDeviceBean = new EndDeviceBean();
            mSearchList.add(endDeviceBean);
        }
        endAdapter = new EndAdapter( mContext );
        endAdapter.setmSearchList( mSearchList );
        endAdapter.setEndAdapterInterface(new EndAdapter.EndAdapterInterface() {
            @Override
            public void onItemClick( int position, String tx ) {
                if ( !lightChangeSuccess ){
                    Toast.makeText(mContext, "请等待当前操作响应", Toast.LENGTH_LONG).show();
                    return;
                }
                lightChangeSuccess = false;
                int devNum = position + 1;
                String cmd = TextToON_OFF( tx );
                setCtrlTips( devNum, cmd );
                httpcmd = getCtrlCmd( devNum, cmd );
                httpUtil.getHttpData( Common.HTTP_CHANGE_LIGHT_STATUS, httpcmd );
                Toast.makeText(mContext, "已发送指令", Toast.LENGTH_LONG).show();

            }
        });
        listView.setAdapter(endAdapter);
        endAdapter.notifyDataSetChanged();
    }

    private String TextToON_OFF( String tx ){
        String status = tx.equals( "开灯" ) ? "OFF" : "ON";
        return status;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data.getStringExtra("type").equals("setOk")){
            String uid = data.getStringExtra("uid");
            String key = data.getStringExtra("key");
            Log.i("ycc", "ycc:onActivityResult==uid_" + uid + "##key_" + key);
            if ( !uid.equals("") && !key.equals("") ){
                SharedPreferencesUtil.setUserUid( uid );
                SharedPreferencesUtil.setUserKey( key );
                httpUtil.getHttpData( Common.HTTP_GET_ENDDEVICE_DATA, "" );
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
