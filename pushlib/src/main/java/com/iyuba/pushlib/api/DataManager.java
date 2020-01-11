package com.iyuba.pushlib.api;

import com.iyuba.module.toolbox.SingleParser;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class DataManager {

    private static DataManager sInstance = new DataManager();

    private static final SimpleDateFormat CREDIT_SDF = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);


    public static DataManager getInstance() {
        return sInstance;
    }

    private final AiService mAiService;

    private DataManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        //if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        //}
        OkHttpClient client = builder.build();
        SimpleXmlConverterFactory xmlFactory = SimpleXmlConverterFactory.create();
        GsonConverterFactory gsonFactory = GsonConverterFactory.create();
        RxJava2CallAdapterFactory rxJavaFactory = RxJava2CallAdapterFactory.create();

        mAiService = AiService.Creator.createService(client, gsonFactory, rxJavaFactory);


    }

    public Single<Boolean> setPushData(int userId, String token, int sendFlg,String appID,String platApp,
                                       String appKey,String appSecret,String packageName) {
        return mAiService.setPushData(token,appID,sendFlg,userId,appKey,appSecret,packageName,platApp)
                .compose(this.<AiResponse.SetPush, Boolean>applyParser());
    }

    // ----------------------- divider ---------------------------

    @SuppressWarnings("unchecked")
    private <T, R> SingleTransformer<T, R> applyParser() {
        return (SingleTransformer<T, R>) SingleParser.parseTransformer;
    }
}
