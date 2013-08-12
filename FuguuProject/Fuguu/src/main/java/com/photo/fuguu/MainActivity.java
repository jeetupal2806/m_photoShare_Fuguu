package com.photo.fuguu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

public class MainActivity extends Activity {

    ParseUser currentUser = ParseUser.getCurrentUser();
    String struser = currentUser.getUsername().toString();
    String struser_email = currentUser.getEmail().toString();

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// fetching current users name and implementaion of logout code [CU&LOUT]
        TextView txtusername = (TextView) findViewById(R.id.txtusername);
        // Set the currentUser String into TextView
        txtusername.setText("Hi " + struser+ " ;)");
        ImageButton logout = (ImageButton) findViewById(R.id.buttonUserlogout);
        logout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                finish();
                Intent myIntent = new Intent(MainActivity.this, AppLoginActivity.class);
                myIntent.putExtra("key", '2');
                MainActivity.this.startActivity(myIntent);
            }
        }); // end of [CU&LOUT]

        Button btn_load_pic = (Button) findViewById(R.id.buttonLoadPicture);
        Button btn_take_pic = (Button) findViewById(R.id.buttonTakePicture);
        ImageButton btn_user_timeline = (ImageButton) findViewById(R.id.buttonUserTimeline);
        ImageButton btn_profile = (ImageButton) findViewById(R.id.buttonProfilepic);
        ImageButton btn_addtext = (ImageButton) findViewById(R.id.buttonAddtext);


        btn_user_timeline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, TimelineActivity.class);
                myIntent.putExtra("key", '2');
                MainActivity.this.startActivity(myIntent);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        btn_addtext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        btn_load_pic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        btn_take_pic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // String picturePath contains the path of selected Image
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();

            ParseFile file = new ParseFile("androidload.png", image);
            file.saveInBackground();
            ParseObject imgupload = new ParseObject("ImageUpload");
            imgupload.put("ImageName", "AndroidLoad Logo");
            imgupload.put("ImageFile", file);
            imgupload.put("username",struser);
            imgupload.put("email",struser_email);
            imgupload.saveInBackground();

            // Show a simple toast message
            Toast.makeText(MainActivity.this, "Image Uploaded",
                    Toast.LENGTH_SHORT).show();

        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(photo);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            ParseFile file = new ParseFile("androidbegin.png", image);
            file.saveInBackground();

            //after the save completes, you can associate a ParseFile onto a ParseObject just like any other piece of data:
            ParseObject imgupload = new ParseObject("ImageUpload");
            imgupload.put("ImageName", "AndroidBegin Logo");
            imgupload.put("ImageFile", file);
            imgupload.put("username",struser);
            imgupload.put("email",struser_email);
            imgupload.saveInBackground();

            Toast.makeText(MainActivity.this, "Image Uploaded",
                    Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}