package com.refine.activities.product.check;

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
import com.refine.activities.CommonActivity;
import com.refine.activities.product.record.RecordWorkflowDetailsCommon;
import com.refine.adapters.WorkflowDetailsAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.Operation;
import com.refine.model.WorkflowDetails;

public class CheckPendingTasksCommon extends CommonActivity {
    private final Operation operation;
    private final Class<? extends RecordWorkflowDetailsCommon> clazz;
    private RecyclerView recyclerView;
    private WorkflowDetailsAdapter workflowDetailsAdapter;

    public CheckPendingTasksCommon(Operation operation,
                                   Class<? extends RecordWorkflowDetailsCommon> clazz) {
        this.operation = operation;
        this.clazz = clazz;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pending_tasks_common);

        setTitle(String.format("待处理%s工作", operation.name()));

        recyclerView = findViewById(R.id.item_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CheckPendingTasksCommon.this);
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
            List<WorkflowDetails> workflowDetails = DatabaseHelper.searchPendingWorkflowDetails(operation);

            if (workflowDetails.isEmpty()) {
                runInBackground(() -> {
                    normalPopUp(String.format("待处理%s任务为空！", operation.name()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    finish();
                });
            }

            workflowDetailsAdapter = new WorkflowDetailsAdapter(CheckPendingTasksCommon.this, workflowDetails);
            recyclerView.setAdapter(workflowDetailsAdapter);
        } catch (Exception e) {
            errorPopUp("获取工作失败！");
            finish();
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
        } else {
            Thread background = new Thread() {
                public void run() {
                    Intent intent = new Intent(CheckPendingTasksCommon.this, clazz);

                    intent.putExtra(ActivityConstants.WORKFLOW_DETAILS_EXTRA, workflowDetailsAdapter.getSelected());
                    startActivity(intent);
                }
            };

            // start thread
            background.start();
        }

    }
}
