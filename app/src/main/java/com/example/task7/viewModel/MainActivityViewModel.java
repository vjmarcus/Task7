package com.example.task7.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.task7.data.Story;
import com.example.task7.repository.StoryRepository;

import java.util.List;

// Может вызывать только методы модели, меняет свое состояние, своих полей
public class MainActivityViewModel extends AndroidViewModel {

    private StoryRepository storyRepository;
    private String key;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        storyRepository = new StoryRepository(application);
    }

    public LiveData<List<Story>> getAllStoryData() {
        return storyRepository.getMutableLiveData(key);
    }

    public void setCurrentKey(String currentKey) {
        this.key = currentKey;
    }
}
