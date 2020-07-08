package com.refine.activities.search;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.common.collect.Lists;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;

public class AdminJobHistorySearchActivity extends CommonActivity {
    private static final String PLACE_HOLDER_OPTION = "全部";
    private EditText startDateET;
    private EditText endDateET;
    private Spinner ownerSpinner;
    private Spinner productSpinner;

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    private List<String> allUsers = Lists.newArrayList(PLACE_HOLDER_OPTION);
    private List<String> allProducts = Lists.newArrayList(PLACE_HOLDER_OPTION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_history_search);

        setTitle("工作信息查询");

        startDateET = findViewById(R.id.start_date);
        endDateET = findViewById(R.id.end_date);

        ownerSpinner = findViewById(R.id.owner);
        productSpinner = findViewById(R.id.product);

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

        startDateET.setOnClickListener(v -> new DatePickerDialog(AdminJobHistorySearchActivity.this, startDateSetListener,
                                                                 myCalendar.get(Calendar.YEAR),
                                                                 myCalendar.get(Calendar.MONTH),
                                                                 myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        endDateET.setOnClickListener(v -> new DatePickerDialog(AdminJobHistorySearchActivity.this, endDateSetListener,
                                                               myCalendar.get(Calendar.YEAR),
                                                               myCalendar.get(Calendar.MONTH),
                                                               myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Ignore NetworkOnMainThreadException in this activity
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            allUsers = Lists.newArrayList(PLACE_HOLDER_OPTION);
            allUsers.addAll(DatabaseHelper.listAllUsers());
            ArrayAdapter<String> ownerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allUsers);
            ownerSpinner.setAdapter(ownerAdapter);

            allProducts = Lists.newArrayList(PLACE_HOLDER_OPTION);
            allProducts.addAll(DatabaseHelper.listProducts());
            ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProducts);
            productSpinner.setAdapter(productAdapter);
        } catch (Exception e) {
            errorPopUp("获取信息失败！");
            finish();
        }
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
                Intent intent = new Intent(AdminJobHistorySearchActivity.this, JobHistoryListActivity.class);
                intent.putExtra(ActivityConstants.START_DATE_EXTRA, startDateString);
                intent.putExtra(ActivityConstants.END_DATE_EXTRA, endDateString);
                if (ownerSpinner.getSelectedItemPosition() != AdapterView.INVALID_POSITION
                            && !PLACE_HOLDER_OPTION.equals(allUsers.get(ownerSpinner.getSelectedItemPosition()))) {
                    intent.putExtra(ActivityConstants.OWNER_NAME_EXTRA, allUsers.get(ownerSpinner.getSelectedItemPosition()));
                }

                if (productSpinner.getSelectedItemPosition() != AdapterView.INVALID_POSITION
                            && !PLACE_HOLDER_OPTION.equals(allProducts.get(productSpinner.getSelectedItemPosition()))) {
                    intent.putExtra(ActivityConstants.PRODUCT_NAME_EXTRA, allProducts.get(productSpinner.getSelectedItemPosition()));
                }

                startActivity(intent);
            }
        };

        // start thread
        background.start();
    }

    private void updateStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
        startDateET.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
        endDateET.setText(sdf.format(myCalendar.getTime()));
    }
}