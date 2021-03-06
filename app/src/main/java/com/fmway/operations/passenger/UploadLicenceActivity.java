package com.fmway.operations.passenger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.fmway.R;
import com.fmway.models.user.upload.LicenseParseDefinitions;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadLicenceActivity extends AppCompatActivity {

    private EditText uploadText;
    private ImageView imageView;
    private Bitmap chosenImage;
    private String userID;


    private LicenseParseDefinitions definitions = new LicenseParseDefinitions();

    /**
     * passenger license upload page
     * activity constructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_licence_activity);

        uploadText = findViewById(R.id.upload_activity_licence);
        imageView   = findViewById(R.id.upload_activity_image);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
            userID =(String) b.get("userID");

    }

    /**
     * button handling for license photo
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
        object.put("userID",userID);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e!= null) {
                    Toast.makeText(getApplicationContext()
                                    ,e.getLocalizedMessage()
                                    ,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext()
                                    ,"Your driver license been uploaded successfully!"
                                    ,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent (getApplicationContext(),PassengerActivity.class);
                    startActivity(intent);
                }}
        });

    }

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
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    chosenImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(chosenImage);
                }else {
                    chosenImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    imageView.setImageBitmap(chosenImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


