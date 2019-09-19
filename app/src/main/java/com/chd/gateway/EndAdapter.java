package com.chd.gateway;

import android.content.Context;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */

public class EndAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<EndDeviceBean> mSearchList = new ArrayList<>();

    protected EndAdapterInterface endAdapterInterface;

    public interface EndAdapterInterface {
        void onItemClick( int position, String status );
    }

    public void setEndAdapterInterface( EndAdapterInterface a ){
        endAdapterInterface = a;
    }

    public EndAdapter(Context c) {
        this.mContext = c;
    }

    public ArrayList<EndDeviceBean> getmSearchList() {
        return mSearchList;
    }

    public void setmSearchList(ArrayList<EndDeviceBean> mSearchList) {
        this.mSearchList = mSearchList;
    }

    @Override
    public int getCount() {
        return mSearchList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mSearchList.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_enddevice, null);
            holder.ll_item6 = (LinearLayout) convertView.findViewById(R.id.ll_item6);
            holder.tv_header = (TextView) convertView.findViewById(R.id.tv_header);
            holder.tv_content1 = (TextView) convertView.findViewById(R.id.tv_content1);
            holder.tv_content2 = (TextView) convertView.findViewById(R.id.tv_content2);
            holder.tv_content3 = (TextView) convertView.findViewById(R.id.tv_content3);
            holder.tv_content4 = (TextView) convertView.findViewById(R.id.tv_content4);
            holder.tv_content5 = (TextView) convertView.findViewById(R.id.tv_content5);
            holder.tv_content6 = (TextView) convertView.findViewById(R.id.tv_content6);
            holder.iv_icon1 = (ImageView) convertView.findViewById(R.id.iv_icon1);
            holder.iv_icon4 = (ImageView) convertView.findViewById(R.id.iv_icon4);
            holder.iv_icon5 = (ImageView) convertView.findViewById(R.id.iv_icon5);
            holder.iv_icon6 = (ImageView) convertView.findViewById(R.id.iv_icon6);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EndDeviceBean endDeviceBean = mSearchList.get(position);
        holder.tv_header.setText( "终端" + (position + 1) );

        String status = endDeviceBean.getStatus().equals( "1" ) ? "在线" : "离线";
        int statusSrc = endDeviceBean.getStatus().equals( "1" ) ? R.drawable.online : R.drawable.offline;
        holder.tv_content1.setText( status );
        holder.iv_icon1.setImageResource( statusSrc );

        String wd = endDeviceBean.getWdval();
        if ( wd.substring( 0, 1 ).equals("0") ){
            wd = wd.substring( 1 );
        }
        holder.tv_content2.setText( wd + "℃" );

        String sd = endDeviceBean.getSdval();
        if ( sd.substring( 0, 1 ).equals("0") ){
            sd = sd.substring( 1 );
        }
        holder.tv_content3.setText( sd + "%" );

        if ( endDeviceBean.getMq2val().equals( "1" ) ){
            holder.tv_content4.setText( "正常" );
            holder.iv_icon4.setImageResource(R.drawable.mq2);
            // holder.tv_content4.setTextColor( mContext.getResources().getColor( R.color.color_77 ) );
            //TextPaint tp = holder.tv_content4.getPaint();
            //tp.setFakeBoldText(false);
        }else {
            holder.tv_content4.setText( "异常" );
            holder.iv_icon4.setImageResource(R.drawable.mq2_warn);
           // holder.tv_content4.setTextColor( mContext.getResources().getColor( R.color.color_66 ) );
            //TextPaint tp = holder.tv_content4.getPaint();
            //tp.setFakeBoldText(true);
        }

        if ( endDeviceBean.getHumval().equals( "1" ) ){
            holder.tv_content5.setText( "有人" );
            holder.iv_icon5.setImageResource(R.drawable.renti_warn);
           // holder.tv_content5.setTextColor( mContext.getResources().getColor( R.color.color_66 ) );
           // TextPaint tp = holder.tv_content5.getPaint();
           // tp.setFakeBoldText(true);
        }else {
            holder.tv_content5.setText( "无人" );
            holder.iv_icon5.setImageResource(R.drawable.renti);
            //holder.tv_content5.setTextColor( mContext.getResources().getColor( R.color.color_77 ) );
           // TextPaint tp = holder.tv_content5.getPaint();
           // tp.setFakeBoldText(false);
        }

        if ( endDeviceBean.getLightval().equals( "1" ) ){
            holder.tv_content6.setText( "开灯" );
            holder.iv_icon6.setImageResource(R.drawable.on);
           // holder.tv_content6.setTextColor( mContext.getResources().getColor( R.color.colorPrimary ) );
           // holder.tv_content6.setBackgroundResource( R.drawable.textview_border );
        }else {
            holder.tv_content6.setText( "关灯" );
            holder.iv_icon6.setImageResource(R.drawable.off);
           // holder.tv_content6.setTextColor( mContext.getResources().getColor( R.color.color_gray_99 ) );
           // holder.tv_content6.setBackgroundResource( R.drawable.textview_border2 );
        }

        holder.ll_item6.setTag( holder );
        holder.ll_item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                endAdapterInterface.onItemClick( position, viewHolder.tv_content6.getText().toString() );
            }
        });
        return convertView;
    }

    class ViewHolder {
        public LinearLayout ll_item6;
        public TextView tv_header;
        public TextView tv_content1;
        public TextView tv_content2;
        public TextView tv_content3;
        public TextView tv_content4;
        public TextView tv_content5;
        public TextView tv_content6;
        public ImageView iv_icon1;
        public ImageView iv_icon4;
        public ImageView iv_icon5;
        public ImageView iv_icon6;
    }
}
