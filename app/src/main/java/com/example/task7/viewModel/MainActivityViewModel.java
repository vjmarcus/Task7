package com.example.task7.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task7.MainActivity;
import com.example.task7.data.Story;
import com.example.task7.repository.StoryRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = "MyApp";
    private StoryRepository storyRepository;
    private String key;
    private long updatedTime;
    private String updatedTopic;
    public LiveData<List<Story>> liveDataViewModel;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainActivityViewModel: updatedTime is " + updatedTime);
    }

    public void initRepository() {
        storyRepository = new StoryRepository(getApplication());
    }

    public LiveData<List<Story>> getLiveDataViewModel() {
        return liveDataViewModel;
    }

    public void update(String currentTopic) {
        if (loadFromDbOrLoadFromWEb(currentTopic) ) {
            Log.d(TAG, "viewModel getAllStoryData: load from db");
            liveDataViewModel  = storyRepository.getLiveDataFromDb();
        } else {
            Log.d(TAG, "viewModel getAllStoryData: load from WEB");
            liveDataViewModel  = storyRepository.getLiveDataFromWeb(key);
        }
    }

    public void setCurrentRequestParam(String currentKey) {
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
