package com.cst3104.androidlab4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SecondActivity extends AppCompatActivity {

    private TextView textView3;
    private EditText editTextPhone;
    private Button callButton;
    private ImageButton imageButton;
    private ImageView myImageView;
    private static final int REQUEST_CALL_PERMISSION = 1;

    private ActivityResultLauncher<Intent> cameraResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView3 = findViewById(R.id.textView3);
        editTextPhone = findViewById(R.id.editTextPhone);
        callButton = findViewById(R.id.button2);
        imageButton = findViewById(R.id.imageButton);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");


        int MY_PERMISSIONS_REQUEST_CAMERA=0;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        // Retrieve data from Shared Preferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);


        // Set the retrieved email address in TextView
        textView3.setText(emailAddress);

        setCaptureImage();

        cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Bitmap thumbnail = (Bitmap) data.getParcelableExtra("data");
                                if (thumbnail != null) {
                                    imageButton.setImageBitmap(thumbnail);
                                    saveImageToInternalStorage(thumbnail, "Picture.png");

                                    imageButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // Launch the camera intent
                                            setCaptureImage();
                                        }
                                    });
                                }
                            }
                        }
                    }
                });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editTextPhone.getText().toString();
                Log.d("ButtonClick", "Change Picture button clicked");

                if (ContextCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall(phoneNumber);
                } else {
                    ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                }
            }
        });

        // Load and display the saved image from internal storage
        Bitmap savedImage = loadImageFromInternalStorage("Picture.png");
        if (savedImage != null) {
            myImageView.setImageBitmap(savedImage);
        }
    }

        private void makePhoneCall(String phoneNumber) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
    }

    private void setCaptureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Inside your onActivityResult method
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap thumbnail = (Bitmap) data.getParcelableExtra("data");
                            if (thumbnail != null) {
                                // Call setImageBitmap on the instance of ImageButton
                                imageButton.setImageBitmap(thumbnail);
                                saveImageToInternalStorage(thumbnail, "Picture.png");
                            } else {
                                Log.e("CaptureImage", "Bitmap is null");
                            }
                        } else {
                            Log.e("CaptureImage", "Intent data is null");
                        }
                    } else {
                        Log.e("CaptureImage", "Capture result is not OK");
                    }
                });

        cameraResult.launch(cameraIntent);
    }

    private void saveImageToInternalStorage(Bitmap bitmap, String filename) {
        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromInternalStorage(String filename) {
        File file = new File(getFilesDir(), filename);
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }
}
