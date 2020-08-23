package com.refine.activities.product;

import java.sql.Date;
import java.util.Calendar;

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

@Deprecated
public class ProductOwnerJobHistorySearchActivity extends CommonActivity {
    private EditText startDateET;
    private EditText endDateET;

    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_history_search);

        setTitle("工作信息查询");

        startDateET = findViewById(R.id.start_date);
        endDateET = findViewById(R.id.end_date);

        startCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH) - 1);
        startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH) + 1);
        updateStartDate();
        updateEndDate();

        startDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, monthOfYear);
            startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateStartDate();
        };

        endDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, monthOfYear);
            endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndDate();
        };

        startDateET.setOnClickListener(v -> new DatePickerDialog(ProductOwnerJobHistorySearchActivity.this, startDateSetListener,
                                                                 startCalendar.get(Calendar.YEAR),
                                                                 startCalendar.get(Calendar.MONTH),
                                                                 startCalendar.get(Calendar.DAY_OF_MONTH)).show());

        endDateET.setOnClickListener(v -> new DatePickerDialog(ProductOwnerJobHistorySearchActivity.this, endDateSetListener,
                                                               endCalendar.get(Calendar.YEAR),
                                                               endCalendar.get(Calendar.MONTH),
                                                               endCalendar.get(Calendar.DAY_OF_MONTH)).show());
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
        startDateET.setText(getDateFormat().format(startCalendar.getTime()));
    }

    private void updateEndDate() {
        endDateET.setText(getDateFormat().format(endCalendar.getTime()));
    }

    private void resetInputs() {
        startDateET.setText("");
        endDateET.setText("");
    }
}