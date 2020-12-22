package com.refine.activities.product.check;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.activities.CommonActivity;
import com.refine.activities.search.WorkflowSheetDetailsActivity;
import com.refine.adapters.WorkflowDetailsAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.Operation;
import com.refine.model.WorkflowDetails;
import com.refine.model.WorkflowSheet;

public class CheckAllPendingTasksActivity extends CommonActivity {
    private static final int MAX_RECORD_IN_SEARCH = 10;
    private RecyclerView recyclerView;
    private WorkflowDetailsAdapter workflowDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pending_tasks_common);

        setTitle("任务详情");

        recyclerView = findViewById(R.id.item_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CheckAllPendingTasksActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Ignore NetworkOnMainThreadException in this activity
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            List<WorkflowDetails> workflowDetails = DatabaseHelper.searchAllPendingWorkflowDetails(
                    AccountProfileLocator.getProfile().getAllowedOperations());

            int remainNum = MAX_RECORD_IN_SEARCH - workflowDetails.size();
            if (remainNum > 0) {
                Calendar lookBackDate = Calendar.getInstance();
                lookBackDate.set(Calendar.MONTH, lookBackDate.get(Calendar.MONTH) - 1);
                lookBackDate.set(Calendar.DAY_OF_MONTH, lookBackDate.get(Calendar.DAY_OF_MONTH) + 1);

                workflowDetails.addAll(DatabaseHelper.searchRecentFinishedWorkflowDetails(
                        AccountProfileLocator.getProfile().getCurrentUser(), new Date(lookBackDate.getTimeInMillis()), remainNum));
            }

            if (workflowDetails.isEmpty()) {
                runInBackground(() -> {
                    normalPopUp("待处理任务为空！");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    finish();
                });
            }

            workflowDetailsAdapter = new WorkflowDetailsAdapter(CheckAllPendingTasksActivity.this, workflowDetails);
            recyclerView.setAdapter(workflowDetailsAdapter);
        } catch (Exception e) {
            errorPopUp("获取工作失败！");
            finish();
        }
    }

    public void tryCheckDetails(View v) {
        Button button = findViewById(R.id.check_details);
        button.setEnabled(false);
        try {
            checkDetails(v);
        } finally {
            button.setEnabled(true);
        }
    }

    private void checkDetails(View v) {
        if (workflowDetailsAdapter.getSelected() == null) {
            errorPopUp("请选择工作单");
        } else {
            Thread background = new Thread() {
                public void run() {
                    try {
                        Intent intent = new Intent(CheckAllPendingTasksActivity.this, WorkflowSheetDetailsActivity.class);

                        WorkflowSheet workflowSheet = DatabaseHelper.getWorkflowSheet(workflowDetailsAdapter.getSelected().getWorkflowId());

                        if (workflowSheet != null) {
                            intent.putExtra(ActivityConstants.WORKFLOW_SHEET_EXTRA, workflowSheet);
                            startActivity(intent);
                        } else {
                            errorPopUp("获取工作单失败！");
                        }
                    } catch (Exception e) {
                        errorPopUp("获取工作单失败！");
                        finish();
                    }
                }
            };

            // start thread
            background.start();
        }
    }

    public void tryRecord(View v) {
        Button button = findViewById(R.id.record);
        button.setEnabled(false);
        try {
            record(v);
        } finally {
            button.setEnabled(true);
        }
    }

    private void record(View v) {
        if (workflowDetailsAdapter.getSelected() == null) {
            errorPopUp("请选择任务");
        } else if (Boolean.TRUE.equals(workflowDetailsAdapter.getSelected().isFinish())) {
            errorPopUp("此任务已完成");
        }  else {
            Thread background = new Thread() {
                public void run() {
                    Operation selectedOperation = workflowDetailsAdapter.getSelected().getOperation();
                    Class<?> clazz = ActivityConstants.RECORD_CLASS_MAP.get(selectedOperation);
                    Intent intent = new Intent(CheckAllPendingTasksActivity.this, clazz);

                    intent.putExtra(ActivityConstants.WORKFLOW_DETAILS_EXTRA, workflowDetailsAdapter.getSelected());
                    startActivity(intent);
                }
            };

            // start thread
            background.start();
        }

    }
}
