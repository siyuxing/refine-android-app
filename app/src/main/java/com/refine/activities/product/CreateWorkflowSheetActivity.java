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
import com.mysql.jdbc.StringUtils;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.Operation;
import customfonts.PrefixEditTest;

public class CreateWorkflowSheetActivity extends CommonActivity {
    public static final String WORKFLOW_SHEET_ID_DATE_FORMAT = "yyyyMMdd-"; //In which you need put here

    private EditText dateET;
    private PrefixEditTest sheetIdET;
    private EditText materialET;
    private EditText materialCountET;
    private EditText productCountET;

    private Spinner productSpinner;

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private List<String> allProducts = Lists.newArrayList(ActivityConstants.SPINNER_PLACE_HOLDER_OPTION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workflow_sheet);

        setTitle("创建任务单");

        dateET = findViewById(R.id.history_date);
        sheetIdET = findViewById(R.id.sheet_id);
        materialET = findViewById(R.id.material);
        materialCountET = findViewById(R.id.material_count);
        productCountET = findViewById(R.id.product_count);

        productSpinner = findViewById(R.id.product_list);

        updateDate();

        dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        };

        dateET.setOnClickListener(v -> new DatePickerDialog(CreateWorkflowSheetActivity.this, dateSetListener,
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

    public void create(View v) {
        if (ActivityConstants.SPINNER_PLACE_HOLDER_OPTION.equals(allProducts.get(productSpinner.getSelectedItemPosition()))) {
            errorPopUp("请选择产品");
        } else {
            final String sheetId = sheetIdET.getText().toString();
            final String material = materialET.getText().toString();
            final String productName = allProducts.get(productSpinner.getSelectedItemPosition());

            final Date date;
            final int materialCount, productCount;
            try {
                materialCount = Integer.parseInt(materialCountET.getText().toString());
                productCount = Integer.parseInt(productCountET.getText().toString());
                date = Date.valueOf(dateET.getText().toString());
            } catch (Exception e) {
                errorPopUp("任务单数量信息或日期无效！");
                return;
            }

            if (StringUtils.isNullOrEmpty(sheetId) || StringUtils.isNullOrEmpty(material)) {
                errorPopUp("任务单号或原材料信息无效！");
                return;
            }

            Thread background = new Thread() {
                public void run() {
                    try {
                        Long productId = DatabaseHelper.getProductId(productName);
                        DatabaseHelper.addWorkflowSheet(productId, sheetId, date, material, materialCount, productCount,
                                                        AccountProfileLocator.getProfile().getCurrentUser());
                        DatabaseHelper.createWorkflowDetails(productId, sheetId, date, productCount, Operation.浇注);

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
        dateET.setText(getDateFormat().format(myCalendar.getTime()));

        SimpleDateFormat sheetIdSdf = new SimpleDateFormat(WORKFLOW_SHEET_ID_DATE_FORMAT, Locale.CHINA);
        sheetIdET.setCharactersNoChangeInitial(sheetIdSdf.format(myCalendar.getTime()));
    }
}