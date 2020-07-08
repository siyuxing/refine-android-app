package com.refine.activities.search;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.adapters.SingleSelectItemAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;

public class ProductStockSearchActivity extends CommonActivity {

    private RecyclerView recyclerView;
    private SingleSelectItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_stock_search);

        setTitle("产品库存查询");

        recyclerView = findViewById(R.id.item_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductStockSearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Ignore NetworkOnMainThreadException in this activity
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            List<String> allProducts = DatabaseHelper.listProducts();

            if (allProducts.isEmpty()) {
                normalPopUp("产品信息为空！");
            }

            adapter = new SingleSelectItemAdapter(ProductStockSearchActivity.this, allProducts);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            errorPopUp("获取产品信息失败！");
            finish();
        }
    }

    public void search(View v) {
        if (adapter.getSelected() == null) {
            errorPopUp("请选择产品");
        } else {
            Thread background = new Thread() {
                public void run() {
                    Intent intent = new Intent(ProductStockSearchActivity.this, ProductStockDetailsActivity.class);

                    intent.putExtra(ActivityConstants.PRODUCT_NAME_EXTRA, adapter.getSelected());
                    startActivity(intent);
                }
            };

            // start thread
            background.start();
        }

    }
}