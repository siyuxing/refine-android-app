package com.refine.activities.search;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.adapters.OwnerSummaryAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.OwnerSummary;
import com.refine.model.User;

public class OwnerSummaryDetailsActivity extends CommonActivity {
    private RecyclerView recyclerview;
    private EditText ownerET;
    private List<OwnerSummary> ownerSummaries = new ArrayList<>();
    private OwnerSummaryAdapter ownerSummaryAdapter;

    private String startDateString;
    private String endDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_summary_details);

        setTitle("负责人汇总信息详情");

        ownerET = findViewById(R.id.owner);

        recyclerview = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OwnerSummaryDetailsActivity.this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadSummary();
    }

    private void loadSummary() {
        // Ignore NetworkOnMainThreadException in this activity
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                startDateString = extras.getString(ActivityConstants.START_DATE_EXTRA);
                Date startDate = Date.valueOf(startDateString);
                endDateString = extras.getString(ActivityConstants.END_DATE_EXTRA);
                Date endDate = Date.valueOf(endDateString);
                String owner = extras.getString(ActivityConstants.OWNER_NAME_EXTRA);
                String product = extras.getString(ActivityConstants.PRODUCT_NAME_EXTRA);

                User user = DatabaseHelper.getUser(owner);
                ownerET.setText(user.getDisplayName());
                ownerSummaries = DatabaseHelper.searchOwnerSummary(startDate, endDate, owner, product);
            }

            if (ownerSummaries.isEmpty()) {
                normalPopUp("工作记录为空！");
            } else {
                ownerSummaryAdapter = new OwnerSummaryAdapter(OwnerSummaryDetailsActivity.this, ownerSummaries);
                recyclerview.setAdapter(ownerSummaryAdapter);
            }
        } catch (Exception e) {
            errorPopUp("获取工作历史记录失败！");
            finish();
        }
    }
}
