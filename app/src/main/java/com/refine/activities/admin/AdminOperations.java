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
    public static final String ADD_USER = "添加用户";
    public static final String CHECK_USERS = "查看用户";
    public static final String ADD_PRODUCT = "添加产品";
    public static final String CHECK_PRODUCT = "查看产品";

    private final static List<String> ADMIN_ACTIVITY_LIST =
            Lists.newArrayList(ADD_USER, CHECK_USERS, ADD_PRODUCT, CHECK_PRODUCT);

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
