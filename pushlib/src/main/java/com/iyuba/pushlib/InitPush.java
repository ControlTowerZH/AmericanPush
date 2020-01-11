package com.iyuba.pushlib;

import android.app.Application;
import android.content.Context;
import android.support.annotation.MainThread;
import android.util.Log;

import com.heytap.mcssdk.PushManager;
import com.iyuba.pushlib.api.SetPushMvpView;
import com.iyuba.pushlib.api.SetPushPresenter;
import com.iyuba.pushlib.helper.SPHelper;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.XPushManager;
import com.xuexiang.xpush.core.queue.impl.MessageSubscriber;
import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.xiaomi.XiaoMiPushClient;
import com.xuexiang.xutil.system.RomUtils;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNREGISTER;
import static com.xuexiang.xutil.system.RomUtils.SYS_COLOROS;
import static com.xuexiang.xutil.system.RomUtils.SYS_EMUI;
import static com.xuexiang.xutil.system.RomUtils.SYS_MIUI;

public class InitPush implements SetPushMvpView {
    private Context mContext;
    private int mUserId;
    private String mAppId;
    private String mAppKey;
    private String mAppSecret;
    private GetSignPush mGetSignPush;
    public boolean isShowToast;

    private static final InitPush Instance = new InitPush();

    public static InitPush getInstance() {
        return Instance;
    }

    public void setInitPush(Context context,PushConfig config) {
        mContext = context;
        String system = RomUtils.getRom().getRomName();
        Log.e("手机系统", system);
        switch (system) {
            case SYS_COLOROS:
            case "OPPO":
                mAppId = config.OPPO_ID;
                mAppKey = config.OPPO_KEY;
                mAppSecret = config.OPPO_MASTER_SECRET;
                break;
            case SYS_EMUI:
                mAppId = config.HUAWEI_ID;
                mAppKey = "0";
                mAppSecret = config.HUAWEI_SECRET;
                break;
            default:
                mAppId = config.MI_ID;
                mAppKey = config.MI_KEY;
                mAppSecret = config.MI_SECRET;
                break;
        }

        OppoPush.getInstance().initOppoPush(context, config.OPPO_KEY, config.OPPO_SECRET);
        OpenPushSwitch.openPush(context);//系统消息通知开关
        PushChannel.notifyChannel(context);//消息通道
        XPushManager.get().register(mMessageSubscriber);//穿透消息接收开启
        //XPushManager.get().unregister(mMessageSubscriber);//穿透消息接收关闭
    }

    public boolean isOtherPush() {
        String romName = RomUtils.getRom().getRomName();
        return !romName.equals("OPPO") && !romName.equals(SYS_COLOROS) && !romName.equals(SYS_MIUI) && !romName.equals(SYS_EMUI);
        //&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void resetPush(Application application) {
        XPush.debug(BuildConfig.DEBUG);
        XPush.init(application, new XiaoMiPushClient());
        XPush.register();
    }

    public void registerToken(Context context, int userId){
        tokenRegister(context,true,userId);
    }
    public void unRegisterToken(Context context, int userId) {
        tokenRegister(context,false,userId);

    }

    private void tokenRegister(Context context,boolean isRegister, int userId){
        Token token = getToken(context);
 //       SPHelper spHelper = SPHelper.getInstance();
//        if (spHelper.getPushUser() == userId && spHelper.getPushStatues()) {
//            return;//代表已经这个uid 已经注册过
//        }
        mUserId=userId;
        SetPushPresenter presenter = new SetPushPresenter();
        presenter.attachView(this);

        if (token != null && !token.token.isEmpty()) {
            String platForm;
            switch (RomUtils.getRom().getRomName()) {
                case SYS_COLOROS:
                case "OPPO":
                    platForm = "oppo";
                    break;
                case SYS_EMUI:
                    platForm = "huawei";
                    break;
                default:
                    platForm = "xiaomi";
            }
            //sendFlg:1 接收推送信息  0 不接收
            presenter.setTokenData(userId, token.token, isRegister?1:0, mAppId, platForm, mAppKey,
                    mAppSecret,context.getPackageName());
        }else {
           ToastFactory.showShort(context,"token 为空，不能注册！");
        }
    }

    public Token getToken(Context context) {
        mContext = context;
        Token token;
        String system = RomUtils.getRom().getRomName();
        if (system.equals(SYS_COLOROS) || system.equals("OPPO")) {
            try {
                token = OppoPush.getInstance().token;
                if (token == null || token.token.isEmpty()) {
                    try {
                        PushManager.getInstance().getRegister();
                        //token = OppoPush.getInstance().token;//有延时！！！
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("oppo", e.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                token=new Token();
                token.token="OPPO Token获取失败";
            }
        } else {
            token = getXToken();
        }
        return token;
    }

    private Token getSaveToken() {
        SPHelper spHelper = SPHelper.getInstance();
        Token token = new Token();
        if (spHelper.getPushUser() == mUserId && spHelper.getPushStatues()) {
            if (mGetSignPush != null) {
                mGetSignPush.signPush("通知注册成功！", mUserId, spHelper.getPushToken());
            }
            token.token = spHelper.getPushToken();
            token.code = 200;
        }
        return token;
    }


    private Token getXToken() {
        String Token = XPush.getPushToken();
        int Code = XPush.getPlatformCode();
        Token token = new Token();
        token.code = Code;
        token.token = Token;
        return token;
    }


    @Override
    public void setPushSuccess(String token) {
        ToastFactory.showShort(mContext, "通知注册成功！");
        SPHelper spHelper = SPHelper.getInstance();
        spHelper.setPushStatues(true);
        spHelper.putPushToken(token);
        spHelper.putPushUser(mUserId);
        if (mGetSignPush != null) {
            mGetSignPush.signPush("通知注册成功！", mUserId, token);
        }
    }

    @Override
    public void setPushError() {
        ToastFactory.showShort(mContext, "通知注册失败！");
        SPHelper spHelper = SPHelper.getInstance();
        spHelper.setPushStatues(false);
        spHelper.putPushToken("");
        if (mGetSignPush != null) {
            mGetSignPush.signPush("通知注册失败！", mUserId, "错误");
        }
    }

    public interface GetSignPush {
        void signPush(String message, int userId, String token);
    }

    public void setSignPushCallback(GetSignPush getSignPush) {
        mGetSignPush = getSignPush;
    }

    private void showMessage(String message) {
        //ToastFactory.showShort(mContext, message);
    }

    private MessageSubscriber mMessageSubscriber = new MessageSubscriber() {
        @MainThread
        @Override
        public void onMessageReceived(CustomMessage message) {
            showMessage(message.getMsg());
            Log.e("message", message.toString());
        }

        @MainThread
        @Override
        public void onNotification(Notification notification) {
        }

        @MainThread
        @Override
        public void onConnectStatusChanged(int connectStatus) {
            //tvStatus.setText(PushUtils.formatConnectStatus(connectStatus));
        }

        @MainThread
        @Override
        public void onCommandResult(XPushCommand command) {
            if (!command.isSuccess()) {
                return;
            }
            switch (command.getType()) {
                case TYPE_REGISTER:
                    break;
                case TYPE_UNREGISTER:
                    break;
                case TYPE_GET_TAG:
                    break;
                case TYPE_GET_ALIAS:
                    break;
                default:
                    break;
            }
        }
    };
}
