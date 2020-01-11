package com.iyuba.pushlib.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {
    private static final String PREF_NAME = "push_series_info";
    private static SPHelper sInstance;

    public static void init(Context appContext) {
        if (sInstance == null) {
            sInstance = new SPHelper(appContext);
        }
    }

    public static SPHelper getInstance() {
        if (sInstance == null) throw new NullPointerException("not init");
        return sInstance;
    }

    private final SharedPreferences mPref;

    private SPHelper(Context appContext) {
        mPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void putShowPushSwitch(boolean value) {
        mPref.edit().putBoolean("dialog_switch", value).apply();
    }

    public boolean getShowPushSwitch() {
        return mPref.getBoolean("dialog_switch", true);
    }

    public void setPushStatues(boolean statues){
        mPref.edit().putBoolean("push_statues", statues).apply();
    }

    public boolean getPushStatues() {
        return mPref.getBoolean("push_statues",false);
    }

    public void putPushToken(String token){
        mPref.edit().putString("push_token", token).apply();
    }

    public String getPushToken() {
        return mPref.getString("push_token","");
    }

    public void putPushUser(int userId){
        mPref.edit().putInt("push_user", userId).apply();
    }

    public int getPushUser() {
        return mPref.getInt("push_user",0);
    }

}
