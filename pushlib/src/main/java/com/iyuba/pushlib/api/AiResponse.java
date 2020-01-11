package com.iyuba.pushlib.api;

import com.google.gson.annotations.SerializedName;
import com.iyuba.module.toolbox.SingleParser;

import io.reactivex.Single;

public interface AiResponse {
    class SetPush implements SingleParser<Boolean> {
        @SerializedName("result")
        public String result;
        @SerializedName("info")
        public String message;


        @Override
        public Single<Boolean> parse() {
            if (result.equals("true")) {
                return Single.just(true);
            } else {
                return Single.just(false);
            }
        }

    }
}
