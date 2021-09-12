package com.example.petductivity.leaderboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petductivity.R;
import com.example.petductivity.setting_up.VerificationActivity;
import com.example.petductivity.pet.PetsStatusActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

/**
 * A class encapsulating the actions within the Leaderboard Activity.
 *
 * @author Team Petductivity
 */
public class LeaderboardMainActivity extends AppCompatActivity {

    /**
     * UI elements for the LeaderboardMainActivity layout.
     */
    private TextView navBackBtn;
    private RecyclerView rv;
    private ImageView medal1, medal2;

    /**
     * Firebase Components
     */
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_main);

        //set up the various components
        setUpUI();
        animateUI();
        setUpFireBase();

        navBackBtn.setOnClickListener(listener -> {
            finish();
        });

        //
        reference.orderByChild(PetsStatusActivity.pokeexpDir).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //define and retrieve data from firebase
                int userNumber = (int) snapshot.getChildrenCount();
                String[] names = new String[userNumber];
                int[] images = new int[userNumber];
                long[] levels = new long[userNumber];
                int count = userNumber;


                //set up the recyclerview and adapter
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    names[count - 1] = (String) userSnapshot.child(VerificationActivity.nameDir).getValue();
                    String pokemon = (String) userSnapshot.child(PetsStatusActivity.pokemonDir).getValue();
                    switch(pokemon) {
                        case("bulbasaur"):
                            images[count - 1] = R.drawable.bulbasaur;
                            break;
                        case("ivysaur"):
                            images[count - 1] = R.drawable.ivysaur;
                            break;
                        case("venusaur"):
                            images[count - 1] = R.drawable.venusaur;
                            break;
                        case("squirtle"):
                            images[count - 1] = R.drawable.squirtle;
                            break;
                        case("wartortle"):
                            images[count - 1] = R.drawable.wartortle;
                            break;
                        case("blastoise"):
                            images[count - 1] = R.drawable.blastoise;
                            break;
                        case("charmander"):
                            images[count - 1] = R.drawable.charmander;
                            break;
                        case("charmeleon"):
                            images[count - 1] = R.drawable.charmeleon;
                            break;
                        case("charizard"):
                            images[count - 1] = R.drawable.charizard;
                            break;
                        case("pikachu"):
                            images[count - 1] = R.drawable.pikachu;
                    }
                    levels[count - 1] = ((Long) userSnapshot.child("pokeexp").getValue()) / 100;
                    count -= 1;
                }
                setAdapter(names, images, levels);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Leaderboard", "" + error);
            }
        });


    }

    /**
     * Set up the adapter used for the recyclerview.
     *
     * @param nameArray The array of names to be used.
     * @param imageArray The array images to be used.
     * @param levelArray The array of levels to be used.
     */
    private void setAdapter(String[] nameArray, int[] imageArray, long[] levelArray) {
        LeaderboardAdapter adapter = new LeaderboardAdapter(this, nameArray, imageArray, levelArray);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Sets up the UI elements.
     */
    private void setUpUI() {
        navBackBtn = findViewById(R.id.leaderboardBackToHomePage);
        rv = findViewById(R.id.leaderboardRecyclerView);
        medal1 = findViewById(R.id.medal1);
        medal2 = findViewById(R.id.medal2);
    }

    /**
     * Sets up the firebase reference.
     */
    private void setUpFireBase() {
        reference = FirebaseDatabase.getInstance(VerificationActivity.databaseURL).getReference().child(PetsStatusActivity.pokesysDir);
    }

    /**
     * Animates the UI elements.
     */
    private void animateUI() {
        medal1.setTranslationX(-1500);
        medal2.setTranslationX(1500);
        medal1.animate().translationXBy(1500).setDuration(1500);
        medal2.animate().translationXBy(-1500).setDuration(1500);
    }
}
