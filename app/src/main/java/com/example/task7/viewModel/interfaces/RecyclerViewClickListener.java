package com.example.task7.viewModel.interfaces;

import android.view.View;

import com.example.task7.data.Story;

public interface RecyclerViewClickListener {
    void recyclerViewListClicked(View sharedView, Story story, int position);
}
