package com.refine.activities.admin;

import java.util.List;

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

public class DeleteProductActivity extends CommonActivity {

    private RecyclerView recyclerView;
    private SingleSelectItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);

        setTitle("删除产品");

        recyclerView = findViewById(R.id.item_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DeleteProductActivity.this);
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

            adapter = new SingleSelectItemAdapter(DeleteProductActivity.this, allProducts);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            errorPopUp("获取产品信息失败！");
            finish();
        }
    }

    public void removeUser(View v) {
        if (adapter.getSelected() == null) {
            errorPopUp("请选择产品");
        } else {
            Thread background = new Thread() {
                public void run() {
                    try {
                        String productName = adapter.getSelected();

                        DatabaseHelper.deleteProduct(productName);

                        successPopUp("删除产品成功！");

                        sleep(1000);

                        finish();
                    } catch (Exception e) {
                        errorPopUp("删除产品失败！");
                    } finally {
                        adapter.getSelected();
                    }
                }
            };

            // start thread
            background.start();
        }

    }
}