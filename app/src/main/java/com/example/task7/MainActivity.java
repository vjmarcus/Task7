package com.example.task7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.task7.adapter.StoryAdapter;
import com.example.task7.data.Story;
import com.example.task7.interfaces.RecyclerViewClickListener;
import com.example.task7.viewModel.MainActivityViewModel;

import java.util.List;

//Вызывает методы ВьюМодел, данные получает через связывание, изменения во ВьюМодел отражаюстся во Вью
public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    private static final String TAG = "MyApp";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerViewClickListener recyclerViewClickListener;
    private RecyclerView recyclerView;
    private String currentTopic;
    private MainActivityViewModel viewModel;
    private List<Story> storyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initRecyclerViewClickListener();
        initSwipeRefreshLayout();
        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(MainActivityViewModel.class);
        viewModel.initRepository(getApplication());
    }

    private void init() {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<?> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.topics,
                android.R.layout.simple_list_item_1);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        recyclerView = findViewById(R.id.story_recycler);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "Main onItemSelected: " + adapterView.getSelectedItem().toString());
        if (currentTopic != adapterView.getSelectedItem().toString()) {
            viewModel.clearDb();
        }
        currentTopic = adapterView.getSelectedItem().toString();
        getDataFromViewModel();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //DO NOTHING
    }

    public void showStories() {
        Log.d(TAG, "showStories: create adapter " + storyList.size());
        StoryAdapter storyAdapter = new StoryAdapter(storyList, recyclerViewClickListener);
        recyclerView.setAdapter(storyAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getDataFromViewModel(){
        swipeRefreshLayout.setRefreshing(true);
        Log.d(TAG, "getDataFromViewModel: current topic = " + currentTopic);
        viewModel.setCurrentRequestParam(currentTopic);
        // Вынести в онКреа
        viewModel.getAllStoryData(currentTopic).observe(this, new Observer<List<Story>>() {
            @Override
            public void onChanged(List<Story> stories) {
                if (stories != null)
                Log.d(TAG, "onChanged: " + stories.size());
                storyList = stories;
                //Update recyclerView
                showStories();
            }
        });
    }

    private void viewModelSubscribe() {
        viewModel.getAllStoryData(currentTopic).observe(this, new Observer<List<Story>>() {
            @Override
            public void onChanged(List<Story> stories) {
                if (stories != null)
                    Log.d(TAG, "onChanged: " + stories.size());
                storyList = stories;
                //Update recyclerView
                showStories();
            }
        });
    }

    private void initRecyclerViewClickListener() {
        recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View sharedView, Story story, int position) {
                Log.d(TAG, "recyclerViewListClicked: ");
                goToSecondActivity(sharedView, story, position);
            }
        };
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromViewModel();
                Log.d(TAG, "onRefresh: swipe");
            }
        });
    }

    private void goToSecondActivity(View sharedView, Story story, int position) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("obj", story);
        ActivityOptionsCompat compat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(MainActivity.this, sharedView, "transition");
        startActivity(intent, compat.toBundle());
    }
}