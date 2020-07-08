package com.refine.activities.admin;

import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.adapters.ActivityListAdapter;

public class AdminOperations extends CommonActivity {
    private final static List<String> ADMIN_ACTIVITY_LIST =
            Lists.newArrayList("添加用户", "删除用户", "添加产品", "删除产品");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("管理员选项");

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminOperations.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ActivityListAdapter activityListAdapter = new ActivityListAdapter(AdminOperations.this, ADMIN_ACTIVITY_LIST);
        recyclerView.setAdapter(activityListAdapter);
    }
}
