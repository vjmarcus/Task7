package com.example.task7.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.task7.repository.StoryRepository;

// Может вызывать только методы модели, меняет свое состояние, своих полей
public class SecondActivityViewModel extends AndroidViewModel {

    private StoryRepository storyRepository;

    public SecondActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDataFromIntent() {

    }
}
