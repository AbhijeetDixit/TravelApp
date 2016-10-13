package com.starsoft.traveldiary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starsoft.traveldiary.R;
import com.starsoft.traveldiary.adapters.CustomListAdapter;
import com.starsoft.traveldiary.adapters.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aashish on 9/9/2016.
 */
public class FragOne extends Fragment implements LoadMoreAdapter.OnLoadMoreListener {

    private CustomListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static FragOne getNewInstance(String title){
        FragOne fragOne = new FragOne();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragOne.setArguments(args);
        return fragOne;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_one, container, false);
        //view.setLayoutParams(new La);
        //TextView tv = (TextView)view.findViewById(R.id.pageTitle);
        //tv.setText("Page " + getArguments().getString("title"));
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeView);
        swipeRefreshLayout.setRefreshing(true);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.fragment_demo_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void initList(View view, Bundle savedInstanceState) {

        mAdapter = new CustomListAdapter(mRecyclerView,getContext());
        mAdapter.setOnLoadMoreListener(this);

        if (savedInstanceState == null) {
           mRecyclerView.postDelayed(new Runnable() {
               @Override
               public void run() {
                   mAdapter.addData(getData(0));
                   swipeRefreshLayout.setRefreshing(false);
               }
           },1500);
        } else {
            mAdapter.restoreState(savedInstanceState);
            if (mAdapter.isLoading()) {
                onLoadMore(mAdapter.getItemCount() - 1);
            }
        }

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onLoadMore(mAdapter.getItemCount() -1);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.saveState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
    }

    private List<String> getData(int offset) {
        List<String> data = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            data.add((offset + i) + "");
        }

        return data;
    }

    @Override
    public void onLoadMore(final int offset) {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAdapter != null) {
                    // If loading fails, call setLoading(false) to cancel loading more
                    // TODO add error view to retry
                    mAdapter.addData(getData(offset));
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 1500);
    }
}
