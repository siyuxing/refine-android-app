package com.refine.activities;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.activities.admin.AdminUserHome;
import com.refine.activities.product.ProductOwnerOperations;

public class UserLoginActivity extends CommonActivity {

    private EditText usernameET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AccountProfileLocator.getProfile() != null) {
            normalPopUp("用户已登陆，进入主界面...");
            gotoHomePage();
        }
    }

    public void loginActivity(View view) throws ExecutionException, InterruptedException {
        Button login = findViewById(R.id.button);
        login.setEnabled(false);
        login.setClickable(false);
        try {
            final String username = usernameET.getText().toString();
            final String password = passwordET.getText().toString();

            Callable<Boolean> validLogin = () -> AccountProfileLocator.login(username, password);

            FutureTask<Boolean> task = new FutureTask<>(validLogin);
            Thread validLoginThread = new Thread(task);

            validLoginThread.start();

            if (task.get()) {
                successPopUp("登陆成功，进入主界面...");
                gotoHomePage();
            } else {
                errorPopUp("登陆失败");
            }
        } finally {
            login.setEnabled(true);
            login.setClickable(true);
        }
    }

    private void gotoHomePage() {
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 1 second
                    sleep(1000);

                    Intent intent;
                    if (AccountProfileLocator.getProfile().isAdminUser()) {
                        intent = new Intent(UserLoginActivity.this, AdminUserHome.class);
                    } else {
                        intent = new Intent(UserLoginActivity.this, ProductOwnerOperations.class);
                    }
                    startActivity(intent);
                    resetInputs();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

    private void resetInputs() {
        usernameET.setText("");
        passwordET.setText("");
    }
}
