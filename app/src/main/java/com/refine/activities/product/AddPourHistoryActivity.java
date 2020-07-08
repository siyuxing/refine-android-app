package com.refine.activities.product;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.common.collect.Lists;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.Operation;
import com.refine.model.ProductStatus;

public class AddPourHistoryActivity extends CommonActivity {
    private EditText dateET;
    private EditText materialCountET;
    private EditText successCountET;
    private EditText failCountET;
    private EditText noteET;
    private Spinner productSpinner;

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private List<String> allProducts = Lists.newArrayList(ActivityConstants.SPINNER_PLACE_HOLDER_OPTION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pour_history);

        setTitle("添加浇注信息");

        dateET = findViewById(R.id.history_date);
        materialCountET = findViewById(R.id.material_count);
        successCountET = findViewById(R.id.success_count);
        failCountET = findViewById(R.id.fail_count);
        noteET = findViewById(R.id.note);

        productSpinner = findViewById(R.id.product_list);

        dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        };

        dateET.setOnClickListener(v -> new DatePickerDialog(AddPourHistoryActivity.this, dateSetListener,
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
            allProducts = Lists.newArrayList(ActivityConstants.SPINNER_PLACE_HOLDER_OPTION);
            allProducts.addAll(DatabaseHelper.listProducts());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProducts);
            productSpinner.setAdapter(adapter);
        } catch (Exception e) {
            errorPopUp("获取产品信息失败！");
            finish();
        }
    }

    public void addHistory(View v) {
        if (ActivityConstants.SPINNER_PLACE_HOLDER_OPTION.equals(allProducts.get(productSpinner.getSelectedItemPosition()))) {
            errorPopUp("请选择产品");
        } else {
            final String note = noteET.getText().toString();
            final String productName = allProducts.get(productSpinner.getSelectedItemPosition());

            final Date date;
            final int materialCount, successCount, failCount;
            try {
                materialCount = Integer.parseInt(materialCountET.getText().toString());
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

                        DatabaseHelper.mutateProductCountInStock(productName, ProductStatus.待醛化.getStatusCode(), successCount);
                        DatabaseHelper.addJobHistory(productName, date, AccountProfileLocator.getProfile().getCurrentUser(),
                                                     Operation.浇注.getOperationCode(), note, successCount, failCount, materialCount);

                        successPopUp("添加记录成功！");

                        sleep(1000);

                        finish();
                    } catch (Exception e) {
                        errorPopUp("添加记录失败！");
                    }
                }
            };

            // start thread
            background.start();
        }
    }

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);

        dateET.setText(sdf.format(myCalendar.getTime()));
    }
}