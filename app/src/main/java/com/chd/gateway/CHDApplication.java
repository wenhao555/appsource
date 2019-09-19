package com.chd.gateway;

import android.app.Application;
import android.content.Context;

public class CHDApplication extends Application {
    public static Context mAppLicationContext;

    @Override
    public void onCreate() {
        mAppLicationContext = this;
        super.onCreate();
    }

    public static Context getContext() {
        if(mAppLicationContext == null){
            mAppLicationContext = getContext();
        }
        return mAppLicationContext;
    }
}
