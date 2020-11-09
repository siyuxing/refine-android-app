package com.refine.activities.product.record;

import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.mysql.jdbc.StringUtils;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.database.InsuffcientProductException;
import com.refine.model.ActivityConstants;
import com.refine.model.Operation;
import com.refine.model.WorkflowDetails;

public class RecordWorkflowDetailsCommon extends CommonActivity {
    private EditText finishDateET;
    private EditText successCountET;
    private EditText failCountET;
    private EditText noteET;

    private WorkflowDetails workflowDetails;

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private final Operation operation;
    private final Operation nextOperation;

    public RecordWorkflowDetailsCommon(Operation operation, Operation nextOperation) {
        this.operation = operation;
        this.nextOperation = nextOperation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_workflow_common);

        setTitle(String.format("%s任务详情", operation.name()));

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            workflowDetails = (WorkflowDetails) extras.get(ActivityConstants.WORKFLOW_DETAILS_EXTRA);
        } else {
            runInBackground(() -> {
                errorPopUp("任务信息为空！");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finish();
            });
        }

        EditText sheetIdET = findViewById(R.id.sheet_id);
        EditText productET = findViewById(R.id.product);
        EditText createDateET = findViewById(R.id.create_date);
        EditText productCountET = findViewById(R.id.total_count);
        finishDateET = findViewById(R.id.finish_date);
        successCountET = findViewById(R.id.success_count);
        failCountET = findViewById(R.id.fail_count);
        noteET = findViewById(R.id.note);

        updateDate();
        dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        };

        finishDateET.setOnClickListener(v -> new DatePickerDialog(RecordWorkflowDetailsCommon.this, dateSetListener,
                                                            myCalendar.get(Calendar.YEAR),
                                                            myCalendar.get(Calendar.MONTH),
                                                            myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        sheetIdET.setText(workflowDetails.getWorkflowId());
        productET.setText(workflowDetails.getProductName());
        createDateET.setText(getDateFormat().format(workflowDetails.getStartDate()));
        productCountET.setText(String.valueOf(workflowDetails.getNumOfTotal()));

        if (workflowDetails.getFinishDate() != null) {
            finishDateET.setText(getDateFormat().format(workflowDetails.getFinishDate()));
        }
        if (workflowDetails.getNumOfSuccess() != null) {
            successCountET.setText(String.valueOf(workflowDetails.getNumOfSuccess()));
        }
        if (workflowDetails.getNumOfFailure() != null) {
            failCountET.setText(String.valueOf(workflowDetails.getNumOfFailure()));
        }
        noteET.setText(workflowDetails.getAdditionNote());
    }

    public void trySave(View v) {
        Button button = findViewById(R.id.save);
        button.setEnabled(false);
        try {
            save(v);
        } finally {
            button.setEnabled(true);
        }
    }

    private void save(View v) {
        final String note = noteET.getText().toString();

        final Date finishDate;
        final Integer successCount, failCount;
        try {
            successCount = StringUtils.isNullOrEmpty(successCountET.getText().toString()) ? null : Integer.parseInt(successCountET.getText().toString());
            failCount = StringUtils.isNullOrEmpty(failCountET.getText().toString()) ? null : Integer.parseInt(failCountET.getText().toString());
            finishDate = StringUtils.isNullOrEmpty(finishDateET.getText().toString()) ? null : Date.valueOf(finishDateET.getText().toString());
        } catch (Exception e) {
            errorPopUp("产品数量信息或完成日期无效！");
            return;
        }

        Thread background = new Thread() {
            public void run() {
                try {
                    DatabaseHelper.updateWorkflowDetail(workflowDetails.getId(), AccountProfileLocator.getProfile().getCurrentUser(), finishDate,
                                                        successCount, failCount, note);

                    successPopUp("保存工作记录成功！");

                    sleep(1000);
                    finish();
                } catch (InsuffcientProductException e) {
                    errorPopUp(e.getMessage());
                } catch (Exception e) {
                    errorPopUp("保存工作记录失败！");
                }
            }
        };

        // start thread
        background.start();
    }

    public void tryAddHistory(View v) {
        Button button = findViewById(R.id.submit);
        button.setEnabled(false);
        try {
            addHistory(v);
        } finally {
            button.setEnabled(true);
        }
    }

    private void addHistory(View v) {
        final String note = noteET.getText().toString();
        final String productName = workflowDetails.getProductName();

        final Date finishDate;
        final int successCount, failCount;
        try {
            successCount = Integer.parseInt(successCountET.getText().toString());
            failCount = Integer.parseInt(failCountET.getText().toString());
            finishDate = Date.valueOf(finishDateET.getText().toString());
        } catch (Exception e) {
            errorPopUp("产品数量信息或完成日期无效！");
            return;
        }

        if (successCount + failCount != workflowDetails.getNumOfTotal()) {
            errorPopUp("合格数量 + 报废数量 != 代加工产品总数");
            return;
        }

        Thread background = new Thread() {
            public void run() {
                try {
                    Long productId = DatabaseHelper.getProductId(productName);
                    Connection conn = DatabaseHelper.startTransaction();
                    if (operation.getFromStatus() != null) {
                        DatabaseHelper.mutateProductCountInStock(productId, operation.getFromStatus().getStatusCode(), -(successCount + failCount), conn);
                    }
                    if (operation.getToStatus() != null) {
                        DatabaseHelper.mutateProductCountInStock(productId, operation.getToStatus().getStatusCode(), successCount, conn);
                    }
                    DatabaseHelper.confirmWorkflowDetail(workflowDetails.getId(), AccountProfileLocator.getProfile().getCurrentUser(), finishDate,
                                                         successCount, failCount, note, conn);

                    if (nextOperation != null) {
                        DatabaseHelper.createWorkflowDetails(workflowDetails.getProductId(), workflowDetails.getWorkflowId(), finishDate, successCount, nextOperation, conn);
                    } else {
                        DatabaseHelper.completeWorkflowSheet(workflowDetails.getWorkflowId(), finishDate, successCount, conn);
                    }

                    DatabaseHelper.commitTransaction(conn);

                    successPopUp("提交工作记录成功！");

                    sleep(1000);

                    finish();
                } catch (InsuffcientProductException e) {
                    errorPopUp(e.getMessage());
                } catch (Exception e) {
                    errorPopUp("提交工作记录失败！");
                }
            }
        };

        // start thread
        background.start();
    }

    private void updateDate() {
        finishDateET.setText(getDateFormat().format(myCalendar.getTime()));
    }

}
