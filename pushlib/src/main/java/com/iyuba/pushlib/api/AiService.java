package com.iyuba.pushlib.api;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AiService {
    String ENDPOINT = "http://ai.iyuba.cn/";

    //http://ai.iyuba.cn/management/androidtoken.jsp?token=CN_bb876988c3b76173af90b4b21fcefbb6&appID=121212&userId=1&sendFlg=1
    @GET("management/androidtoken.jsp")
    Single<AiResponse.SetPush> setPushData(@Query("token") String token,
                                           @Query("fromapp") String appID,
                                           @Query("sendFlg") int sendFlg,
                                           @Query("userId") int userId,
                                           @Query("appkey") String appKey,
                                           @Query("appsecret") String appSecret,
                                           @Query("packagename") String packageName,
                                           @Query("platform") String platForm);

    class Creator {
        public static AiService createService(OkHttpClient client, GsonConverterFactory gsonFactory, RxJava2CallAdapterFactory rxJavaFactory) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .client(client)
                    .addConverterFactory(gsonFactory)
                    .addCallAdapterFactory(rxJavaFactory)
                    .build();
            return retrofit.create(AiService.class);
        }
    }
}
