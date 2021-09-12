package com.example.petductivity.achievement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class encapsulates the Achievement Activity.
 *
 * @author Team Petductivity
 */
public class AchievementMainActivity extends AppCompatActivity {
    /**
     * Directory names used in database for this activity.
     */
    public static final String achievementsDir = "achievements";
    public static final String nonShinyPokemonDir = "pokemon";
    public static final String shinyPokemonDir = "shinyPokemon";

    /**
     * UI elements for the Achievement Activity layout.
     */
    private TextView achievementBackButton;
    private RecyclerView rv;
    private ImageView badge1, badge2, helpIcon;
    private String[] names, descs, shinyDescs, color;

    /**
     * Firebase Components
     */
    private DatabaseReference reference;
    private FirebaseAuth fb;
    private FirebaseUser user;
    private String onlineUserID;

    /**
     * Array of pokemon images used.
     */
    private static final int[] images = {0, R.drawable.bulbasaur, R.drawable.ivysaur, R.drawable.venusaur,
            R.drawable.charmander, R.drawable.charmeleon, R.drawable.charizard,
            R.drawable.squirtle, R.drawable.wartortle, R.drawable.blastoise,
            R.drawable.caterpie, R.drawable.metapod, R.drawable.butterfree,
            R.drawable.weedle, R.drawable.kakuna, R.drawable.beedrill,
            R.drawable.pidgey, R.drawable.pidgeotto, R.drawable.pidgeot,
            R.drawable.rattata, R.drawable.raticate, R.drawable.spearow, R.drawable.fearow,
            R.drawable.ekans, R.drawable.arbok, R.drawable.pikachu, R.drawable.raichu,
            R.drawable.sandshrew, R.drawable.sandslash,
            R.drawable.nidoran_f, R.drawable.nidorina, R.drawable.nidoqueen,
            R.drawable.nidoran_m, R.drawable.nidorino, R.drawable.nidoking,
            R.drawable.clefairy, R.drawable.clefable, R.drawable.vulpix, R.drawable.ninetales,
            R.drawable.jigglypuff, R.drawable.wigglytuff, R.drawable.zubat, R.drawable.golbat,
            R.drawable.oddish, R.drawable.gloom, R.drawable.vileplume,
            R.drawable.paras, R.drawable.parasect, R.drawable.venonat, R.drawable.venomoth,
            R.drawable.diglett, R.drawable.dugtrio, R.drawable.meowth, R.drawable.persian,
            R.drawable.psyduck, R.drawable.golduck, R.drawable.mankey, R.drawable.primeape,
            R.drawable.growlithe, R.drawable.arcanine,
            R.drawable.poliwag, R.drawable.poliwhirl, R.drawable.poliwrath,
            R.drawable.abra, R.drawable.kadabra, R.drawable.alakazam,
            R.drawable.machop, R.drawable.machoke, R.drawable.machamp,
            R.drawable.bellsprout, R.drawable.weepinbell, R.drawable.victreebel,
            R.drawable.tentacool, R.drawable.tentacruel,
            R.drawable.geodude, R.drawable.graveler, R.drawable.golem,
            R.drawable.ponyta, R.drawable.rapidash, R.drawable.slowpoke, R.drawable.slowbro,
            R.drawable.magnemite, R.drawable.magneton, R.drawable.farfetchd,
            R.drawable.doduo, R.drawable.dodrio, R.drawable.seel, R.drawable.dewgong,
            R.drawable.grimer, R.drawable.muk, R.drawable.shellder, R.drawable.cloyster,
            R.drawable.gastly, R.drawable.haunter, R.drawable.gengar,
            R.drawable.onix, R.drawable.drowzee, R.drawable.hypno,
            R.drawable.krabby, R.drawable.kingler, R.drawable.voltorb, R.drawable.electrode,
            R.drawable.exeggcute, R.drawable.exeggutor, R.drawable.cubone, R.drawable.marowak,
            R.drawable.hitmonlee, R.drawable.hitmonchan, R.drawable.lickitung,
            R.drawable.koffing, R.drawable.weezing, R.drawable.rhyhorn, R.drawable.rhydon,
            R.drawable.chansey, R.drawable.tangela, R.drawable.kangaskhan,
            R.drawable.horsea, R.drawable.seadra, R.drawable.goldeen, R.drawable.seaking,
            R.drawable.staryu, R.drawable.starmie, R.drawable.mr_mine,
            R.drawable.scyther, R.drawable.jynx, R.drawable.electabuzz,
            R.drawable.magmar, R.drawable.pinsir, R.drawable.tauros,
            R.drawable.magikarp, R.drawable.gyarados, R.drawable.lapras, R.drawable.ditto,
            R.drawable.eevee, R.drawable.vaporeon, R.drawable.jolteon, R.drawable.flareon,
            R.drawable.porygon, R.drawable.omanyte, R.drawable.omastar,
            R.drawable.kabuto, R.drawable.kabutops, R.drawable.aerodactyl, R.drawable.snorlax,
            R.drawable.articuno, R.drawable.zapdos, R.drawable.moltres,
            R.drawable.dratini, R.drawable.dragonair, R.drawable.dragonite,
            R.drawable.mewtwo, R.drawable.mew};

    /**
     * Array of shiny pokemon images used.
     */
    private static final int[] shinyImages = {0, R.drawable.bulbasaur_shiny, R.drawable.ivysaur_shiny, R.drawable.venusaur_shiny,
            R.drawable.charmander_shiny, R.drawable.charmeleon_shiny, R.drawable.charizard_shiny,
            R.drawable.squirtle_shiny, R.drawable.wartortle_shiny, R.drawable.blastoise_shiny,
            R.drawable.caterpie_shiny, R.drawable.metapod_shiny, R.drawable.butterfree_shiny,
            R.drawable.weedle_shiny, R.drawable.kakuna_shiny, R.drawable.beedrill_shiny,
            R.drawable.pidgey_shiny, R.drawable.pidgeotto_shiny, R.drawable.pidgeot_shiny,
            R.drawable.rattata_shiny, R.drawable.raticate_shiny, R.drawable.spearow_shiny, R.drawable.fearow_shiny,
            R.drawable.ekans_shiny, R.drawable.arbok_shiny, R.drawable.pikachu_shiny, R.drawable.raichu_shiny,
            R.drawable.sandshrew_shiny, R.drawable.sandslash_shiny,
            R.drawable.nidoran_f_shiny, R.drawable.nidorina_shiny, R.drawable.nidoqueen_shiny,
            R.drawable.nidoran_m_shiny, R.drawable.nidorino_shiny, R.drawable.nidoking_shiny,
            R.drawable.clefairy_shiny, R.drawable.clefable_shiny, R.drawable.vulpix_shiny, R.drawable.ninetales_shiny,
            R.drawable.jigglypuff_shiny, R.drawable.wigglytuff_shiny, R.drawable.zubat_shiny, R.drawable.golbat_shiny,
            R.drawable.oddish_shiny, R.drawable.gloom_shiny, R.drawable.vileplume_shiny,
            R.drawable.paras_shiny, R.drawable.parasect_shiny, R.drawable.venonat_shiny, R.drawable.venomoth_shiny,
            R.drawable.diglett_shiny, R.drawable.dugtrio_shiny, R.drawable.meowth_shiny, R.drawable.persian_shiny,
            R.drawable.psyduck_shiny, R.drawable.golduck_shiny, R.drawable.mankey_shiny, R.drawable.primeape_shiny,
            R.drawable.growlithe_shiny, R.drawable.arcanine_shiny,
            R.drawable.poliwag_shiny, R.drawable.poliwhirl_shiny, R.drawable.poliwrath_shiny,
            R.drawable.abra_shiny, R.drawable.kadabra_shiny, R.drawable.alakazam_shiny,
            R.drawable.machop_shiny, R.drawable.machoke_shiny, R.drawable.machamp_shiny,
            R.drawable.bellsprout_shiny, R.drawable.weepinbell_shiny, R.drawable.victreebel_shiny,
            R.drawable.tentacool_shiny, R.drawable.tentacruel_shiny,
            R.drawable.geodude_shiny, R.drawable.graveler_shiny, R.drawable.golem_shiny,
            R.drawable.ponyta_shiny, R.drawable.rapidash_shiny, R.drawable.slowpoke_shiny, R.drawable.slowbro_shiny,
            R.drawable.magnemite_shiny, R.drawable.magneton_shiny, R.drawable.farfetchd_shiny,
            R.drawable.doduo_shiny, R.drawable.dodrio_shiny, R.drawable.seel_shiny, R.drawable.dewgong_shiny,
            R.drawable.grimer_shiny, R.drawable.muk_shiny, R.drawable.shellder_shiny, R.drawable.cloyster_shiny,
            R.drawable.gastly_shiny, R.drawable.haunter_shiny, R.drawable.gengar_shiny,
            R.drawable.onix_shiny, R.drawable.drowzee_shiny, R.drawable.hypno_shiny,
            R.drawable.krabby_shiny, R.drawable.kingler_shiny, R.drawable.voltorb_shiny, R.drawable.electrode_shiny,
            R.drawable.exeggcute_shiny, R.drawable.exeggutor_shiny, R.drawable.cubone_shiny, R.drawable.marowak_shiny,
            R.drawable.hitmonlee_shiny, R.drawable.hitmonchan_shiny, R.drawable.lickitung_shiny,
            R.drawable.koffing_shiny, R.drawable.weezing_shiny, R.drawable.rhyhorn_shiny, R.drawable.rhydon_shiny,
            R.drawable.chansey_shiny, R.drawable.tangela_shiny, R.drawable.kangaskhan_shiny,
            R.drawable.horsea_shiny, R.drawable.seadra_shiny, R.drawable.goldeen_shiny, R.drawable.seaking_shiny,
            R.drawable.staryu_shiny, R.drawable.starmie_shiny, R.drawable.mr_mine_shiny,
            R.drawable.scyther_shiny, R.drawable.jynx_shiny, R.drawable.electabuzz_shiny,
            R.drawable.magmar_shiny, R.drawable.pinsir_shiny, R.drawable.tauros_shiny,
            R.drawable.magikarp_shiny, R.drawable.gyarados_shiny, R.drawable.lapras_shiny, R.drawable.ditto_shiny,
            R.drawable.eevee_shiny, R.drawable.vaporeon_shiny, R.drawable.jolteon_shiny, R.drawable.flareon_shiny,
            R.drawable.porygon_shiny, R.drawable.omanyte_shiny, R.drawable.omastar_shiny,
            R.drawable.kabuto_shiny, R.drawable.kabutops_shiny, R.drawable.aerodactyl_shiny, R.drawable.snorlax_shiny,
            R.drawable.articuno_shiny, R.drawable.zapdos_shiny, R.drawable.moltres_shiny,
            R.drawable.dratini_shiny, R.drawable.dragonair_shiny, R.drawable.dragonite_shiny,
            R.drawable.mewtwo_shiny, R.drawable.mew_shiny};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_main);

        setUpFireBase();
        setUpUI();
        animateUI();


        //set back button
        achievementBackButton.setOnClickListener(listener -> {
            finish();
        });

        //set up the help icon
        helpIcon.setOnClickListener(v -> setUpHelpPopUp());

        //set up recyclerview, retrieve data from firebase first
        //achievements stored as a helper class containing 2 arraylists.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                AchievementHelper helper = dataSnapshot.getValue(AchievementHelper.class);
                int pokemonSize = helper.pokemonSize();
                int shinyPokemonSize = helper.shinyPokemonSize();
                int size = pokemonSize + shinyPokemonSize - 2;

                ArrayList<Integer> sortedPokemonList = helper.getPokemon();
                ArrayList<Integer> sortedShinyPokemonList = helper.getShinyPokemon();

                // sort pokemon badges by their index
                quicksort(sortedPokemonList);
                quicksort(sortedShinyPokemonList);

                String[] pokemonArray = new String[size];
                String[] descsArray = new String[size];
                String[] colorArray = new String[size + 1];
                int[] imageArray = new int[size];

                for (int x = 0; x < pokemonSize - 1; x++) {
                    int pokemonIndex = sortedPokemonList.get(x + 1);
                    pokemonArray[x] = names[pokemonIndex];
                    descsArray[x] = descs[pokemonIndex];
                    colorArray[x] = color[pokemonIndex];
                    imageArray[x] = images[pokemonIndex];
                }

                for (int y = 0; y < shinyPokemonSize - 1; y++) {
                    int pokemonIndex = sortedShinyPokemonList.get(y + 1);
                    pokemonArray[y + pokemonSize - 1] = names[pokemonIndex];
                    descsArray[y + pokemonSize - 1] = shinyDescs[pokemonIndex];
                    colorArray[y + pokemonSize - 1] = color[pokemonIndex];
                    imageArray[y + pokemonSize - 1] = shinyImages[pokemonIndex];
                }

                setAdapter(pokemonArray, descsArray, colorArray, imageArray);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Log.d("Achievements", "" + databaseError);
            }
        });
    }

    /**
     * Set up the UI elements used for this layout.
     */
    private void setUpUI() {
        achievementBackButton = findViewById(R.id.achievementBackToHomePage);
        names = getResources().getStringArray(R.array.achievement_names);
        descs = getResources().getStringArray(R.array.achievement_descriptions);
        shinyDescs = getResources().getStringArray(R.array.achievement_descriptions_shiny);
        color = getResources().getStringArray(R.array.achievement_colors);
        rv = findViewById(R.id.achievementRecyclerView);
        badge1 = findViewById(R.id.achievementBadge1);
        badge2 = findViewById(R.id.achievementBadge2);
        helpIcon = findViewById(R.id.achievementHelp);
    }

    /**
     * Setup Firebase.
     */
    private void setUpFireBase() {
        fb = FirebaseAuth.getInstance();
        user = fb.getCurrentUser();
        onlineUserID = user.getUid();
        reference = FirebaseDatabase.getInstance(VerificationActivity.databaseURL).getReference().child(achievementsDir).child(onlineUserID);
    }

    /**
     * Setup adapter for recyclerview used in this activity's layout.
     *
     * @param pokemonArray String array containing the names of pokemon used for the badges.
     * @param descsArray String array containing the descriptions of pokemon used for the badges.
     * @param colorArray String array containing the colors of pokemon used for the badges.
     * @param imageArray int array containing the images of pokemon used for the badges.
     */
    private void setAdapter(String[] pokemonArray, String[] descsArray, String[] colorArray, int[] imageArray) {
        AchievementAdapter adapter = new AchievementAdapter(this, pokemonArray, descsArray, colorArray, imageArray);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Set animation for the UI elements in this layout.
     */
    private void animateUI() {
        badge1.setTranslationX(-1500);
        badge2.setTranslationX(1500);
        badge1.animate().translationXBy(1500).setDuration(1500);
        badge2.animate().translationXBy(-1500).setDuration(1500);
    }


    /**
     * Initialises the lists of badges for the new user account.
     *
     * @param currUser The UID of the user logged-in/registered.
     */
    public static void setUpAchievements(String currUser) {
        DatabaseReference rref = FirebaseDatabase.getInstance(VerificationActivity.databaseURL).getReference().child(achievementsDir).child(currUser);

        ArrayList<Integer> initialList = new ArrayList<>();
        initialList.add(0);

        rref.child(nonShinyPokemonDir).setValue(initialList).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("Achievement", "Non-shiny SetUp: Success");
            } else {
                Log.d("Achievement", "Non-shiny Setup Failed: " + task.getException().toString());
            }
        });

        rref.child(shinyPokemonDir).setValue(initialList).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("Achievement", "Shiny SetUp: Success");
            } else {
                Log.d("Achievement", "Shiny Setup Failed: " + task.getException().toString());
            }
        });
    }


    /**
     * Add a new badge to the achievement directory.
     * 1 random badge will be given when user completes a task / unlock focus mode
     * Shiny rate: 3%, non-shiny rate: 97%
     * Equal chance of getting starters, common, legendary pokemon
     *
     * @param currUser The UID of the current user.
     * @param context The context used for this execution.
     * @param viewId The view used for this execution.
     * @param duration The duration of the task completed
     */
    public static void giveAchievement(String currUser, Context context, int viewId, long duration) {
        long minDuration = 30;
        if (duration < minDuration) { return; }

        // sync database.
        DatabaseReference rref = FirebaseDatabase.getInstance(VerificationActivity.databaseURL).getReference().child(achievementsDir).child(currUser);

        rref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                AchievementHelper helper = snapshot.getValue(AchievementHelper.class);
                //set up the popup view when achievement is unlocked.
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(viewId, null);
                ImageView pokemonGotten = view.findViewById(R.id.AwardIV);

                Random rand = new Random();
                int p = rand.nextInt(100);
                int toGive = rand.nextInt(151) + 1;

                // shiny
                if (p <= 2) {
                    pokemonGotten.setImageResource(shinyImages[toGive]);
                    ArrayList<Integer> shiny = helper.getShinyPokemon();
                    if (!shiny.contains(toGive)) {
                        shiny.add(toGive);
                        rref.child(shinyPokemonDir).setValue(shiny);
                    }
                }

                // non-shiny
                else {
                    pokemonGotten.setImageResource(images[toGive]);
                    ArrayList<Integer> nonShiny = helper.getPokemon();
                    if (!nonShiny.contains(toGive)) {
                        nonShiny.add(toGive);
                        rref.child(nonShinyPokemonDir).setValue(nonShiny);
                    }
                }

                alert.setNegativeButton("Dismiss", null)
                        .setView(view)
                        .create()
                        .show();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Achievements", "Failed to give achievement: " + error);
            }
        });
    }

    /**
     * Sort the list of achievements that the user has.
     *
     * @param list The list of achievements that the user has.
     */
    public static void quicksort(List<Integer> list) {
        sortie(list, 0, list.size() - 1);
    }

    /**
     * Helper function for quicksort.
     *
     * @param list The list to sort.
     * @param start Starting index.
     * @param end Ending index.
     */
    public static void sortie(List<Integer> list, int start, int end) {
        if (start < end) {
            int left = start + 1;
            int right = end;
            int startValue = list.get(start);
            while (left <= right) {
                while (left <= end && startValue >= list.get(left)) {
                    left++;
                }
                while (right > start && startValue < list.get(right)) {
                    right--;
                }
                if (left < right) {
                    Collections.swap(list, left, right);
                }
            }
            Collections.swap(list, start, left - 1);
            sortie(list, start, right - 1);
            sortie(list, right + 1, end);
        }
    }

    /**
     * Set up the UI elements used for the help popup.
     */
    private void setUpHelpPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.achievement_help, null);


        alert.setNegativeButton("Dismiss", null)
                .setView(view) // insert the layout into the dialog
                .create()
                .show();
    }
}