package com.iyuba.pushlib;

import android.app.Application;

import com.iyuba.pushlib.helper.SPHelper;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.IPushInitCallback;
import com.xuexiang.xpush.huawei.HuaweiPushClient;
import com.xuexiang.xpush.xiaomi.XiaoMiPushClient;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.system.RomUtils;

import static com.xuexiang.xutil.system.RomUtils.SYS_EMUI;

//import com.xuexiang.xpush.jpush.JPushClient;

public class PushApplication {

    public static  void initPush(Application application){
        SPHelper.init(application.getApplicationContext());
        XUtil.init(application);
        String romName = RomUtils.getRom().getRomName();
        initPushAll(application);
//        if (!romName.equals(SYS_COLOROS)&&!romName.equals("OPPO")) {//如果不是OPPO 走XPush
//            initPushAll(application);
//        }
    }

    /**
     * 动态注册初始化推送
     */
    private static void initPushAll(Application application) {
        XPush.debug(BuildConfig.DEBUG);
        //动态注册，根据平台名或者平台码动态注册推送客户端
        XPush.init(application, new IPushInitCallback() {
            @Override
            public boolean onInitPush(int platformCode, String platformName) {
                String romName = RomUtils.getRom().getRomName();
                if (romName.equals(SYS_EMUI)) {
                    return platformCode == HuaweiPushClient.HUAWEI_PUSH_PLATFORM_CODE && platformName.equals(HuaweiPushClient.HUAWEI_PUSH_PLATFORM_NAME);
                } else {
                    //return platformCode == JPushClient.JPUSH_PLATFORM_CODE && platformName.equals(JPushClient.JPUSH_PLATFORM_NAME);
                    return platformCode == XiaoMiPushClient.MIPUSH_PLATFORM_CODE && platformName.equals(XiaoMiPushClient.MIPUSH_PLATFORM_NAME);
                }
            }
        });
        XPush.register();
    }

}
