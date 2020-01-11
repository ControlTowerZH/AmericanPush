package com.iyuba.pushlib.api;

import com.iyuba.module.mvp.MvpView;

public interface SetPushMvpView extends MvpView {
    void setPushSuccess(String token);
    void setPushError();
}
