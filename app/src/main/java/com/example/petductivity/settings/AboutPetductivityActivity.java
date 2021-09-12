package com.example.petductivity.settings;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.R;

/**
 * This class contains the actions when the "About Petductivity" activity is started.
 *
 * @author Team Petductivity
 */
public class AboutPetductivityActivity extends AppCompatActivity {

    /**
     * navBack: The TextView that brings the user back to the Settings page when clicked.
     */
    private TextView navBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_petductivity);

        navBack = findViewById(R.id.arrowClickAboutPetductivity);

        navBack.setOnClickListener(listener -> {
            finish();
        });
    }
}