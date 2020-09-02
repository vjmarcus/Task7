package com.example.task7;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.task7.data.Story;

import java.util.List;

@Dao
public interface StoryDao {

    @Insert
    void insert(Story story);

    @Query("SELECT * FROM story_table")
    LiveData<List<Story>> getAllStories();
}
