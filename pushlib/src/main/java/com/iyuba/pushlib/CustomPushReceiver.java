package com.iyuba.pushlib;

import android.content.Context;

import com.xuexiang.xpush.core.receiver.impl.XPushReceiver;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.entity.XPushMsg;

public class CustomPushReceiver extends XPushReceiver {

    @Override
    public void onNotificationClick(Context context, XPushMsg msg) {
        super.onNotificationClick(context, msg);
        //打开自定义的Activity
//        Intent intent = IntentUtils.getIntent(context, TestActivity.class, null, true);
//        intent.putExtra(KEY_PARAM_STRING, msg.getContent());
//        intent.putExtra(KEY_PARAM_INT, msg.getId());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        ActivityUtils.startActivity(intent);
//        ToastFactory.showShort(context,"华为点击"+msg.toString());
//        Logger.e("华为点击"+msg.toString());
    }

    @Override
    public void onCommandResult(Context context, XPushCommand command) {
        super.onCommandResult(context, command);
        //ToastUtils.toast(command.getDescription());
    }

}