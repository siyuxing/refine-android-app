package com.refine.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.mysql.jdbc.StringUtils;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.database.ResourceAlreadyExistException;

public class AddProductActivity extends CommonActivity {
    private EditText productNameET;

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
    }

    public void addProduct(View v) {
        final String productName = productNameET.getText().toString();

        if (StringUtils.isNullOrEmpty(productName)) {
            errorPopUp("产品名称无效！");
        } else {
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

}