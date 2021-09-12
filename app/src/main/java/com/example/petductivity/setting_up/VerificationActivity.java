package com.example.petductivity.setting_up;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.R;
import com.example.petductivity.achievement.AchievementMainActivity;
import com.example.petductivity.focus_mode.FocusModeActivity;
import com.example.petductivity.pet.PetsStatusActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This encapsulates the information of the Verification Activity.
 *
 * @author Team Petductivity
 */
public class VerificationActivity extends AppCompatActivity {
    /**
     * Directory names used in database for this activity.
     */
    public static String databaseURL = "https://petductivity-v2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    public static final String nameDir = "name";

    /**
     * UI elements for the VerificationActivity layout.
     */
    private TextView goBack, choosePoke;
    private ImageView bulbasaur, charmander, squirtle, pikachu;

    /**
     * Firebase Components
     */
    private FirebaseAuth fb;
    private DatabaseReference ref;
    private FirebaseUser currUser;
    private String onlineUserID;
    private final String selectedUsername = RegisterActivity.retreiveUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        // setup UI for starter selection
        setUpStarters();

        // connects to the firebase for this app
        setUpFirebase();

        // check if the user has logged in / registered
        if (fb.getCurrentUser() == null) {
            // bring user to login page
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        // go back line pressed
        goBack.setOnClickListener(listener -> {
            // notify the user that he has pressed the line
            goBack.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            // sign the user out
            FirebaseAuth.getInstance().signOut();

            bringToLoginPage();
        });

        // action that follows after the 4 pokeballs are clicked
        bulbasaur.setOnClickListener(listener -> setUpBulbasaurPopUp());
        charmander.setOnClickListener(listener -> setUpCharmanderPopUp());
        squirtle.setOnClickListener(listener -> setUpSquirtlePopUp());
        pikachu.setOnClickListener(listener -> setUpPikachuPopUp());
    }

    /**
     * Setup Firebase.
     */
    private void setUpFirebase() {
        fb = FirebaseAuth.getInstance();
        currUser = fb.getCurrentUser();
        onlineUserID = currUser.getUid();
        ref = FirebaseDatabase.getInstance(databaseURL).getReference().child(PetsStatusActivity.pokesysDir).child(onlineUserID);
    }

    /**
     * Setup the directory in firebase.
     */
    private void setUpNameAndExperienceDirectory() {
        ref.child(PetsStatusActivity.pokeexpDir).setValue(100);
        ref.child(nameDir).setValue(selectedUsername);
    }

    /**
     * Setup the UI for choosing starter pokemon portion.
     */
    private void setUpStarters() {
        goBack = findViewById(R.id.goBackTextView);
        choosePoke = findViewById(R.id.choosePokemonTV);
        bulbasaur = findViewById(R.id.pokeballBulbIV);
        charmander = findViewById(R.id.pokeballCharIV);
        squirtle = findViewById(R.id.pokeballSquirIV);
        pikachu = findViewById(R.id.pokeballPikaIV);
    }

    /**
     * Setup the UI for the popup for selection of the starters
     */
    private void setUpBulbasaurPopUp() {
        AlertDialog.Builder bulbAlert = new AlertDialog.Builder(VerificationActivity.this);
        LayoutInflater bulbInflater = LayoutInflater.from(VerificationActivity.this);
        View bulbView = bulbInflater.inflate(R.layout.select_bulbasaur, null);

        bulbAlert.setTitle("Select Bulbasaur as your Pokemon?")
                .setMessage("Pokemon cannot be changed after selection!")
                .setPositiveButton("Yes", (dialog, which) -> {
                    ref.child(PetsStatusActivity.pokemonDir).setValue("bulbasaur").addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerificationActivity.this, "Bulbasaur Selected!", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();
                            AchievementMainActivity.setUpAchievements(onlineUserID);
                            setUpNameAndExperienceDirectory();
                            FocusModeActivity.initialiseFocusMode(onlineUserID);
                        } else {
                            Toast.makeText(VerificationActivity.this, "Unexpected Error Occurred!\nPlease Try Again!", Toast.LENGTH_SHORT).show();
                            Log.d("Verification", "Unable to select pokemon" + task.getException().toString());
                        }
                    });

                })
                .setNegativeButton("Cancel", null)
                .setView(bulbView) // insert the layout into the dialog
                .create()
                .show();
    }

    /**
     * Setup the UI for the popup for selection of the starters
     */
    private void setUpCharmanderPopUp() {
        AlertDialog.Builder charAlert = new AlertDialog.Builder(VerificationActivity.this);
        LayoutInflater charInflater = LayoutInflater.from(VerificationActivity.this);
        View charView = charInflater.inflate(R.layout.select_charmander, null);

        charAlert.setTitle("Select Charmander as your Pokemon?")
                .setMessage("Pokemon cannot be changed after selection!")
                .setPositiveButton("Yes", (dialog, which) -> {
                    ref.child(PetsStatusActivity.pokemonDir).setValue("charmander").addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerificationActivity.this, "Charmander Selected!", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();
                            AchievementMainActivity.setUpAchievements(onlineUserID);
                            setUpNameAndExperienceDirectory();
                            FocusModeActivity.initialiseFocusMode(onlineUserID);
                        } else {
                            Toast.makeText(VerificationActivity.this, "Unexpected Error Occurred!\nPlease Try Again!", Toast.LENGTH_SHORT).show();
                            Log.d("Verificaton", "Unable to select pokemon" + task.getException().toString());
                        }
                    });

                })
                .setNegativeButton("Cancel", null)
                .setView(charView) // insert the layout into the dialog
                .create()
                .show();
    }

    /**
     * Setup the UI for the popup for selection of the starters
     */
    private void setUpSquirtlePopUp() {
        AlertDialog.Builder squirAlert = new AlertDialog.Builder(VerificationActivity.this);
        LayoutInflater squirInflater = LayoutInflater.from(VerificationActivity.this);
        View squirView = squirInflater.inflate(R.layout.select_squirtle, null);

        squirAlert.setTitle("Select Squirtle as your Pokemon?")
                .setMessage("Pokemon cannot be changed after selection!")
                .setPositiveButton("Yes", (dialog, which) -> {
                    ref.child(PetsStatusActivity.pokemonDir).setValue("squirtle").addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerificationActivity.this, "Squirtle Selected!", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();
                            AchievementMainActivity.setUpAchievements(onlineUserID);
                            setUpNameAndExperienceDirectory();
                            FocusModeActivity.initialiseFocusMode(onlineUserID);
                        } else {
                            Toast.makeText(VerificationActivity.this, "Unexpected Error Occurred!\nPlease Try Again!", Toast.LENGTH_SHORT).show();
                            Log.d("Verificaton", "Unable to select pokemon" + task.getException().toString());
                        }
                    });

                })
                .setNegativeButton("Cancel", null)
                .setView(squirView) // insert the layout into the dialog
                .create()
                .show();
    }

    /**
     * Setup the UI for the popup for selection of the starters
     */
    private void setUpPikachuPopUp() {
        AlertDialog.Builder pikaAlert = new AlertDialog.Builder(VerificationActivity.this);
        LayoutInflater pikaInflater = LayoutInflater.from(VerificationActivity.this);
        View pikaView = pikaInflater.inflate(R.layout.select_pikachu, null);

        pikaAlert.setTitle("Select Pikachu as your Pokemon?")
                .setMessage("Pokemon cannot be changed after selection!")
                .setPositiveButton("Yes", (dialog, which) -> {
                    ref.child(PetsStatusActivity.pokemonDir).setValue("pikachu").addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerificationActivity.this, "Pikachu Selected!", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();
                            AchievementMainActivity.setUpAchievements(onlineUserID);
                            setUpNameAndExperienceDirectory();
                            FocusModeActivity.initialiseFocusMode(onlineUserID);
                        } else {
                            Toast.makeText(VerificationActivity.this, "Unexpected Error Occurred!\nPlease Try Again!", Toast.LENGTH_SHORT).show();
                            Log.d("Verificaton", "Unable to select pokemon" + task.getException().toString());
                        }
                    });

                })
                .setNegativeButton("Cancel", null)
                .setView(pikaView) // insert the layout into the dialog
                .create()
                .show();
    }

    /**
     * Get the database to send an email for user to verify his account
     */
    private void sendVerificationEmail() {
        fb.getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(VerificationActivity.this, "Verification Email Sent!", Toast.LENGTH_SHORT).show();

                // sign user out since user brought back to login page
                FirebaseAuth.getInstance().signOut();
                bringToLoginPage();
            } else {
                Toast.makeText(VerificationActivity.this, "Unexpected Error Occurred!\nPlease Login Again!", Toast.LENGTH_SHORT).show();
                Log.d("Verification", "Unable to send verification email: " + task.getException().toString());
            }
        });
    }

    /**
     * Bring user back to login page after verification is done / if user wants to.
     */
    private void bringToLoginPage() {
        // bring user back to login page
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }


}