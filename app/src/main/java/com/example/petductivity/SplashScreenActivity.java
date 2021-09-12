package com.example.petductivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.setting_up.LoginActivity;

/**
 * This encapsulates the information of the Splashscreen Activity.
 *
 * @author Team Petductivity
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);

        // bring user to login page after 3 secs
        startActivity(intent);
        finish();
    }
}