package com.example.task7.interfaces;

import com.example.task7.data.Story;

import java.util.List;

public interface LoadStoryCallback {
    void onCompleteCallback(List<Story> storyList);
}
