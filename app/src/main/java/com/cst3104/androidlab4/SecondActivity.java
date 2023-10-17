package com.cst3104.androidlab4;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private ImageView myImageView;

    private Button button3;
    private static final int REQUEST_CALL_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textView3 = findViewById(R.id.textView3);
        editTextPhone = findViewById(R.id.editTextPhone);
        Button callButton = findViewById(R.id.button2);
        myImageView = findViewById(R.id.myImageView);
        button3 = findViewById(R.id.button3);


        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        TextView welcomeMsg = findViewById(R.id.textView3);
        String displayMsg = "Welcome back " + emailAddress;
        welcomeMsg.setText(displayMsg);

        int MY_PERMISSIONS_REQUEST_CAMERA=0;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
            }
        }

        editTextPhone.setOnClickListener(view -> {
            String phoneNumber = editTextPhone.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        });

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Inside your onActivityResult method
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap thumbnail = (Bitmap) data.getParcelableExtra("data");
                            myImageView.setImageBitmap(thumbnail);

                            FileOutputStream fOut = null;
                            try {
                                fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                Objects.requireNonNull(thumbnail).compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        callButton.setOnClickListener(view -> {
            String phoneNumber = editTextPhone.getText().toString();
            Log.d("ButtonClick", "Change Picture button clicked");
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        });

        String filename = "Picture.png";
        File file = new File(getExternalFilesDir(null), filename);
        if (file.exists()) {
            Bitmap theImage;
            theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            myImageView.setImageBitmap(theImage);
        }

        button3.setOnClickListener(view -> {
            Log.w("SecondActivity", "Before Camera Launch");
            cameraResult.launch(cameraIntent);
        });

        SharedPreferences prefs = getSharedPreferences("MyData", MODE_PRIVATE);
        String savedNumber = prefs.getString("EmailAddress", "");
        textView3.setText(emailAddress);
        }

        protected void onPause() {
            super.onPause();
            EditText phoneText;
            phoneText = findViewById(R.id.editTextPhone);
            String phone = phoneText.getText().toString();

            SharedPreferences prefs = getSharedPreferences("MyData", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("PhoneNumber", phone);
            editor.apply();
        }
    }

