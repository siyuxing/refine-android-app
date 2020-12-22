package com.refine.activities.admin;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.adapters.UserAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.User;

public class CheckUserActivity extends CommonActivity {
    private RecyclerView recyclerView;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_item);

        setTitle("查看用户");

        recyclerView = findViewById(R.id.item_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CheckUserActivity.this);
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
            List<User> allUsers = DatabaseHelper.listAllUsers();
            adapter = new UserAdapter(CheckUserActivity.this, allUsers);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            errorPopUp("获取用户信息失败！");
            finish();
        }
    }

    public void tryCheckDetails(View v) {
        Button button = findViewById(R.id.check_details);
        button.setEnabled(false);
        try {
            checkDetails(v);
        } finally {
            button.setEnabled(true);
        }
    }

    private void checkDetails(View v) {
        if (adapter.getSelected() == null) {
            errorPopUp("请选择用户");
        } else {
            Thread background = new Thread() {
                public void run() {
                    String username = adapter.getSelected().getUsername();

                    Intent intent = new Intent(CheckUserActivity.this, UserDetailsActivity.class);
                    intent.putExtra(ActivityConstants.USER_NAME_EXTRA, username);
                    startActivity(intent);
                }
            };
            // start thread
            background.start();
        }
    }

    public void tryRemove(View v) {
        Button button = findViewById(R.id.delete_button);
        button.setEnabled(false);
        try {
            remove(v);
        } finally {
            button.setEnabled(true);
        }
    }

    private void remove(View v) {
        if (adapter.getSelected() == null) {
            errorPopUp("请选择用户");
        } else {
            Thread background = new Thread() {
                public void run() {
                    try {
                        String username = adapter.getSelected().getUsername();

                        DatabaseHelper.dropUser(username);

                        successPopUp("删除用户成功！");

                        sleep(1000);

                        finish();
                    } catch (Exception e) {
                        errorPopUp("删除用户失败！");
                    }
                }
            };

            // start thread
            background.start();
        }

    }
}
