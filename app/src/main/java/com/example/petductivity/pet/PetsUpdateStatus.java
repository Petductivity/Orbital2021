package com.example.petductivity.pet;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.petductivity.setting_up.VerificationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * This class contains methods for updating the Pets Status of users.
 *
 * @author Team Petductivity
 */
public class PetsUpdateStatus {

    /**
     * Set up the firebase reference to be used.
     */
    private static DatabaseReference ref = FirebaseDatabase.getInstance(VerificationActivity.databaseURL).getReference(PetsStatusActivity.pokesysDir);

    /**
     * Lists of pokemon that can undergo first and second evolution.
     */
    private static List<String> firstEvolution = Arrays.asList("charmander", "bulbasaur", "squirtle");
    private static List<String> secondEvolution = Arrays.asList("charmeleon", "ivysaur", "wartortle");

    /**
     * Fixed variables indicating amount of exp required for each stage of evolution.
     */
    private static int minExpForFirstEvolution = 3000;
    private static int minExpForSecondEvolution = 6000;

    /**
     * Fixed variables indicating the minimium duration required for exp to be awarded and the exp rate
     */
    private static long minDurationRequired = 30;
    private static long ratePerDurationRequired = 10;


    /**
     * Sets a user's exp to a certain value.
     *
     * @param num The exp value to be set.
     * @param userID The userID whose exp is to be set.
     */
    public static void setExperience(long num, String userID) {
        ref.child(userID).child(PetsStatusActivity.pokeexpDir).setValue(num);
    }

    /**
     * Set a user's pokemon to a certain pokemon.
     *
     * @param pokemon The pokemon to be set to.
     * @param userID The userID whose pokemon is to be set.
     */
    public static void setPokemon(String pokemon, String userID) {
        ref.child(userID).child(PetsStatusActivity.pokemonDir).setValue(pokemon);
    }

    /**
     * Check whether the current pokemon can still evolve
     *
     * @param pokemon The pokemon to be checked.
     * @return A boolean indicating the result.
     */
    public static boolean hasEvolution(String pokemon) {
        return firstEvolution.contains(pokemon) || secondEvolution.contains(pokemon);
    }

    /**
     * Check which evolution will A pokemon evolve to. Returns null if a evolution does not exist.
     *
     * @param pokemon The pokemon to be checked.
     * @return A String indicating the evolution.
     */
    public static String getEvolution(String pokemon) {
        switch(pokemon) {
            case "bulbasaur":
                return "ivysaur";
            case "ivysaur":
                return "venusaur";
            case "charmander":
                return "charmeleon";
            case "charmeleon":
                return "charizard";
            case "squirtle":
                return "wartortle";
            case "wartortle":
                return "blastoise";
            default:
                return null;
        }
    }

    /**
     * Checks and updates the evolution of a user's pokemon. If a pokemon can be evolved, it will automatically be evolved to the next stage.
     *
     * @param pokemon The pokemon of the user.
     * @param exp The current amount of exp possessed by the user.
     * @param currUser The userID of the current user.
     */
    public static void checkAndUpdateEvolution(String pokemon, long exp, String currUser) {
        if (!hasEvolution(pokemon)) { return; }

        if (exp >= minExpForFirstEvolution && exp < minExpForSecondEvolution && firstEvolution.contains(pokemon)) {
            String nextEvol = getEvolution(pokemon);
            setPokemon(nextEvol, currUser);
        }

        if (exp >= minExpForSecondEvolution && secondEvolution.contains(pokemon)) {
            String nextEvol = getEvolution(pokemon);
            setPokemon(nextEvol, currUser);
        }
    }

    /**
     * Updates the exp of the user in the database.
     *
     * @param exp The amount of exp to be added.
     * @param currUser The userID of the current user.
     */
    public static void updateExp(long exp, String currUser) {

        ref.child(currUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String pokemon = (String) snapshot.child(PetsStatusActivity.pokemonDir).getValue();
                long oldExp = (long) snapshot.child(PetsStatusActivity.pokeexpDir).getValue();
                long newExp = exp + oldExp;

                checkAndUpdateEvolution(pokemon, newExp, currUser);
                setExperience(newExp, currUser);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Update pokesys", "Failed: " + error.getMessage());
            }
        });
    }

    /**
     * Determines the amount of exp to give after user has completed a task.
     * Current Rate: 10 exp per 30 minutes of task.
     *
     * @param duration The duration of the task completed.
     * @return A long indicating the amount of exp to be awarded.
     */
    public static long expAwarded(int duration) {
        if (duration < minDurationRequired) {
            return 0;
        }

        long scaling = duration / minDurationRequired;
        long expReceived = scaling * ratePerDurationRequired;

        return expReceived;
    }

}