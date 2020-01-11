package com.iyuba.pushlib.api;

import android.util.Log;

import com.iyuba.module.mvp.BasePresenter;
import com.iyuba.module.toolbox.RxUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SetPushPresenter extends BasePresenter<SetPushMvpView> {
    private final DataManager mDataManager;

    private Disposable mDisposable;

    public SetPushPresenter() {
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void detachView() {
        RxUtil.dispose(mDisposable);
        super.detachView();
    }

    public void setTokenData(int userId, final String token, int sendFlg, String appID, String platApp,
                             String appKey,String appSecret,String packageName) {
        RxUtil.dispose(mDisposable);
        mDisposable = mDataManager.setPushData(userId,token, sendFlg,appID,platApp,appKey,appSecret,packageName)
                .compose(RxUtil.<Boolean>applySingleIoSchedulerWith(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (isViewAttached()) {
                        }
                    }
                }))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean pair) throws Exception {
                        if (isViewAttached()) {
                            if (pair) {
                                getMvpView().setPushSuccess(token);
                            } else {
                                getMvpView().setPushError();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (isViewAttached()) {
                            getMvpView().setPushError();
                            Log.e("Error","error");
                        }
                    }
                });
    }
}
