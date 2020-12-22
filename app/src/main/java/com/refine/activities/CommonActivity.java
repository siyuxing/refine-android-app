package com.refine.activities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.refine.R;
import com.refine.account.AccountProfileLocator;
import com.refine.utils.PasswordUtils;

public abstract class CommonActivity extends AppCompatActivity {
    public static final String DATE_FORMAT = "yyyy-MM-dd"; //In which you need put here
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    private Snackbar snackbar = null;

    public void tryLogout(View v) {
        Button button = findViewById(R.id.button);
        button.setEnabled(false);
        try {
            logoutActivity(v);
        } finally {
            button.setEnabled(true);
        }
    }

    public void logoutActivity(View view) {
        successPopUp("退出登陆");

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 1 second
                    sleep(500);

                    AccountProfileLocator.resetProfile();
                    saveLoginInfo(null, null);
                    Intent intent = new Intent(CommonActivity.this, UserLoginActivity.class);
                    startActivity(intent);

                    finish();
                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();

    }

    public void back(View view) {
        onBackPressed();
    }

    protected void setTitle(String titleString) {
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(titleString);
    }

    protected void dismissPopUp() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    protected void informationPopUp(String message) {
        snackbar = Snackbar.make(findViewById(R.id.pop_up), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    protected void normalPopUp(String message) {
        Snackbar.make(findViewById(R.id.pop_up), message, Snackbar.LENGTH_LONG).show();
    }

    protected void successPopUp(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.pop_up), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackground(getResources().getDrawable(R.color.colorSuccess));
        snackbar.show();
    }

    protected void errorPopUp(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.pop_up), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackground(getResources().getDrawable(R.color.colorError));
        snackbar.show();
    }

    protected SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
    }

    protected void runInBackground(final Runnable func) {
        Thread background = new Thread() {
            public void run() {
                func.run();
            }
        };

        // start thread
        background.start();
    }

    protected void saveLoginInfo(String username, String password) {
        SharedPreferences wmbPreference = getApplicationContext().getSharedPreferences("RefineManagement", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = wmbPreference.edit();

        editor.putString(USERNAME_KEY, username);
        editor.putString(PASSWORD_KEY, PasswordUtils.encrypt(password));
        editor.apply();
    }

    protected Pair<String, String> getLoginInfo() {
        SharedPreferences wmbPreference = getApplicationContext().getSharedPreferences("RefineManagement", Context.MODE_PRIVATE);

        try {
            String username = wmbPreference.getString(USERNAME_KEY, "");
            String password = PasswordUtils.decrypt(wmbPreference.getString(PASSWORD_KEY, ""));

            return Pair.create(username, password);
        } catch (Exception e) {
            saveLoginInfo(null, null);
            return null;
        }
    }
}
