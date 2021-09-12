package com.example.petductivity.pet;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.petductivity.R;
import com.example.petductivity.setting_up.VerificationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

/**
 * This class encapsulates the information contained within the PetsStatusActivity class.
 *
 * @author Team Petductivity
 */
public class PetsStatusActivity extends AppCompatActivity {

    /**
     * Directory names used in database for this activity.
     */
    public static final String pokesysDir = "pokesys";
    public static final String pokeexpDir = "pokeexp";
    public static final String pokemonDir = "pokemon";

    /**
     * UI elements for the PetsStatusActivity layout.
     */
    private ImageView selectedPokemon, pokeball1, pokeball2, pokeball3;
    private TextView level, exp, pokemonName, backButton;
    private CoordinatorLayout lvlExpBorder;
    private ConstraintLayout  extBorder;
    private ProgressBar expBar;

    /**
     * Firebase Components
     */
    private FirebaseAuth fb;
    private FirebaseUser user;
    private DatabaseReference ref;

    /**
     * Temporary variables
     */
    private String selectedPokemonName;
    private Long experience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pets_status);

        //initialise variables
        setUpUI();

        //retrieve data from firebase on data change
        setUpFirebase();

        //animate the UI.
        animateUI();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                selectedPokemonName = (String) dataSnapshot.child(pokemonDir).getValue();
                experience = (Long) dataSnapshot.child(pokeexpDir).getValue();
                update();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Log.d("My Pets" ,"The read failed: " + databaseError.getCode());
            }
        });

        //set activity for button
        backButton.setOnClickListener(listener -> {
            finish();
        });

    }

    /**
     * Sets up the UI elements in the layout.
     */
    private void setUpUI() {
        this.level = findViewById(R.id.level);
        this.exp = findViewById(R.id.exp);
        this.pokemonName = findViewById(R.id.pokemonName);
        this.selectedPokemon = findViewById(R.id.selectedPokemon);
        this.backButton = findViewById(R.id.petsBackToHomePage);
        this.lvlExpBorder = findViewById(R.id.levelExpBorder);
        this.extBorder = findViewById(R.id.border);
        this.expBar = findViewById(R.id.expBar);
        this.pokeball1 = findViewById(R.id.pokeball1Pets);
        this.pokeball2 = findViewById(R.id.pokeball2Pets);
        this.pokeball3 = findViewById(R.id.pokeball3Pets);
    }

    /**
     * Sets up the firebase utilities.
     */
    private void setUpFirebase() {
        fb = FirebaseAuth.getInstance();
        user = fb.getCurrentUser();
        String onlineUserID = user.getUid();
        ref = FirebaseDatabase.getInstance(VerificationActivity.databaseURL).getReference(pokesysDir).child(onlineUserID);
    }

    /**
     * Sets relevant fields as required(levels, exp, pokemon).
     */
    private void update() {
        pokemonName.setText(selectedPokemonName.toUpperCase());
        long levelLong = (experience / 100);
        long expLong = (experience % 100);
        level.setText("" + levelLong);
        exp.setText("" + expLong + " / 100");

        //different cases based on which pokemon the user has selected
        switch(selectedPokemonName) {
            case "pikachu":
                selectedPokemon.setImageResource(R.drawable.pikachu);
                pokemonName.setTextColor(Color.parseColor("#FFD700"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#FFD700"));
                extBorder.setBackgroundColor(Color.parseColor("#BDB76B"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#B8860B"), PorterDuff.Mode.SRC_IN);
                break;
            case "charmander":
                selectedPokemon.setImageResource(R.drawable.charmander);
                pokemonName.setTextColor(Color.parseColor("#FF8C00"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#FF8C00"));
                extBorder.setBackgroundColor(Color.parseColor("#FF4500"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#8A4117"), PorterDuff.Mode.SRC_IN);
                break;
            case "charmeleon":
                selectedPokemon.setImageResource(R.drawable.charmeleon);
                pokemonName.setTextColor(Color.parseColor("#FF8C00"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#FF8C00"));
                extBorder.setBackgroundColor(Color.parseColor("#FF4500"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#8A4117"), PorterDuff.Mode.SRC_IN);
                break;
            case "charizard":
                selectedPokemon.setImageResource(R.drawable.charizard);
                pokemonName.setTextColor(Color.parseColor("#FF8C00"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#FF8C00"));
                extBorder.setBackgroundColor(Color.parseColor("#FF4500"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#8A4117"), PorterDuff.Mode.SRC_IN);
                break;
            case "squirtle":
                selectedPokemon.setImageResource(R.drawable.squirtle);
                pokemonName.setTextColor(Color.parseColor("#87CEEB"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#87CEEB"));
                extBorder.setBackgroundColor(Color.parseColor("#1E90FF"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#151B54"), PorterDuff.Mode.SRC_IN);
                break;
            case "wartortle":
                selectedPokemon.setImageResource(R.drawable.wartortle);
                pokemonName.setTextColor(Color.parseColor("#87CEEB"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#87CEEB"));
                extBorder.setBackgroundColor(Color.parseColor("#1E90FF"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#151B54"), PorterDuff.Mode.SRC_IN);
                break;
            case "blastoise":
                selectedPokemon.setImageResource(R.drawable.blastoise);
                pokemonName.setTextColor(Color.parseColor("#87CEEB"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#87CEEB"));
                extBorder.setBackgroundColor(Color.parseColor("#1E90FF"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#151B54"), PorterDuff.Mode.SRC_IN);
                break;
            case "bulbasaur":
                selectedPokemon.setImageResource(R.drawable.bulbasaur);
                pokemonName.setTextColor(Color.parseColor("#90EE90"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#90EE90"));
                extBorder.setBackgroundColor(Color.parseColor("#2E8B57"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#306754"), PorterDuff.Mode.SRC_IN);
                break;
            case "ivysaur":
                selectedPokemon.setImageResource(R.drawable.ivysaur);
                pokemonName.setTextColor(Color.parseColor("#90EE90"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#90EE90"));
                extBorder.setBackgroundColor(Color.parseColor("#2E8B57"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#306754"), PorterDuff.Mode.SRC_IN);
                break;
            case "venusaur":
                selectedPokemon.setImageResource(R.drawable.venusaur);
                pokemonName.setTextColor(Color.parseColor("#90EE90"));
                lvlExpBorder.setBackgroundColor(Color.parseColor("#90EE90"));
                extBorder.setBackgroundColor(Color.parseColor("#2E8B57"));
                expBar.getProgressDrawable().setColorFilter(Color.parseColor("#306754"), PorterDuff.Mode.SRC_IN);
                break;
        }

        updateExpBar(expLong);

    }

    /**
     * Updates the Exp Bar based on how much exp the user has accumulated.
     *
     * @param currExp The amount of exp currently owned by the user.
     */
    private void updateExpBar(long currExp) {
        expBar.setProgress((int) currExp);
    }

    /**
     * Animates the UI for the layout
     */
    private void animateUI() {
        pokeball1.setTranslationY(-1000);
        pokeball2.setTranslationY(-1000);
        pokeball3.setTranslationY(-1000);
        pokeball1.animate().translationYBy(1000).setDuration(1500);
        pokeball2.animate().translationYBy(1000).setDuration(1500);
        pokeball3.animate().translationYBy(1000).setDuration(1500);

        selectedPokemon.setTranslationX(-2000);
        selectedPokemon.animate().rotationBy(360).setDuration(2500);
        selectedPokemon.animate().translationXBy(2000).setDuration(1500);
    }

}