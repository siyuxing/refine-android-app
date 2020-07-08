package com.refine.activities.admin;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import com.google.common.collect.Lists;
import com.mysql.jdbc.StringUtils;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.adapters.MultiSelectItemAdapter;
import com.refine.database.DatabaseHelper;
import com.refine.model.Operation;

public class AddUserActivity extends CommonActivity {
    private EditText displayNameET;
    private EditText usernameET;
    private EditText passwordET;
    private RecyclerView recyclerView;

    private List<String> permissions = Lists.newArrayList(Operation.extractPermissions());
    private MultiSelectItemAdapter multiSelectItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        setTitle("添加用户");

        displayNameET = findViewById(R.id.display_name);
        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);


        recyclerView = findViewById(R.id.permission_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AddUserActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onStart() {
        super.onStart();

        multiSelectItemAdapter = new MultiSelectItemAdapter(AddUserActivity.this, permissions);
        recyclerView.setAdapter(multiSelectItemAdapter);
    }

    public void addUser(View v) {
        final String displayName = displayNameET.getText().toString();
        final String username = usernameET.getText().toString();
        final String password = passwordET.getText().toString();

        List<String> selectedItems = new ArrayList<>();
        for (Integer position : multiSelectItemAdapter.getCheckedPositions()) {
            selectedItems.add(permissions.get(position));
        }

        if (StringUtils.isNullOrEmpty(displayName)
                    || StringUtils.isNullOrEmpty(username)
                    || StringUtils.isNullOrEmpty(password)
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

                        DatabaseHelper.createUser(username, password, adminSelected);
                        DatabaseHelper.updateUserInformation(username, displayName, adminSelected, permissions);

                        successPopUp("添加用户成功！");

                        sleep(1000);

                        finish();
                    } catch (Exception e) {
                        errorPopUp("添加用户失败！");
                    }
                }
            };

            // start thread
            background.start();
        }

    }

}
