package com.refine.activities.admin;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.common.collect.Lists;
import com.mysql.jdbc.StringUtils;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.database.ResourceAlreadyExistException;

public class AddProductActivity extends CommonActivity {
    private EditText productNameET;


    private List<String> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setTitle("添加产品");

        productNameET = findViewById(R.id.product_name);
    }

    @Override
    protected void onStart() {
        super.onStart();

        productNameET.setText(null);

        // Ignore NetworkOnMainThreadException in this activity
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            allProducts = DatabaseHelper.listProducts();
        } catch (Exception e) {
            errorPopUp("获取信息失败！");
            allProducts = new ArrayList<>();
        }
    }

    public void tryAddProduct(View v) {
        Button button = findViewById(R.id.button);
        button.setEnabled(false);
        try {
            addProduct(v);
        } finally {
            button.setEnabled(true);
        }
    }

    public void addProduct(View v) {
        final String productName = productNameET.getText().toString();

        if (allProducts.contains(productName)) {
            errorPopUp("产品已存在！");
            return;
        }

        if (StringUtils.isNullOrEmpty(productName)) {
            errorPopUp("产品名称无效！");
            return;
        }

        Thread background = new Thread() {
            public void run() {
                try {
                    DatabaseHelper.addProduct(productName);

                    successPopUp("添加产品成功！");

                    sleep(1000);

                    finish();
                } catch (ResourceAlreadyExistException e) {
                    errorPopUp("产品已存在！");
                } catch (Exception e) {
                    errorPopUp("添加产品失败！");
                }
            }
        };

        // start thread
        background.start();
    }

}