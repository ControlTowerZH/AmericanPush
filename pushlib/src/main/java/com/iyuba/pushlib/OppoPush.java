package com.iyuba.pushlib;

import android.content.Context;
import android.util.Log;

import com.heytap.mcssdk.PushManager;
import com.heytap.mcssdk.callback.PushAdapter;
import com.heytap.mcssdk.callback.PushCallback;
import com.huawei.android.hms.agent.HMSAgent;
import com.xuexiang.xutil.system.RomUtils;

import static com.xuexiang.xutil.system.RomUtils.SYS_COLOROS;

public class OppoPush {


    private static final OppoPush Instance = new OppoPush();

    public static OppoPush getInstance() {
        return Instance;
    }

    public  Token token;

    public void initOppoPush(Context context,String appKey, String appSecret) {
        String system=RomUtils.getRom().getRomName();
        if(system.equals(SYS_COLOROS)||system.equals("OPPO")) {
            HMSAgent.Push.enableReceiveNormalMsg(true, null);
            try {
                PushManager.getInstance().register(context, appKey, appSecret, mPushCallback);//setPushCallback接口也可设置callback
            } catch (Exception e) {
                e.printStackTrace();
                //ToastFactory.showShort(context,e.getMessage());
            }
        }
    }

    //OPPO状态回调
    private PushCallback mPushCallback = new PushAdapter() {
        @Override
        public void onRegister(int code, String s) {
            if (code == 0) {
                token=new Token();
                token.token=s;
                token.code=code;
                Log.e("oppo push success", "registerId:" + s);
                //InitPush initPush =new InitPush();
                //initPush.registerToken(mContext);
            } else {
                Log.e("oppo push Error", "registerId:" + s+"code"+code);
            }
        }

        @Override
        public void onGetPushStatus(final int code, int status) {
            if (code == 0 && status == 0) {
                Log.e("Push状态正常", "status:" + status);
            } else {
                Log.e("Push状态错误", "status:" + status);
            }
        }

        @Override
        public void onGetNotificationStatus(final int code, final int status) {
            if (code == 0 && status == 0) {
                Log.e("通知状态正常", "status:" + status);
            } else {
                Log.e("通知状态错误", "status:" + status);
            }
        }
    };

}
