package com.example.task7.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.task7.data.Story;
import com.example.task7.repository.StoryRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = "MyApp";
    private StoryRepository storyRepository;
    private String key;
    private long updatedTime;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        storyRepository = new StoryRepository(application);
        updatedTime = 0;
        Log.d(TAG, "MainActivityViewModel: updatedTime is " + updatedTime);
    }

    public LiveData<List<Story>> getAllStoryData() {
        if (loadFromDbOrLoadFromWEb()) {
            return storyRepository.getLiveDataFromDb();
        } else {
            return storyRepository.getLiveDataFromWeb(key);
        }
    }

    public void setCurrentKey(String currentKey) {
        this.key = currentKey;
    }

    private Boolean loadFromDbOrLoadFromWEb() {
        long currentTime = System.currentTimeMillis();
        Log.d(TAG, "getAllStoryData: current time is " + currentTime);
        Log.d(TAG, "difference is " + (currentTime - updatedTime));
        if ((currentTime - updatedTime) < 60000){
            Log.d(TAG, "getAllStoryData return from db");
            updatedTime = System.currentTimeMillis();
            return true;
        } else {
            Log.d(TAG, "getAllStoryData return from web");
            updatedTime = System.currentTimeMillis();
            return false;
        }
    }
}
