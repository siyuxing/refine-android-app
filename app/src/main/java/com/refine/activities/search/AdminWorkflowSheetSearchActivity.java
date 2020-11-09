package com.refine.activities.search;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.common.collect.Lists;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.User;

public class AdminWorkflowSheetSearchActivity extends CommonActivity {
    private static final String PLACE_HOLDER_OPTION = "全部";
    private static final User PLACE_HOLDER_USER = User.createPlaceHolderUser(PLACE_HOLDER_OPTION);

    private EditText startDateET;
    private EditText endDateET;
    private Spinner ownerSpinner;
    private Spinner productSpinner;

    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    private List<User> allUsers;
    private List<String> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_history_search);

        setTitle("任务单查询");

        startDateET = findViewById(R.id.start_date);
        endDateET = findViewById(R.id.end_date);

        EditText ownerTitleET = findViewById(R.id.owner_title);
        ownerTitleET.setHint("提交人");

        ownerSpinner = findViewById(R.id.owner);
        productSpinner = findViewById(R.id.product);

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

        startDateET.setOnClickListener(v -> new DatePickerDialog(AdminWorkflowSheetSearchActivity.this, startDateSetListener,
                                                                 startCalendar.get(Calendar.YEAR),
                                                                 startCalendar.get(Calendar.MONTH),
                                                                 startCalendar.get(Calendar.DAY_OF_MONTH)).show());

        endDateET.setOnClickListener(v -> new DatePickerDialog(AdminWorkflowSheetSearchActivity.this, endDateSetListener,
                                                               endCalendar.get(Calendar.YEAR),
                                                               endCalendar.get(Calendar.MONTH),
                                                               endCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Ignore NetworkOnMainThreadException in this activity
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            allUsers = Lists.newArrayList(PLACE_HOLDER_USER);
            allUsers.addAll(DatabaseHelper.listAllUsers());
            List<String> displayUsers = new ArrayList<>();
            for (User user : allUsers) {
                displayUsers.add(user.getDisplayName());
            }
            ArrayAdapter<String> ownerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, displayUsers);
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

    public void trySearch(View v) {
        Button button = findViewById(R.id.button);
        button.setEnabled(false);
        try {
            search(v);
        } finally {
            button.setEnabled(true);
        }
    }

    private void search(View v) {
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
                Intent intent = new Intent(AdminWorkflowSheetSearchActivity.this, WorkflowSheetListActivity.class);
                intent.putExtra(ActivityConstants.START_DATE_EXTRA, startDateString);
                intent.putExtra(ActivityConstants.END_DATE_EXTRA, endDateString);
                if (ownerSpinner.getSelectedItemPosition() != AdapterView.INVALID_POSITION
                            && !PLACE_HOLDER_USER.equals(allUsers.get(ownerSpinner.getSelectedItemPosition()))) {
                    intent.putExtra(ActivityConstants.OWNER_NAME_EXTRA, allUsers.get(ownerSpinner.getSelectedItemPosition()).getUsername());
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
        startDateET.setText(getDateFormat().format(startCalendar.getTime()));
    }

    private void updateEndDate() {
        endDateET.setText(getDateFormat().format(endCalendar.getTime()));
    }
}