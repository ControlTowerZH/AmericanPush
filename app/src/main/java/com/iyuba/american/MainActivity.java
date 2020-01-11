package com.iyuba.american;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.heytap.mcssdk.PushManager;
import com.iyuba.american.push.LogUtil;
import com.iyuba.pushlib.InitPush;
import com.iyuba.pushlib.PushApplication;
import com.iyuba.pushlib.PushConfig;
import com.iyuba.pushlib.Token;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xuexiang.xutil.system.RomUtils;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity  {
    private Context mContext;
    private Button mCheck, mPuse, mRegId, mPackage, mMiRegId,mVivoStart;
    private EditText mTvRegid;//btn_get_package
    private InitPush mInitPush;
    private TextView mTvPushInfo,mTvSystem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mInitPush=InitPush.getInstance();
        if (mInitPush.isOtherPush()){//重新注册
            MainActivityPermissionsDispatcher.requestPushWithPermissionCheck(MainActivity.this);
        }
        PushConfig config = new PushConfig();
        config.HUAWEI_ID="10181585";
        config.HUAWEI_SECRET="wuux1kg6bg34cnrhhcgakouc7irmns67";
        config.MI_ID="2882303761517251113";
        config.MI_KEY="5811725177113";
        config.MI_SECRET="y7DQFxT4p5Gcf2yV+CgxTA==";
        config.OPPO_ID="549140";
        config.OPPO_KEY="5BaBaU49lp4w8sOs0wGwc8s4W";
        config.OPPO_SECRET="3F69B49fd835385a14D6c5E867f5E37A";
        config.OPPO_MASTER_SECRET="813aA793B4be82697C79b1a97B20ec07";//我日
        mInitPush.setInitPush(mContext,config);//zh101 6551060
        mInitPush.setSignPushCallback(getSignPush);



        mCheck = findViewById(R.id.btn_check_opush_support);
        mPuse = findViewById(R.id.btn_get_push_service_status);
        mRegId = findViewById(R.id.btn_get_regid);
        mPackage = findViewById(R.id.btn_get_package);
        mMiRegId = findViewById(R.id.btn_mi_regid);
        mTvRegid = findViewById(R.id.tv_reg_id);
        mVivoStart = findViewById(R.id.btn_vivo);
        mTvPushInfo =findViewById(R.id.tv_push_sign);
        mTvSystem =findViewById(R.id.tv_system);

        mTvSystem.setText(RomUtils.getRom().getRomName());

        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushApplication.initPush(getApplication());//????
                ToastUtil.showLongToast(mContext, "isSupportPush:" + PushManager.isSupportPush(mContext));
            }
        });

        mPuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PushManager.getInstance().getPushStatus();
                } catch (Exception e) {
                    LogUtil.e(e);
                    ToastUtil.showToast(mContext, e.getMessage());
                }
            }
        });
        mRegId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });
        mPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showLongToast(mContext, getPackageName());
            }
        });
        mMiRegId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
                mInitPush.registerToken(mContext,6551060);//手动注册了！！！
            }
        });
        mVivoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initVivoPush();
                startActivity(new Intent(mContext,TestActivity.class));
            }
        });
    }

    private void getToken(){
        //应该延时调用
        Token token=mInitPush.getToken(mContext);
        if (token!=null) {
            String Token = token.token;
            //int Code = token.code;
             ToastUtil.showLongToast(mContext, "Token:" + token.token + "code:" + token.code);
            LogUtil.e("Token:" + Token);
            mTvRegid.setText("XToken:" + Token);
        }
    }

    private InitPush.GetSignPush getSignPush=new InitPush.GetSignPush() {
        @Override
        public void signPush(String message, int userId,String token) {
             mTvPushInfo.setText("推送后台注册情况："+message+"用户："+userId);
            LogUtil.e("推送后台注册情况："+message+"用户："+userId+"Token:" + token);
            mTvRegid.setText("XToken:" + token);
        }
    };

    private void initVivoPush() {
        // (28671)systemPushPkgName is null
        final boolean isSupport=PushClient.getInstance(getApplicationContext()).isSupport();
        LogUtil.e("是否支持VIVO"+isSupport);
        //初始化vivo推送
        PushClient.getInstance(getApplicationContext()).initialize();
        //并且打开推送服务
        PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                if (i == 0) {
                    ToastUtil.showToast(mContext,"打开推送服务成功");
                    Log.e("NPL", "打开推送服务成功");
                } else {
                    ToastUtil.showToast(mContext,"打开推送服务失败"+isSupport+"code:"+i);
                    Log.e("NPL", "打开推送服务失败"+i);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void requestPush() {
       mInitPush.resetPush(getApplication());
    }

    @OnPermissionDenied({android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void requestPushDenied() {
        ToastUtil.showToast(mContext, "申请权限失败,无法正常进行推送!");
    }
}

