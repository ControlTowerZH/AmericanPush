package com.iyuba.american.push;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

public class IVivoPushMessageReceiver extends OpenClientPushMessageReceiver {
    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage upsNotificationMessage) {
        long msgId;
        String customeContent = "";
        if (upsNotificationMessage != null) {
            msgId = upsNotificationMessage.getMsgId();
            customeContent = upsNotificationMessage.getSkipContent();
            Log.e("NPL", "获取通知内容如下:msgId = " + msgId + ";customeContent=" + customeContent);
        }
    }
    @Override
    public void onReceiveRegId(Context context, String s) {
        if (TextUtils.isEmpty(s)) {
            //获取regId失败
            Log.e("NPL", "获取RegId失败");
        } else {
            Log.e("NPL", "获取RegId成功，regid = " + s);
        }
    }
}
