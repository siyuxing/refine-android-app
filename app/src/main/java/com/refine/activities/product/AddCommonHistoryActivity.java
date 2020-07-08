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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.database.InsuffcientProductException;
import com.refine.model.ActivityConstants;
import com.refine.model.Operation;
import com.refine.model.ProductStatus;
import com.refine.model.ProductStock;

public abstract class AddCommonHistoryActivity extends CommonActivity {
    private EditText dateET;
    private EditText successCountET;
    private EditText failCountET;
    private EditText noteET;
    private Spinner productSpinner;

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private final Operation operation;
    private final ProductStatus fromStatus;
    private final ProductStatus toStatus;

    private List<String> allProducts = Lists.newArrayList(ActivityConstants.SPINNER_PLACE_HOLDER_OPTION);

    public AddCommonHistoryActivity(Operation operation, ProductStatus fromStatus, ProductStatus toStatus) {
        this.operation = operation;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_common_history);

        setTitle(String.format("添加%s信息", operation.name()));

        dateET = findViewById(R.id.history_date);
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

        dateET.setOnClickListener(v -> new DatePickerDialog(AddCommonHistoryActivity.this, dateSetListener,
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
            productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (!ActivityConstants.SPINNER_PLACE_HOLDER_OPTION.equals(allProducts.get(position))) {
                        String productName = allProducts.get(position);
                        Thread background = new Thread() {
                            public void run() {
                                try {
                                    List<ProductStock> productStocks = DatabaseHelper.searchProductStock(productName, fromStatus.getStatusCode());

                                    long fromStatusProductCount = productStocks.isEmpty() ? 0 : Iterables.getOnlyElement(productStocks).getNumOfProduct();
                                    informationPopUp(String.format("%s的\"%s\"的库存为(%s)", fromStatus.name(), productName, fromStatusProductCount));
                                } catch (Exception e) {
                                    errorPopUp("读取库存信息失败！");
                                }
                            }
                        };
                        background.start();
                    } else {
                        dismissPopUp();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    dismissPopUp();
                }
            });
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
                        DatabaseHelper.mutateProductCountInStock(productName, fromStatus.getStatusCode(), -(successCount + failCount));
                        if (toStatus != null) {
                            DatabaseHelper.mutateProductCountInStock(productName, toStatus.getStatusCode(), successCount);
                        }
                        DatabaseHelper.addJobHistory(productName, date, AccountProfileLocator.getProfile().getCurrentUser(),
                                                     operation.getOperationCode(), note, successCount, failCount, null);

                        successPopUp("添加记录成功！");

                        sleep(1000);

                        finish();
                    } catch (InsuffcientProductException e) {
                        errorPopUp(e.getMessage());
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
