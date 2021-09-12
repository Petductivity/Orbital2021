package com.example.petductivity.settings;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.R;

/**
 * This encapsulates the information of the Help section under Settings.
 *
 * @author Team Petductivity
 */
public class HelpActivity extends AppCompatActivity {

    /**
     * UI element for HelpActivity layout.
     */
    private TextView navBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        navBack = findViewById(R.id.arrowClickHelp);

        navBack.setOnClickListener(v -> {
            finish();
        });
    }
}