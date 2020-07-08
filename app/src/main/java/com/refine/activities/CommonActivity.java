package com.refine.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.refine.R;
import com.refine.account.AccountProfileLocator;

public abstract class CommonActivity extends AppCompatActivity {
    public static final String DATE_FORMAT = "yyyy-MM-dd"; //In which you need put here

    private Snackbar snackbar = null;

    public void logoutActivity(View view) {
        successPopUp("退出登陆");

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 1 second
                    sleep(1000);

                    AccountProfileLocator.resetProfile();
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
}
