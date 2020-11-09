package com.refine.activities.admin;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.common.collect.Lists;
import com.mysql.jdbc.StringUtils;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.adapters.MultiSelectItemAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.ActivityConstants;
import com.refine.model.Operation;
import com.refine.model.User;

public class UserDetailsActivity extends CommonActivity {
    private EditText usernameET;
    private EditText displayNameET;
    private EditText passwordET;
    private RecyclerView recyclerView;

    private String username;
    private List<String> permissions = Lists.newArrayList(Operation.extractPermissions());
    private MultiSelectItemAdapter multiSelectItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        setTitle("修改用户");

        usernameET = findViewById(R.id.username);
        displayNameET = findViewById(R.id.display_name);
        passwordET = findViewById(R.id.password);


        recyclerView = findViewById(R.id.permission_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserDetailsActivity.this);
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
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                username = extras.getString(ActivityConstants.USER_NAME_EXTRA);
            } else {
                finish();
            }

            User user = DatabaseHelper.getUser(username);

            usernameET.setText(username);
            displayNameET.setText(user.getDisplayName());

            multiSelectItemAdapter = new MultiSelectItemAdapter(UserDetailsActivity.this, permissions);
            recyclerView.setAdapter(multiSelectItemAdapter);
            multiSelectItemAdapter.setCheckedItems(user.getPermissions());
        } catch (Exception e) {
            errorPopUp("获取用户信息失败！");
            finish();
        }
    }

    public void tryModifyUser(View v) {
        Button button = findViewById(R.id.button);
        button.setEnabled(false);
        try {
            modifyUser(v);
        } finally {
            button.setEnabled(true);
        }
    }

    private void modifyUser(View v) {
        final String displayName = displayNameET.getText().toString();
        final String password = passwordET.getText().toString();

        List<String> selectedItems = new ArrayList<>();
        for (Integer position : multiSelectItemAdapter.getCheckedPositions()) {
            selectedItems.add(permissions.get(position));
        }

        if (StringUtils.isNullOrEmpty(displayName)
                    || selectedItems.isEmpty()) {
            errorPopUp("用户信息无效！");
        } else {
            Thread background = new Thread() {
                public void run() {
                    try {
                        List<String> permissions = Operation.getPermissionCodes(selectedItems);

                        boolean adminSelected = false;
                        for (String permission : selectedItems) {
                            if (Operation.ADMIN_PERMISSION_NAME.equals(permission)) {
                                adminSelected = true;
                                break;
                            }
                        }

                        DatabaseHelper.updateUser(username, password, adminSelected);
                        DatabaseHelper.updateUserInformation(username, displayName, adminSelected, permissions);

                        successPopUp("修改用户成功！");

                        sleep(1000);

                        finish();
                    } catch (Exception e) {
                        errorPopUp("修改用户失败！");
                    }
                }
            };

            // start thread
            background.start();
        }

    }

}
