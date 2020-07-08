package com.refine.activities.product;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.activities.CommonActivity;
import com.refine.activities.search.JobHistoryListActivity;
import com.refine.model.ActivityConstants;

public class ProductOwnerJobHistorySearchActivity extends CommonActivity {
    private EditText startDateET;
    private EditText endDateET;

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_history_search);

        setTitle("工作信息查询");

        startDateET = findViewById(R.id.start_date);
        endDateET = findViewById(R.id.end_date);

        startDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateStartDate();
        };

        endDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndDate();
        };

        startDateET.setOnClickListener(v -> new DatePickerDialog(ProductOwnerJobHistorySearchActivity.this, startDateSetListener,
                                                                 myCalendar.get(Calendar.YEAR),
                                                                 myCalendar.get(Calendar.MONTH),
                                                                 myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        endDateET.setOnClickListener(v -> new DatePickerDialog(ProductOwnerJobHistorySearchActivity.this, endDateSetListener,
                                                               myCalendar.get(Calendar.YEAR),
                                                               myCalendar.get(Calendar.MONTH),
                                                               myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    public void search(View v) {
        final String startDateString = startDateET.getText().toString();
        final String endDateString = endDateET.getText().toString();
        final Date startDate, endDate;
        try {
            startDate = Date.valueOf(startDateString);
            endDate = Date.valueOf(endDateString);
        } catch (Exception e) {
            errorPopUp("开始或结束日期无效！");
            return;
        }

        if (startDate.after(endDate)) {
            errorPopUp("日期信息无效！(开始日期 > 结束日期)");
            return;
        }

        Thread background = new Thread() {
            public void run() {
                Intent intent = new Intent(ProductOwnerJobHistorySearchActivity.this, JobHistoryListActivity.class);
                intent.putExtra(ActivityConstants.START_DATE_EXTRA, startDateString);
                intent.putExtra(ActivityConstants.END_DATE_EXTRA, endDateString);
                intent.putExtra(ActivityConstants.OWNER_NAME_EXTRA, AccountProfileLocator.getProfile().getCurrentUser());

                startActivity(intent);
            }
        };

        // start thread
        background.start();
        resetInputs();
    }

    private void updateStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
        startDateET.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
        endDateET.setText(sdf.format(myCalendar.getTime()));
    }

    private void resetInputs() {
        startDateET.setText("");
        endDateET.setText("");
    }
}