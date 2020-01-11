package com.iyuba.pushlib;

import android.content.Context;

import com.huawei.hms.support.api.push.PushReceiver;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.PushUtils;

import java.nio.charset.Charset;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.CONNECTED;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.DISCONNECT;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_OK;
import static com.xuexiang.xpush.huawei.HuaweiPushClient.HUAWEI_PUSH_PLATFORM_NAME;

public class HuaweiReceiver extends PushReceiver {

    private static final String TAG = "HuaweiPush-";

    @Override
    public void onToken(Context context, String token) {
        PushLog.d(TAG + "[onToken]:" + token);
        PushUtils.savePushToken(HUAWEI_PUSH_PLATFORM_NAME, token);
        XPush.transmitCommandResult(context, TYPE_REGISTER, RESULT_OK, token, null, null);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        PushLog.d(TAG + "[onPushState]:" + pushState);
        XPush.transmitConnectStatusChanged(context, pushState ? CONNECTED : DISCONNECT);
    }

    @Override
    public void onPushMsg(Context context, byte[] bytes, String token) {
        String msg = new String(bytes, Charset.forName("UTF-8"));
        PushLog.d(TAG + "[onPushMsg]:" + msg);
        XPush.transmitMessage(context, msg, null, null);
        //ToastUtil.showToast(context,"透传消息处理:"+msg);
    }
}

