package com.refine.activities.product;

import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.activities.CommonActivity;
import com.refine.adapters.ActivityListAdapter;

public class ProductOwnerOperations extends CommonActivity {
    public static final String JOB_HISTORY = "工作记录查询";
    public static final String PRODUCT_STOCK_SEARCH = "库存信息查询";
    private RecyclerView recyclerview;
    private ActivityListAdapter activityListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("项目负责人选项");

        recyclerview = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductOwnerOperations.this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        List<String> operations = Lists.newArrayList(JOB_HISTORY, PRODUCT_STOCK_SEARCH);
        operations.addAll(AccountProfileLocator.getProfile().getAllowedOperations());
        activityListAdapter = new ActivityListAdapter(ProductOwnerOperations.this, operations);
        recyclerview.setAdapter(activityListAdapter);
    }
}
