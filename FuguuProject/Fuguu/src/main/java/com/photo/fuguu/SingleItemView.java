package com.photo.fuguu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SingleItemView extends Activity {
    // Declare Variables
    String phone;
    ProgressDialog mProgressDialog;
    Bitmap bmImg = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleitemview);

        ImageButton back_btn = (ImageButton) findViewById(R.id.back_btn_2timeline);
        back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(SingleItemView.this, TimelineActivity.class);
                SingleItemView.this.startActivity(myIntent);
            }
        });
        // Execute loadSingleView AsyncTask
        new loadSingleView().execute();
    }

    public class loadSingleView extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(SingleItemView.this);
            mProgressDialog.setTitle("fuguu load image babe !!");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                // Retrieve data from ListViewAdapter on click event
                Intent i = getIntent();
                // Get the result of phone
                phone = i.getStringExtra("phone");
                // Download the Image from the result URL given by phone
                URL url = new URL(phone);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String args) {
            ImageView phone = (ImageView) findViewById(R.id.phone);
            // Set results to the ImageView
            phone.setImageBitmap(bmImg);
            mProgressDialog.dismiss();
        }
    }
}