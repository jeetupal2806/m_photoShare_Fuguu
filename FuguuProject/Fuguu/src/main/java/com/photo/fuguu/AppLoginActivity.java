package com.photo.fuguu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class AppLoginActivity extends Activity {

    Button mButton;
    protected ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);


        super.onCreate(savedInstanceState);
        Parse.initialize(this, "dJ9mCrdReUSukCip3R7zWxbmHX6KUdkqBpQY8ab9", "dS9IBkNRTkoj0S6xyfWWhyNLMwZv7wQVpjqFma10");

        setContentView(R.layout.activity_applogin);

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        registerScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent myIntent = new Intent(AppLoginActivity.this, AppRegisterActivity.class);
                AppLoginActivity.this.startActivity(myIntent);
            }
        });

        mButton = (Button) findViewById(R.id.btnLogin);
        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        startLoading();
                        // Force user to fill up the form
                        if (getLoginUserName().equals("") && getLoginPwd().equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Please fill username & password to login",
                                    Toast.LENGTH_LONG).show();
                            stopLoading();
                        } else {
                            // user authentication and login
                            ParseUser.logInInBackground(getLoginUserName(), getLoginPwd(), new LogInCallback() {
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null) {
                                        // ParseUser currentUser = ParseUser.getCurrentUser();
                                        Intent myIntent = new Intent(AppLoginActivity.this, MainActivity.class);
                                        AppLoginActivity.this.startActivity(myIntent);

                                        stopLoading();
                                        Toast.makeText(getApplicationContext(),
                                                "Successfully Logged in",
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        stopLoading();
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "No such user exist, please signup",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                    }
                });
    }

    private String getLoginUserName() {
        String username = ((EditText) findViewById(R.id.usernameEdtext)).getText().toString();
        username = username.toLowerCase();
        Log.i("USERNAME:", username);
        return username;
    }

    private String getLoginPwd() {
        String password = ((EditText) findViewById(R.id.passEdtext)).getText().toString();
        return password;
    }

    public void startLoading() {
        proDialog = new ProgressDialog(this);
        proDialog.setMessage("loading...");
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    public void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_login, menu);
        return true;
    }

}
