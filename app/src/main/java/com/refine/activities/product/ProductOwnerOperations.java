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
import com.refine.model.Operation;

public class ProductOwnerOperations extends CommonActivity {
    public static final String PRODUCT_STOCK_SEARCH = "查看库存信息";
    public static final String WORK_DETAILS_CHECK = "查看任务详情";

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

        List<String> operations = Lists.newArrayList(PRODUCT_STOCK_SEARCH);
        List<String> permissions = AccountProfileLocator.getProfile().getAllowedOperations();
        if (permissions.contains(Operation.创建新任务.name())) {
            operations.add(Operation.创建新任务.name());
        }
        operations.add(WORK_DETAILS_CHECK);
        if (permissions.contains(Operation.出库.name())) {
            operations.add(Operation.出库.name());
        }
        activityListAdapter = new ActivityListAdapter(ProductOwnerOperations.this, operations);
        recyclerview.setAdapter(activityListAdapter);
    }
}
