package com.example.task7.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.task7.data.Source;
import com.example.task7.data.Story;
import com.example.task7.db.StoryDao;
import com.example.task7.db.StoryDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class NewsApiTest {

    private StoryDao storyDao;
    private StoryDatabase storyDatabase;

    @Before
    public void createDb() throws Exception {
        storyDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry
                .getInstrumentation()
                .getContext(), StoryDatabase.class)
                .build();
        storyDao = storyDatabase.storyDao();
    }

    @Test
    public void whenInsertStoryThenReadTheSameOne() throws Exception {
        List<Story> storyList = StoryTestHelper.createFakeStoryList(1);
        storyDao.insert(storyList.get(0));
        List<Story> storyListDb = storyDao.getAllStoriesList();
        assertEquals(1, storyListDb.size());
        assertTrue(StoryTestHelper.ifStoryIdentical(storyList.get(0), storyListDb.get(0)));
    }

    @Test
    public void getAllStories() throws Exception {
        List<Story> storyList = StoryTestHelper.createFakeStoryList(10);
        for (int i = 0; i < storyList.size(); i++) {
            storyDao.insert(storyList.get(i));
        }
        StoryTestHelper.createFakeStoryLiveDataList(storyList);
        assertNotNull(StoryTestHelper.getCurrentLiveData());
    }

    @Test
    public void saveToDb() throws Exception {
        List<Story> storyList = StoryTestHelper.createFakeStoryList(20);
        for (int i = 0; i < storyList.size(); i++) {
            storyDao.insert(storyList.get(i));
        }
        assertEquals(20, storyDao.getAllStoriesList().size());
    }

    @Test
    public void ifDbCleared () throws Exception {
        List<Story> storyList = StoryTestHelper.createFakeStoryList(20);
        for (int i = 0; i < storyList.size(); i++) {
            storyDao.insert(storyList.get(i));
        }
        storyDao.deleteAllStories();
        assertEquals(0, storyDao.getAllStoriesList().size());
    }

    @After
    public void closeDb() throws Exception {
        storyDatabase.close();
    }

    public static class StoryTestHelper {

        private static MutableLiveData<List<Story>> liveData;

        public static MutableLiveData<List<Story>> getCurrentLiveData() {
            if (liveData == null) {
                liveData = new MutableLiveData<>();
            }
            return liveData;
        }

        public static List<Story> createFakeStoryList(int size) {
            List<Story> storyList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                storyList.add(new Story(new Source("source_name"), "author",
                        "title", "desc", "url", "pub"));
            }
            return storyList;
        }

        public static void createFakeStoryLiveDataList(final List<Story> storyList) {
            liveData = getCurrentLiveData();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    liveData.setValue(storyList);
                }
            });
        }

        public static Boolean ifStoryIdentical(Story firstStory, Story secondStory) {
            return firstStory.equals(secondStory);
        }
    }
}