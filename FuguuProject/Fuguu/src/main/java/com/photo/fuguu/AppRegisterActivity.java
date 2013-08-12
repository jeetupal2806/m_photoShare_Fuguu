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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class AppRegisterActivity extends Activity {

    EditText reg_Fullname,reg_Pass,reg_Email;
    protected ProgressDialog proDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        loginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                Intent i = new Intent(getApplicationContext(), AppLoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button btn_registr = (Button) findViewById(R.id.btnRegister);
        btn_registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // after click registr butn action
                startLoading();

                reg_Fullname = (EditText) findViewById(R.id.reg_fullname);
                reg_Email = (EditText) findViewById(R.id.reg_email);
                reg_Pass = (EditText) findViewById(R.id.reg_password);

                // store always lower case
                String userfullname = reg_Fullname.getText().toString();
                userfullname = userfullname.toLowerCase();
                String useremail = reg_Email.getText().toString();
                useremail = useremail.toLowerCase();
                String userpass = reg_Pass.getText().toString();

                Log.v("EditText", userfullname);
                Log.v("EditText", useremail);
                Log.v("EditText", userpass);
                // Force user to fill up the form
                if (userfullname.equals("") && useremail.equals("") && userpass.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();
                    stopLoading();
                } else {
                    // Save new user data into Parse.com Data Storage
                ParseUser user = new ParseUser();
                user.setUsername(userfullname);
                user.setPassword(userpass);
                user.setEmail(useremail);
                user.put("phone", "650-111-0000");

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.
                            stopLoading();
                            Toast.makeText(getApplicationContext(),
                                    "Successfully Registered ",
                                    Toast.LENGTH_LONG).show();

                            Intent myIntent = new Intent(AppRegisterActivity.this, MainActivity.class);
                            AppRegisterActivity.this.startActivity(myIntent);
                        } else {
                            stopLoading();
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            Toast.makeText(getApplicationContext(),
                                    "Account could not be created ",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
                }
            }
        });

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
        getMenuInflater().inflate(R.menu.app_register, menu);
        return true;
    }
    
}
