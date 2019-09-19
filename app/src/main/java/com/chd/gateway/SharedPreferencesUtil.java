package com.chd.gateway;
import android.app.Application;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static SharedPreferences userShare;

    private static void initUserShare() {
        if (userShare == null) {
            userShare = CHDApplication.getContext().getSharedPreferences("userGuide", Application.MODE_PRIVATE);
        }
    }

    public static void setUserUid(String s) {
        initUserShare();
        userShare.edit().putString("userUid", s).commit();
    }

    public static String getUserUid() {
        initUserShare();
        return userShare.getString( "userUid", "" );
    }

    public static void setUserKey(String s) {
        initUserShare();
        userShare.edit().putString("userKey", s).commit();
    }

    public static String getUserKey() {
        initUserShare();
        return userShare.getString( "userKey", "" );
    }
}
