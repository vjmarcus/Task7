package com.example.task7.viewModel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.task7.data.Source;
import com.example.task7.data.Story;
import com.example.task7.db.StoryDatabase;
import com.example.task7.repository.StoryRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MainActivityViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    MutableLiveData<List<Story>> listLiveData;

    @Mock
    private Application application;
    @Mock
    private StoryRepository storyRepository;
    @Mock
    private Observer<List<Story>> observer;
    @Mock
    LifecycleOwner lifecycleOwner;
    @Mock
    StoryDatabase storyDatabase;

    private MainActivityViewModel viewModel;
    private Lifecycle lifecycle;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        lifecycle = new LifecycleRegistry(lifecycleOwner);
        viewModel = new MainActivityViewModel(application);
        viewModel.getAllStoryData(null).observeForever(observer);
        when(storyRepository.getLiveDataFromDb()).thenReturn(getFakeLiveData());

    }

    @Test
    public void testNull() {
        assertNotNull(viewModel.getAllStoryData(null));
//        assertTrue(viewModel.getAllStoryData().hasObservers());
    }

    public LiveData<List<Story>> getFakeLiveData() {
        final List<Story> storyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            storyList.add(new Story(new Source("source_name"), "author",
                    "title", "desc", "url", "pub"));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                listLiveData.setValue(storyList);
            }
        });
        return listLiveData;
    }

    public LiveData<List<Story>> getCurrentLiveData() {
        if (listLiveData == null) {
            listLiveData = new MutableLiveData<>();
        }
        return listLiveData;
    }

}