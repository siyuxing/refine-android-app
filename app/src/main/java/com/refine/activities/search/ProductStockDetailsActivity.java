package com.refine.activities.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.ProductStatus;
import com.refine.model.ProductStock;

public class ProductStockDetailsActivity extends CommonActivity {

    private EditText productNameET;
    private EditText pouredET;
    private EditText hydroET;
    private EditText driedET;
    private EditText cutET;
    private EditText finishedET;

    private String productName;
    private Map<ProductStatus, ProductStock> productStockMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_stock_details);

        setTitle("修改工作历史记录");

        Bundle extras = getIntent().getExtras();

        if (extras == null || extras.getString(ActivityConstants.PRODUCT_NAME_EXTRA) == null) {
            failActivityOnError();
        } else {
            productName = extras.getString(ActivityConstants.PRODUCT_NAME_EXTRA);
            // Ignore NetworkOnMainThreadException in this activity
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            productNameET = findViewById(R.id.product);
            productNameET.setText(productName);

            pouredET = findViewById(R.id.poured);
            hydroET = findViewById(R.id.hydro);
            driedET = findViewById(R.id.dried);
            cutET = findViewById(R.id.cut);
            finishedET = findViewById(R.id.finished);

            try {
                List<ProductStock> productStocks = DatabaseHelper.searchProductStock(productName);

                for (ProductStock stock : productStocks) {
                    productStockMap.put(stock.getStatus(), stock);
                    switch (stock.getStatus()) {
                        case 待醛化:
                            pouredET.setText(String.valueOf(stock.getNumOfProduct()));
                            break;
                        case 待干燥:
                            hydroET.setText(String.valueOf(stock.getNumOfProduct()));
                            break;
                        case 待切割:
                            driedET.setText(String.valueOf(stock.getNumOfProduct()));
                            break;
                        case 待包装入库:
                            cutET.setText(String.valueOf(stock.getNumOfProduct()));
                            break;
                        case 成品:
                            finishedET.setText(String.valueOf(stock.getNumOfProduct()));
                    }
                }

            } catch (Exception e) {
                failActivityOnError();
            }
        }
    }

    public void updateStock(View v) {
        if (!AccountProfileLocator.getProfile().isAdminUser()) {
            errorPopUp("非管理员账号，仅有管理员可以校正库存信息。");
            return;
        }
        pouredET.setFocusable(true);
        pouredET.setFocusableInTouchMode(true);
        pouredET.setTextColor(getResources().getColor(R.color.colorSuccess));

        hydroET.setFocusable(true);
        hydroET.setFocusableInTouchMode(true);
        hydroET.setTextColor(getResources().getColor(R.color.colorSuccess));

        driedET.setFocusable(true);
        driedET.setFocusableInTouchMode(true);
        driedET.setTextColor(getResources().getColor(R.color.colorSuccess));

        cutET.setFocusable(true);
        cutET.setFocusableInTouchMode(true);
        cutET.setTextColor(getResources().getColor(R.color.colorSuccess));

        finishedET.setFocusable(true);
        finishedET.setFocusableInTouchMode(true);
        finishedET.setTextColor(getResources().getColor(R.color.colorSuccess));
    }

    public void dismiss(View v) {
        final long pouredCount, hydroCount, driedCount, cutCount, finishedCount;
        try {
            pouredCount = Long.parseLong(pouredET.getText().toString());
            hydroCount = Long.parseLong(hydroET.getText().toString());
            driedCount = Long.parseLong(driedET.getText().toString());
            cutCount = Long.parseLong(cutET.getText().toString());
            finishedCount = Long.parseLong(finishedET.getText().toString());
        } catch (Exception e) {
            errorPopUp("库存数量信息无效！");
            return;
        }

        Thread background = new Thread() {
            public void run() {
                try {
                    long oldPouredCount = productStockMap.containsKey(ProductStatus.待醛化) ?
                                          productStockMap.get(ProductStatus.待醛化).getNumOfProduct() : 0;
                    if (oldPouredCount != pouredCount) {
                        DatabaseHelper.updateProductCountInStock(productName, ProductStatus.待醛化.getStatusCode(), pouredCount);
                    }

                    long oldHydroCount = productStockMap.containsKey(ProductStatus.待干燥) ?
                                         productStockMap.get(ProductStatus.待干燥).getNumOfProduct() : 0;
                    if (oldHydroCount != hydroCount) {
                        DatabaseHelper.updateProductCountInStock(productName, ProductStatus.待干燥.getStatusCode(), hydroCount);
                    }

                    long oldDriedCount = productStockMap.containsKey(ProductStatus.待切割) ?
                                         productStockMap.get(ProductStatus.待切割).getNumOfProduct() : 0;
                    if (oldDriedCount != driedCount) {
                        DatabaseHelper.updateProductCountInStock(productName, ProductStatus.待切割.getStatusCode(), driedCount);
                    }

                    long oldCutCount = productStockMap.containsKey(ProductStatus.待包装入库) ?
                                       productStockMap.get(ProductStatus.待包装入库).getNumOfProduct() : 0;
                    if (oldCutCount != cutCount) {
                        DatabaseHelper.updateProductCountInStock(productName, ProductStatus.待包装入库.getStatusCode(), cutCount);
                    }

                    long oldFinishedCount = productStockMap.containsKey(ProductStatus.成品) ?
                                            productStockMap.get(ProductStatus.成品).getNumOfProduct() : 0;
                    if (oldFinishedCount != finishedCount) {
                        DatabaseHelper.updateProductCountInStock(productName, ProductStatus.成品.getStatusCode(), finishedCount);
                    }

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

        pouredET.setFocusable(false);
        pouredET.setFocusableInTouchMode(false);
        pouredET.setTextColor(getResources().getColor(R.color.trans_text_black));

        hydroET.setFocusable(false);
        hydroET.setFocusableInTouchMode(false);
        hydroET.setTextColor(getResources().getColor(R.color.trans_text_black));

        driedET.setFocusable(false);
        driedET.setFocusableInTouchMode(false);
        driedET.setTextColor(getResources().getColor(R.color.trans_text_black));

        cutET.setFocusable(false);
        cutET.setFocusableInTouchMode(false);
        cutET.setTextColor(getResources().getColor(R.color.trans_text_black));

        finishedET.setFocusable(false);
        finishedET.setFocusableInTouchMode(false);
        finishedET.setTextColor(getResources().getColor(R.color.trans_text_black));
    }

    private void failActivityOnError() {
        errorPopUp("获取库存信息失败！");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignore exception
        }
        finish();
    }
}
