package com.fmway.operations.passenger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.fmway.R;
import com.fmway.models.user.upload.IssuesParseDefinitions;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class UploadActivity extends AppCompatActivity {

    private EditText uploadText;
    private ImageView imageView;
    private Bitmap chosenImage;

    private IssuesParseDefinitions definitions = new IssuesParseDefinitions();
    /**
     * passenger issue page
     * activity constructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);

        uploadText = findViewById(R.id.upload_activity_issue);
        imageView   = findViewById(R.id.upload_activity_image);
    }

    /**
     * button handling for photo and send issue
     * @param view
     */
    public void upload (View view) {
        String upload = uploadText.getText().toString();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        chosenImage.compress(Bitmap.CompressFormat.PNG, 50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ParseFile parseFile = new ParseFile (definitions.getFileName(),bytes);

        ParseObject object = new ParseObject(definitions.getClassName());
        object.put(definitions.getImageKey(),parseFile);
        object.put(definitions.getUploadKey(), upload);
        object.put(definitions.getUsernameKey(), ParseUser.getCurrentUser().getUsername());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e!= null) {
                    Toast.makeText(getApplicationContext()
                                    ,e.getLocalizedMessage()
                                    ,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext()
                                    ,"Uploaded without a problem!"
                                    ,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext()
                                                ,PassengerActivity.class);
                    startActivity(intent);
            }}
        });

    }

    /**
     * open file choose file dialog
     * @param view
     */
    public void chooseImage (View view){
        // if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {  //kullanıcının galerisine erişim izni
        Intent intent = new Intent (Intent.ACTION_PICK
                                    ,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageData =data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder
                                                    .createSource(this.getContentResolver()
                                                                    ,imageData);
                    chosenImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(chosenImage);
                }else {
                    chosenImage = MediaStore.Images.Media.getBitmap(this.getContentResolver()
                                                                    ,imageData);
                    imageView.setImageBitmap(chosenImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}









