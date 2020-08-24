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
import com.refine.adapters.WorkflowSheetAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.WorkflowSheet;

public class WorkflowSheetListActivity extends CommonActivity {
    private RecyclerView recyclerview;
    private WorkflowSheetAdapter workflowSheetAdapter;
    private List<WorkflowSheet> workflowSheets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_sheet_list);

        setTitle("任务单列表");

        recyclerview = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WorkflowSheetListActivity.this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onStart() {
        super.onStart();

        loadWorkflowSheet();
    }

    public void checkDetails(View v) {
        if (workflowSheetAdapter.getSelected() == null) {
            errorPopUp("请选择工作单");
        } else {
            Thread background = new Thread() {
                public void run() {
                    Intent intent = new Intent(WorkflowSheetListActivity.this, WorkflowSheetDetailsActivity.class);
                    intent.putExtra(ActivityConstants.WORKFLOW_SHEET_EXTRA, workflowSheetAdapter.getSelected());

                    startActivity(intent);
                }
            };

            // start thread
            background.start();
        }
    }

    private void loadWorkflowSheet() {
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
                workflowSheets = DatabaseHelper.searchWorkflowSheets(startDate, endDate, owner, product);
            }

            if (workflowSheets.isEmpty()) {
                normalPopUp("工作单为空！");
            }

            workflowSheetAdapter = new WorkflowSheetAdapter(WorkflowSheetListActivity.this, workflowSheets);
            recyclerview.setAdapter(workflowSheetAdapter);
        } catch (Exception e) {
            runInBackground(() -> {
                errorPopUp("获取工作单记录失败！");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

                finish();
            });
        }
    }
}
