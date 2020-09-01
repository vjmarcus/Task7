package com.example.task7.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

// Может вызывать только методы модели, меняет свое состояние, своих полей
public class SecondActivityViewModel extends AndroidViewModel {

    public SecondActivityViewModel(@NonNull Application application) {
        super(application);
    }
}
