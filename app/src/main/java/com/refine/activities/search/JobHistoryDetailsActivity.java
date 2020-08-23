package com.refine.activities.search;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.JobHistory;

@Deprecated
public class JobHistoryDetailsActivity extends CommonActivity {
    private EditText dateET;
    private EditText successCountET;
    private EditText failCountET;
    private EditText noteET;

    private JobHistory jobHistory;

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        setTitle("修改工作历史记录");

        Bundle extras = getIntent().getExtras();

        if (extras == null || extras.getLong(ActivityConstants.JOB_HISTORY_ID_EXTRA) <= 0) {
            failActivityOnError();
        } else {
            // Ignore NetworkOnMainThreadException in this activity
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Long jobHistoryId = extras.getLong(ActivityConstants.JOB_HISTORY_ID_EXTRA);

                jobHistory = DatabaseHelper.getJobHistory(jobHistoryId);

                if (jobHistory == null) {
                    failActivityOnError();
                } else {
                    ((EditText) findViewById(R.id.owner)).setText(jobHistory.getOwner());
                    ((EditText) findViewById(R.id.product)).setText(jobHistory.getProductName());
                    ((EditText) findViewById(R.id.operation)).setText(jobHistory.getOperation().name());

                    dateET = findViewById(R.id.history_date);
                    dateET.setText(getDateFormat().format(jobHistory.getDate()));
                    successCountET = findViewById(R.id.success_count);
                    if (jobHistory.getNumOfSuccess() != null) {
                        successCountET.setText(String.valueOf(jobHistory.getNumOfSuccess()));
                    }
                    failCountET = findViewById(R.id.fail_count);
                    if (jobHistory.getNumOfFailure() != null) {
                        failCountET.setText(String.valueOf(jobHistory.getNumOfFailure()));
                    }
                    noteET = findViewById(R.id.note);
                    noteET.setText(jobHistory.getAdditionNote());

                    dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDate();
                    };

                    dateET.setOnClickListener(v -> new DatePickerDialog(JobHistoryDetailsActivity.this, dateSetListener,
                                                                        myCalendar.get(Calendar.YEAR),
                                                                        myCalendar.get(Calendar.MONTH),
                                                                        myCalendar.get(Calendar.DAY_OF_MONTH)).show());
                }
            } catch (Exception e) {
                failActivityOnError();
            }
        }
    }

    public void modify(View v) {
        final String note = noteET.getText().toString();
        final Date date;
        final int successCount, failCount;
        try {
            successCount = Integer.parseInt(successCountET.getText().toString());
            failCount = Integer.parseInt(failCountET.getText().toString());
            date = Date.valueOf(dateET.getText().toString());
        } catch (Exception e) {
            errorPopUp("产品数量信息或日期无效！");
            return;
        }

        Thread background = new Thread() {
            public void run() {
                try {
                    Long productId = DatabaseHelper.getProductId(jobHistory.getProductName());
                    int newTotal = successCount + failCount;
                    int oldTotal = jobHistory.getNumOfSuccess() + jobHistory.getNumOfFailure();
                    if (jobHistory.getOperation().getFromStatus() != null) {
                        DatabaseHelper.mutateProductCountInStock(productId,
                                                                 jobHistory.getOperation().getFromStatus().getStatusCode(),
                                                                 oldTotal - newTotal);
                    }

                    if (jobHistory.getOperation().getToStatus() != null) {
                        DatabaseHelper.mutateProductCountInStock(productId,
                                                                 jobHistory.getOperation().getToStatus().getStatusCode(),
                                                                 successCount - jobHistory.getNumOfSuccess());
                    }

                    DatabaseHelper.updateJobHistory(jobHistory.getId(), date, note, successCount, failCount);

                    successPopUp("更新记录成功！");

                    sleep(1000);

                    finish();
                } catch (Exception e) {
                    errorPopUp("更新记录失败！");
                }
            }
        };

        // start thread
        background.start();
    }

    private void updateDate() {
        dateET.setText(getDateFormat().format(myCalendar.getTime()));
    }

    private void failActivityOnError() {
        errorPopUp("获取工作历史记录信息失败！");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignore exception
        }
        finish();
    }

}
