package com.chd.gateway;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henry on 2016/4/11.
 * todo
 */
public class EndDeviceBean implements Serializable {

    private String status = "";
    private String wdval = "00";
    private String sdval = "00";
    private String mq2val = "";
    private String humval = "";
    private String lightval = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWdval() {
        return wdval;
    }

    public void setWdval(String wdval) {
        this.wdval = wdval;
    }

    public String getSdval() {
        return sdval;
    }

    public void setSdval(String sdval) {
        this.sdval = sdval;
    }

    public String getMq2val() {
        return mq2val;
    }

    public void setMq2val(String mq2val) {
        this.mq2val = mq2val;
    }

    public String getHumval() {
        return humval;
    }

    public void setHumval(String humval) {
        this.humval = humval;
    }

    public String getLightval() {
        return lightval;
    }

    public void setLightval(String lightval) {
        this.lightval = lightval;
    }

    @Override
    public String toString() {
        return "";
    }
}
