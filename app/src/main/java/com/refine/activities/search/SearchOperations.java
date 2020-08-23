package com.refine.activities.search;

import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.adapters.ActivityListAdapter;

public class SearchOperations extends CommonActivity {
    public static final String FULL_JOB_HISTORY_SEARCH = "任务单信息查询";
    public static final String OWNER_SUMMARY_SEARCH = "负责人汇总信息查询";

    private static final List<String> SEARCH_ACTIVITY_LIST =
            Lists.newArrayList(FULL_JOB_HISTORY_SEARCH, OWNER_SUMMARY_SEARCH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("信息查询");

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchOperations.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ActivityListAdapter activityListAdapter = new ActivityListAdapter(SearchOperations.this, SEARCH_ACTIVITY_LIST);
        recyclerView.setAdapter(activityListAdapter);
    }
}
