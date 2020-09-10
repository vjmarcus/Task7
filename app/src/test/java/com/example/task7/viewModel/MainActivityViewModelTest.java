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
import com.example.task7.repository.StoryRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();


    @Mock
    private Application mockApplication;
    @Mock
    private StoryRepository mockStoryRepository;
    @Mock
    private Observer<List<Story>> mockObserver;
    @Mock
    LifecycleOwner mockLifecycleOwner;

    private final String TOPIC = "topic";

    private MutableLiveData<List<Story>> fakeListLiveData = new MutableLiveData<>();
    private MainActivityViewModel viewModel;
    private Lifecycle lifecycle;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        lifecycle = new LifecycleRegistry(mockLifecycleOwner);
        viewModel = new MainActivityViewModel(mockApplication);
        fakeListLiveData.observeForever(mockObserver);
    }

    @Test
    public void testLiveData() {
   assertNotNull((fakeListLiveData));
   fakeListLiveData = (MutableLiveData<List<Story>>) getFakeLiveData();
   assertEquals(getFakeLiveData().getValue(), fakeListLiveData.getValue());
    }

    public LiveData<List<Story>> getFakeLiveData() {
        final List<Story> storyList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            storyList.add(new Story(new Source("source_name"), "author",
                    "title", "desc", "url", "pub"));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                fakeListLiveData.setValue(storyList);
            }
        });
        return fakeListLiveData;
    }

    public LiveData<List<Story>> getCurrentLiveData() {
        if (fakeListLiveData == null) {
            fakeListLiveData = new MutableLiveData<>();
        }
        return fakeListLiveData;
    }

}