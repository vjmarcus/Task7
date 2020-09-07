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
    private String updatedTopic;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        storyRepository = new StoryRepository(application);
        updatedTime = 0;
        Log.d(TAG, "MainActivityViewModel: updatedTime is " + updatedTime);
    }

    public LiveData<List<Story>> getAllStoryData(String currentTopic) {
        if (loadFromDbOrLoadFromWEb(currentTopic) ) {
            Log.d(TAG, "viewModel getAllStoryData: load from db");
            return storyRepository.getLiveDataFromDb();
        } else {
            Log.d(TAG, "viewModel getAllStoryData: load from WEB");
            return storyRepository.getLiveDataFromWeb(key);
        }
    }

    public void setCurrentKey(String currentKey) {
        this.key = currentKey;
    }

    public void clearDb() {
        storyRepository.deleteAllStoriesInDb();
    }

    private Boolean loadFromDbOrLoadFromWEb(String currentTopic) {
        long currentTime = System.currentTimeMillis();
        if (((currentTime - updatedTime) < 60000) && currentTopic.equals(updatedTopic)){
            updatedTime = System.currentTimeMillis();
            return true;
        } else {
            updatedTime = System.currentTimeMillis();
            updatedTopic = currentTopic;
            return false;
        }
    }
}
