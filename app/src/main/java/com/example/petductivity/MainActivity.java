package com.example.petductivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.achievement.AchievementMainActivity;
import com.example.petductivity.leaderboard.LeaderboardMainActivity;
import com.example.petductivity.pet.PetsStatusActivity;
import com.example.petductivity.planner.PlannerMainActivity;
import com.example.petductivity.settings.SettingActivity;
import com.example.petductivity.task.TodoMainActivity;

/**
 * This class encapsulates the information about the various features of this app.
 *
 * @author Team Petductivity
 */
public class MainActivity extends AppCompatActivity {
    /**
     * UI elements for MainActivity layout.
     */
    private ImageView plannerBtn, taskBtn, settingsBtn, petBtn, badgesBtn, leaderboardBtn;
    private ImageView pokeball, pokeball1, pokeball2, pokeball3;
    private TextView welcomeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup UI for this activity
        setUpUI();
        animateUI();

        // 2-week planner selected
        plannerBtn.setOnClickListener(listener -> {
            // bring user to 2-week planner dashboard
            startActivity(new Intent(getApplicationContext(), PlannerMainActivity.class));
        });

        taskBtn.setOnClickListener(listener -> {
            // sent the user back to the login page
            startActivity(new Intent(getApplicationContext(), TodoMainActivity.class));
        });

        settingsBtn.setOnClickListener(listener -> {
            // sent the user back to the login page
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));
        });

        petBtn.setOnClickListener(listener -> {
            // sent the user back to the login page
            startActivity(new Intent(getApplicationContext(), PetsStatusActivity.class));
        });

        badgesBtn.setOnClickListener(listener -> {
            // sent the user back to the login page
            startActivity(new Intent(getApplicationContext(), AchievementMainActivity.class));
        });

        leaderboardBtn.setOnClickListener(listener -> {
            startActivity(new Intent(getApplicationContext(), LeaderboardMainActivity.class));
        });
    }

    /**
     * Set up the UI elements used for this layout.
     */
    private void setUpUI() {
        plannerBtn = findViewById(R.id.plannerImageView);
        taskBtn = findViewById(R.id.taskImageView);
        settingsBtn = findViewById(R.id.settingsImageView);
        petBtn = findViewById(R.id.petsImageView);
        badgesBtn = findViewById(R.id.achievementsImageView);
        leaderboardBtn = findViewById(R.id.leaderboardImageView);
        pokeball = findViewById(R.id.pokeballMain);
        pokeball1 = findViewById(R.id.ballImageView);
        pokeball2 = findViewById(R.id.ballTwoImageView);
        pokeball3 = findViewById(R.id.ballThreeImageView);
        welcomeMsg = findViewById(R.id.welcomeHomeTextView);
    }

    /**
     * Animate the UI elements used for this layout.
     */
    private void animateUI() {
        pokeball1.setTranslationY(-1500);
        pokeball2.setTranslationY(-1500);
        pokeball3.setTranslationY(-1500);
        pokeball1.animate().translationYBy(1500).setDuration(1500);
        pokeball2.animate().translationYBy(1500).setDuration(1500);
        pokeball3.animate().translationYBy(1500).setDuration(1500);
        pokeball.animate().rotationBy(360).setDuration(1200);

        welcomeMsg.setAlpha(0);
        welcomeMsg.animate().alpha(1).setDuration(1500);
    }

}