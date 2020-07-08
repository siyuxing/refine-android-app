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

public class AdminUserHome extends CommonActivity {
    public static final String ADMIN_OPTIONS = "管理员选项";
    public static final String PRODUCT_OWNER_OPTIONS = "项目负责人选项";
    public static final String SEARCH_OPTIONS = "信息查询";
    private static final List<String> ADMIN_ACTIVITY_LIST =
            Lists.newArrayList(ADMIN_OPTIONS, PRODUCT_OWNER_OPTIONS, SEARCH_OPTIONS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("管理员界面");

        RecyclerView RecyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminUserHome.this);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setItemAnimator(new DefaultItemAnimator());

        ActivityListAdapter activityListAdapter = new ActivityListAdapter(AdminUserHome.this, ADMIN_ACTIVITY_LIST);
        RecyclerView.setAdapter(activityListAdapter);
    }
}
