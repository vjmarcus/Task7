package com.example.task7.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.task7.api.ApiFactory;
import com.example.task7.api.NewsApi;
import com.example.task7.data.Story;
import com.example.task7.data.StoryList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryRepository {
    private static final String TAG = "MyApp";
    private List<Story> storyList;
    private ApiFactory apiFactory;
    private NewsApi newsApi;
    private MutableLiveData<List<Story>> mutableLiveData = new MutableLiveData<>();
    private Application application;

    public StoryRepository(Application application) {
        this.application = application;
        apiFactory = ApiFactory.getInstance();
        newsApi = ApiFactory.getNewsApi();
    }

    public MutableLiveData<List<Story>> getMutableLiveData(String key) {
        Call<StoryList> call = newsApi.getPostsByDate(key, ApiFactory.getCurrentDate(),
                ApiFactory.getCurrentDate(), 20, "en", ApiFactory.API_KEY);
        call.enqueue(new Callback<StoryList>() {
            @Override
            public void onResponse(@NonNull Call<StoryList> call, @NonNull Response<StoryList> response) {
                Log.d(TAG, "onResponse: " + response);
                StoryList articlesList = response.body();
                if (articlesList != null) {
                    storyList = articlesList.getArticles();
                    Log.d(TAG, "Good onResponse: " + storyList.size());
                } else {
                    Log.d(TAG, "bad onResponse:");
                }
                mutableLiveData.setValue(storyList);
            }

            @Override
            public void onFailure(Call<StoryList> call, Throwable t) {
                Log.d(TAG, "onFailure: error= " + t.getMessage());
            }
        });
        return mutableLiveData;
    }
}
