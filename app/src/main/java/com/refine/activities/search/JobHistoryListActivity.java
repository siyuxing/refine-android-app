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
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.adapters.JobHistoryAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.JobHistory;

public class JobHistoryListActivity extends CommonActivity {
    private RecyclerView recyclerview;
    private JobHistoryAdapter jobHistoryAdapter;
    private List<JobHistory> jobHistories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        setTitle("工作历史记录");

        recyclerview = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(JobHistoryListActivity.this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onStart() {
        super.onStart();

        loadJobHistory();
    }

    public void delete(View v) {
        if (jobHistoryAdapter.getSelected() == null) {
            errorPopUp("请选择工作历史");
        } else {
            Thread background = new Thread() {
                public void run() {
                    try {
                        JobHistory jobHistory = jobHistoryAdapter.getSelected();

                        if (jobHistory.getOperation().getToStatus() != null) {
                            DatabaseHelper.mutateProductCountInStock(jobHistory.getProductName(),
                                                                     jobHistory.getOperation().getToStatus().getStatusCode(),
                                                                     -jobHistory.getNumOfSuccess());
                        }

                        if (jobHistory.getOperation().getFromStatus() != null) {
                            DatabaseHelper.mutateProductCountInStock(jobHistory.getProductName(),
                                                                     jobHistory.getOperation().getFromStatus().getStatusCode(),
                                                                     jobHistory.getNumOfSuccess() + jobHistory.getNumOfFailure());
                        }

                        DatabaseHelper.deleteJobHistory(jobHistory.getId());

                        successPopUp("历史记录删除成功！");

                        sleep(1000);

                        loadJobHistory();
                    } catch (Exception e) {
                        errorPopUp("历史记录删除失败！");
                    }
                }
            };

            // start thread
            background.start();
        }
    }

    public void checkDetails(View v) {
        if (jobHistoryAdapter.getSelected() == null) {
            errorPopUp("请选择工作历史");
        } else {
            Thread background = new Thread() {
                public void run() {
                    Intent intent = new Intent(JobHistoryListActivity.this, JobHistoryDetailsActivity.class);
                    intent.putExtra(ActivityConstants.JOB_HISTORY_ID_EXTRA, jobHistoryAdapter.getSelected().getId());

                    startActivity(intent);
                }
            };

            // start thread
            background.start();
        }
    }

    private void loadJobHistory() {
        // Ignore NetworkOnMainThreadException in this activity
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                Date startDate = Date.valueOf(extras.getString(ActivityConstants.START_DATE_EXTRA));
                Date endDate = Date.valueOf(extras.getString(ActivityConstants.END_DATE_EXTRA));
                String owner = extras.getString(ActivityConstants.OWNER_NAME_EXTRA);
                String product = extras.getString(ActivityConstants.PRODUCT_NAME_EXTRA);
                String operation = extras.getString(ActivityConstants.OPERATION_EXTRA);
                jobHistories = DatabaseHelper.searchJobHistory(startDate, endDate, owner, product, operation);
            }

            if (jobHistories.isEmpty()) {
                normalPopUp("工作记录为空！");
            }

            jobHistoryAdapter = new JobHistoryAdapter(JobHistoryListActivity.this, jobHistories);
            recyclerview.setAdapter(jobHistoryAdapter);
        } catch (Exception e) {
            errorPopUp("获取工作历史记录失败！");
            finish();
        }
    }
}
