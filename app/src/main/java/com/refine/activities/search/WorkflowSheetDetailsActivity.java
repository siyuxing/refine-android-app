package com.refine.activities.search;

import java.util.List;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.adapters.WorkflowDetailsDisplayAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.User;
import com.refine.model.WorkflowDetails;
import com.refine.model.WorkflowSheet;

public class WorkflowSheetDetailsActivity extends CommonActivity {
    private RecyclerView recyclerView;
    private WorkflowSheet workflowSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_sheet_details);

        setTitle("工作单详情");

        recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WorkflowSheetDetailsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            workflowSheet = (WorkflowSheet) extras.get(ActivityConstants.WORKFLOW_SHEET_EXTRA);
        } else {
            runInBackground(() -> {
                errorPopUp("任务单信息为空！");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finish();
            });
        }

        // Ignore NetworkOnMainThreadException in this activity
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            loadWorkflowSheet();
            loadWorkflowDetails();
        } catch (Exception e) {
            runInBackground(() -> {
                errorPopUp("获取任务单详情失败！");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

                finish();
            });
        }
    }

    private void loadWorkflowSheet() throws Exception {
        setTitle(workflowSheet.getSheetId());

        EditText productET = findViewById(R.id.product);
        productET.setText(workflowSheet.getProductName());
        EditText requesterET = findViewById(R.id.requester);
        String requesterName = ActivityConstants.UNKNOWN_FIELD_VALUE;
        if (workflowSheet.getRequester() != null) {
            User user = DatabaseHelper.getUser(workflowSheet.getRequester());
            requesterName = user.getDisplayName();
        }
        requesterET.setText(requesterName);
        EditText startDateET = findViewById(R.id.start_date);
        startDateET.setText(workflowSheet.getStartDate() != null ? getDateFormat().format(workflowSheet.getStartDate()) : ActivityConstants.UNKNOWN_FIELD_VALUE);
        EditText finishDateET = findViewById(R.id.finish_date);
        finishDateET.setText(workflowSheet.getFinishDate() != null ? getDateFormat().format(workflowSheet.getFinishDate()) : ActivityConstants.UNFINISHED_FIELD_VALUE);
        EditText materialCountET = findViewById(R.id.material_count);
        materialCountET.setText(workflowSheet.getNumOfMaterial() != null ? String.valueOf(workflowSheet.getNumOfMaterial()) : ActivityConstants.UNKNOWN_FIELD_VALUE);
        EditText expectedCountET = findViewById(R.id.expected_count);
        expectedCountET.setText(workflowSheet.getNumOfRequested() != null ? String.valueOf(workflowSheet.getNumOfRequested()) : ActivityConstants.UNKNOWN_FIELD_VALUE);
        EditText finishCountET = findViewById(R.id.finish_count);
        finishCountET.setText(workflowSheet.getNumOfFinal() != null ? String.valueOf(workflowSheet.getNumOfFinal()) : ActivityConstants.UNFINISHED_FIELD_VALUE);
    }

    private void loadWorkflowDetails() throws Exception {
        List<WorkflowDetails> workflowDetails = DatabaseHelper.getWorkflowDetailsBySheetId(workflowSheet.getSheetId());

        WorkflowDetailsDisplayAdapter workflowDetailsAdapter = new WorkflowDetailsDisplayAdapter(WorkflowSheetDetailsActivity.this, workflowDetails);
        recyclerView.setAdapter(workflowDetailsAdapter);
    }
}