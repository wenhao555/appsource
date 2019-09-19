package com.chd.gateway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//设置页面
public class SetActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private ImageView mBackIv;
    private TextView tv_confirm;
    private EditText et_uid;
    private EditText et_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        mContext = this;
        initView();
    }

    /**
     * 初始化界面控件和布局
     */
    private void initView() {
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mBackIv.setOnClickListener(this);

        et_uid = (EditText) findViewById( R.id.et_uid );
        et_uid.setOnClickListener( this );
        et_uid.setText( SharedPreferencesUtil.getUserUid() );

        et_key = (EditText) findViewById( R.id.et_key );
        et_key.setOnClickListener( this );
        et_key.setText( SharedPreferencesUtil.getUserKey() );

        tv_confirm = (TextView) findViewById( R.id.tv_confirm );
        tv_confirm.setOnClickListener( this );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                String uid = et_uid.getText().toString().trim();
                String key = et_key.getText().toString().trim();
                if ( uid.equals("") || key .equals("") ){
                    Toast.makeText(mContext, "设备ID和KEY不能为空", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent();
                intent.putExtra("type", "setOk");
                intent.putExtra("uid", uid);
                intent.putExtra("key", key);
                setResult(RESULT_OK , intent);
                finish();
                break;

        }
    }
}
