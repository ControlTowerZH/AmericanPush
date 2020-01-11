package com.iyuba.american;

import android.app.Application;
import android.util.Log;

import com.iyuba.american.push.LogUtil;
import com.iyuba.pushlib.PushApplication;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.IPushInitCallback;
import com.xuexiang.xpush.huawei.HuaweiPushClient;
import com.xuexiang.xpush.xiaomi.XiaoMiPushClient;
import com.xuexiang.xutil.system.RomUtils;

import static com.xuexiang.xutil.system.RomUtils.SYS_EMUI;
import static com.xuexiang.xutil.system.RomUtils.SYS_MIUI;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //initVivoPush();
        //initPush();

        PushApplication.initPush(this);
       // String romName = RomUtils.getRom().getRomName();
       // if (!romName.equals(SYS_COLOROS)) {//如果不是OPPO 走XPush
       //    initPushAll();
       // }
    }

    /**
     * 动态注册初始化推送
     */
    private void initPushAll() {
        XPush.debug(BuildConfig.DEBUG);
        //动态注册，根据平台名或者平台码动态注册推送客户端
        XPush.init(this, new IPushInitCallback() {
            @Override
            public boolean onInitPush(int platformCode, String platformName) {
                String romName = RomUtils.getRom().getRomName();
                    if (romName.equals(SYS_EMUI)) {
                    return platformCode == HuaweiPushClient.HUAWEI_PUSH_PLATFORM_CODE && platformName.equals(HuaweiPushClient.HUAWEI_PUSH_PLATFORM_NAME);
                } else if (romName.equals(SYS_MIUI)) {
                    return platformCode == XiaoMiPushClient.MIPUSH_PLATFORM_CODE && platformName.equals(XiaoMiPushClient.MIPUSH_PLATFORM_NAME);
                } else {
                    //return platformCode == JPushClient.JPUSH_PLATFORM_CODE && platformName.equals(JPushClient.JPUSH_PLATFORM_NAME);
                    return platformCode == XiaoMiPushClient.MIPUSH_PLATFORM_CODE && platformName.equals(XiaoMiPushClient.MIPUSH_PLATFORM_NAME);
                }
            }
        });
        XPush.register();
    }

    /**
     * 静态注册初始化推送
     */
    private void initPush() {
        XPush.debug(BuildConfig.DEBUG);
        //静态注册，指定使用友盟推送客户端
        XPush.init(this, new HuaweiPushClient());
        XPush.register();
    }

    /**
     * 初始化vivo推送
     */
    protected void initVivoPush() {
        // (28671)systemPushPkgName is null
        boolean isSupport=PushClient.getInstance(getApplicationContext()).isSupport();
        LogUtil.e("是否支持VIVO"+isSupport);
        //初始化vivo推送
        PushClient.getInstance(getApplicationContext()).initialize();
        //并且打开推送服务
        PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                if (i == 0) {
                    Log.e("NPL", "打开推送服务成功");
                } else {
                    Log.e("NPL", "打开推送服务失败");
                }
            }
        });
    }
}
