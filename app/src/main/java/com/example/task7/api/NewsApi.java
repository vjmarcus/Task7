package com.example.task7.api;

import com.example.task7.data.StoryList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("everything")
    Call <StoryList>  getPostsByDate(@Query("q") String key,
                                     @Query("from") String fromDate,
                                     @Query("to") String toDate,
                                     @Query("pageSize") int pageSize,
                                     @Query("language") String language,
                                     @Query("apiKey") String apiKey);
}
