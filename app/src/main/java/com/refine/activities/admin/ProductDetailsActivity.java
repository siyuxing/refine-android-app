package com.refine.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.mysql.jdbc.StringUtils;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.database.DatabaseHelper;
import com.refine.database.ResourceAlreadyExistException;
import com.refine.model.ActivityConstants;

public class ProductDetailsActivity extends CommonActivity {
    private EditText productNameET;

    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        setTitle("修改产品");

        productNameET = findViewById(R.id.product_name);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productName = extras.getString(ActivityConstants.PRODUCT_NAME_EXTRA);
        } else {
            finish();
        }
        productNameET.setText(productName);
    }

    public void modifyProduct(View v) {
        final String newProductName = productNameET.getText().toString();

        if (StringUtils.isNullOrEmpty(newProductName)) {
            errorPopUp("产品名称无效！");
        } else {
            Thread background = new Thread() {
                public void run() {
                    try {
                        DatabaseHelper.modifyProduct(productName, newProductName);

                        successPopUp("修改产品成功！");

                        sleep(1000);

                        finish();
                    } catch (ResourceAlreadyExistException e) {
                        errorPopUp("产品已存在！");
                    } catch (Exception e) {
                        errorPopUp("修改产品失败！");
                    }
                }
            };

            // start thread
            background.start();
        }
    }

}