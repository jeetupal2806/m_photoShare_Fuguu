package com.photo.fuguu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends Activity {

    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<PictureList> picturearraylist = null;
    //private int limit = 7;
    ParseUser currentUser = ParseUser.getCurrentUser();
    String struser_email = currentUser.getEmail().toString();


    @Override
    public void onCreate(Bundle savedInstanceState) {

       requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

        ImageButton refesh_btn = (ImageButton) findViewById(R.id.button_refresh);
        ImageButton back_btn = (ImageButton) findViewById(R.id.back_btn_timeline);
        refesh_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(TimelineActivity.this, MainActivity.class);
                TimelineActivity.this.startActivity(myIntent);
            }
        });

    }
    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(TimelineActivity.this);
            mProgressDialog.setTitle("Fuguu load images baby !");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            picturearraylist = new ArrayList<PictureList>();
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "ImageUpload");
                query.whereEqualTo("email",struser_email);
               // query.setLimit(limit);
                query.orderByDescending("createdAt");
                ob = query.find();
                for (ParseObject country : ob) {
                    ParseFile image = (ParseFile) country.get("ImageFile");
                    PictureList map = new PictureList();
                    map.setPhone(image.getUrl());
                    picturearraylist.add(map);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            listview = (ListView) findViewById(R.id.listview);
            adapter = new ListViewAdapter(TimelineActivity.this,
                    picturearraylist);
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }
}