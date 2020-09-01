package com.example.task7.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    public static final String API_KEY = "92632cb5e9be43ef9dfc1545d021180a";
    public static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String TAG = "MyApp";
    public static ApiFactory apiFactory;
    private static Retrofit retrofit;

    public static synchronized ApiFactory getInstance() {
        if (apiFactory == null) {
            synchronized (ApiFactory.class) {
                if (apiFactory == null){
                    apiFactory = new ApiFactory();
                }
            }
        }
        return apiFactory;
    }

    public static NewsApi getNewsApi() {
        return retrofit.create(NewsApi.class);
    }

    private ApiFactory() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static String getCurrentDate() {
        long date = System.currentTimeMillis();
        String pattern = "YYYY-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(date));
    }
}
