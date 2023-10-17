package com.cst3104.androidlab4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailEditText;
    private ImageButton ImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("MainActivity", "In onCreate() - Loading Widgets");

        loginButton = findViewById(R.id.LoginButton);
        emailEditText = findViewById(R.id.emailEditText);

        loginButton.setOnClickListener(click -> {
            String email = emailEditText.getText().toString();

            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", email);
            startActivity(nextPage);

        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.w("MainActivity", "In onStart() - Loading Widgets");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "In onResume() - Loading Widgets");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "In onPause() - Loading Widgets");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("MainActivity", "In onStop() - Loading Widgets");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity", "In onDestroy() - Loading Widgets");
    }
}
