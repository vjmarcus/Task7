package com.example.task7.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task7.db.StoryDao;
import com.example.task7.db.StoryDatabase;
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
    private StoryDao storyDao;
    private NewsApi newsApi;
    private MutableLiveData<List<Story>> allStories = new MutableLiveData<>();
    private LiveData<List<Story>> allStoriesLiveData;

    public StoryRepository(Application application) {
        StoryDatabase db = StoryDatabase.getInstance(application);
        storyDao = db.storyDao();
        ApiFactory apiFactory = ApiFactory.getInstance();
        newsApi = ApiFactory.getNewsApi();
        allStoriesLiveData = storyDao.getAllStories();
    }

    public LiveData<List<Story>> getLiveDataFromWeb(String key) {
        deleteAllStoriesInDb();
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
                allStories.setValue(storyList);
                addStoriesToDatabase();
            }

            @Override
            public void onFailure(Call<StoryList> call, Throwable t) {
                Log.d(TAG, "onFailure: error= " + t.getMessage());
            }
        });
        return allStories;
    }

    public LiveData<List<Story>> getLiveDataFromDb() {
        return allStoriesLiveData;
    }

    private void addStoriesToDatabase() {
        Log.d(TAG, "addStoriesToDatabase: ");
        for (int i = 0; i < storyList.size(); i++) {
            insert(storyList.get(i));
        }
    }

    public void deleteAllStoriesInDb() {
        new DeleteAllStoriesAsyncTask(storyDao).execute();
    }

    private void insert(Story story) {
        new InsertStoryAsyncTask(storyDao).execute(story);
    }

    private class InsertStoryAsyncTask extends AsyncTask<Story, Void, Void> {
        private StoryDao storyDao;

        public InsertStoryAsyncTask(StoryDao storyDao) {
            this.storyDao = storyDao;
        }

        @Override
        protected Void doInBackground(Story... stories) {
            storyDao.insert(stories[0]);
            return null;
        }
    }

    private class DeleteAllStoriesAsyncTask extends AsyncTask<Void, Void, Void> {

        private StoryDao storyDao;

        public DeleteAllStoriesAsyncTask(StoryDao storyDao) {
            this.storyDao = storyDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            storyDao.deleteAllStories();
            return null;
        }
    }
}
