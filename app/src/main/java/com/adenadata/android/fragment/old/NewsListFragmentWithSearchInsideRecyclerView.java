package com.adenadata.android.fragment.old;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adenadata.android.R;
import com.adenadata.android.adapter.old.NewsAdapterWithSearch;
import com.adenadata.android.model.New;
import com.adenadata.android.util.SearchListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragmentWithSearchInsideRecyclerView extends Fragment implements SearchListener {

    private static final String TAG = "NewsListFragmentSearch";

    private ArrayList<New> mNews = new ArrayList<New>();
    private NewsAdapterWithSearch mAdapter;
    private RecyclerView mRecyclerView;
//    private ProgressWheel mProgressWheel;
//    private TextView mEmptyListTextView;


    public NewsListFragmentWithSearchInsideRecyclerView() {
        // Required empty public constructor
    }


    // HEADER SEARCH BUTTON
    // ********************
    @Override
    public void performSearch(String searchText) {
        query(searchText);
    }

    @Override
    public void undoSearch() {
        query("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_list_with_search_inside_recyclerview, container, false);

//        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.news_list_RecyclerView);
//        mProgressWheel = (ProgressWheel) v.findViewById(R.id.news_list_ProgressWheel);
//        mEmptyListTextView = (TextView) v.findViewById(R.id.news_list_empty_TextView);

        // Improve performance - https://developer.android.com/training/material/lists-cards.html
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new NewsAdapterWithSearch(mNews, this);
        mRecyclerView.setAdapter(mAdapter);

        query("");

        return v;
    }


    private void query(String searchText) {
        Log.i(TAG, "NewsListFragmentWithSearchInsideRecyclerView - query()");
        ParseQuery<New> query = ParseQuery.getQuery(New.class);
        if (searchText.trim().length() > 0) {
            query.whereContains("searchedTitle", searchText);
        } else {
            query.orderByDescending("createdAt");
            // TODO key greater than yesterday
        }
        query.findInBackground(new FindCallback<New>() {
            @Override
            public void done(List<New> news, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Success query news: " + news.size() + " elements");
                    if (news.size() == 0) {
                        // Show empty list TextView
//                        mRecyclerView.setVisibility(View.GONE);
//                        mProgressWheel.setVisibility(View.GONE);
//                        mEmptyListTextView.setVisibility(View.VISIBLE);
                        mNews.clear();
                        mNews.addAll(news);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        // Hide ProgressWheel and empty list TextView
//                        mProgressWheel.setVisibility(View.GONE);
//                        mEmptyListTextView.setVisibility(View.GONE);
//                        mRecyclerView.setVisibility(View.VISIBLE);
                        // Add stuff
                        mNews.clear();
                        mNews.addAll(news);
                        mAdapter.notifyDataSetChanged();
                    }
                }
//                else {
//                    Log.i(TAG, "Error query news: " + e.getMessage());
//                    // Show empty list TextView with text 'Network error'
//                    mEmptyListTextView.setText("Network Error");
//                    mRecyclerView.setVisibility(View.GONE);
//                    mProgressWheel.setVisibility(View.GONE);
//                    mEmptyListLayout.setVisibility(View.VISIBLE);
//                }
            }
        });
    }


    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_new:
                Intent i = new Intent(getActivity(), AddNewActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
