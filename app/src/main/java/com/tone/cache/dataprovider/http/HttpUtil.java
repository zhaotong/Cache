package com.tone.cache.dataprovider.http;

import android.util.Log;

import com.google.gson.Gson;
import com.tone.cache.dataprovider.DataResult;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {

    private static HttpUtil httpUtil;

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    private final String BASE_URL = "http://120.24.159.27:2222";

    public static HttpUtil getInstance() {
        if (httpUtil == null)
            synchronized (HttpUtil.class) {
                if (httpUtil == null)
                    httpUtil = new HttpUtil();
            }
        return httpUtil;
    }

    private HttpUtil() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain
//                                .request()
//                                .newBuilder()
//                                .header("", "")
//                                .build();
//                        return chain.proceed(request);
//                    }
//                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    private OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain
                                .request()
                                .newBuilder()
                                .header("", "")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
        return okHttpClient;
    }

    private Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public <T> Observable<DataResult<T>> postData(String url, Map<String, String> map, Class<T> clazz) {
        return retrofit
                .create(Api.class)
                .getDate(url, map)
                .flatMap(new Function<ResponseBody, Observable<DataResult<T>>>() {
                    @Override
                    public Observable<DataResult<T>> apply(ResponseBody responseBody) throws Exception {
                        String body = responseBody.string();
                        Log.d("http_data", "" + body);
                        Gson gson = new Gson();
                        //data 不能是list，必须为object
                        Type type = new ParameterizedTypeImpl(DataResult.class, new Class[]{clazz});
                        DataResult<T> entity = gson.fromJson(body, type);
                        return Observable.just(entity);
                    }
                });
    }

}
