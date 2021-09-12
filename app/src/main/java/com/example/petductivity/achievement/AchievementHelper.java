package com.example.petductivity.achievement;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

/**
 * A helper class for the AchievementMainActivity class.
 *
 * @author Team Petductivity
 */
public class AchievementHelper {

    /**
     * pokemon: Contains the indices of the pokemon owned by the user.
     * shinyPokemon: Contains the indices of the shiny pokemon owned by the user.
     */
    private ArrayList<Integer> pokemon;
    private ArrayList<Integer> shinyPokemon;

    /**
     * Constructor for an instance of the class.
     * @param pokemon A list of pokemon indices.
     * @param shinyPokemon A list of shiny pokemon indices.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public AchievementHelper(ArrayList<Integer> pokemon, ArrayList<Integer> shinyPokemon) {
        this.pokemon = pokemon;
        this.shinyPokemon = shinyPokemon;
    }

    /**
     * Null constructor for an instance of the class.
     */
    public AchievementHelper() {
    }

    /**
     * Getter for the pokemon list.
     *
     * @return A list.
     */
    public ArrayList<Integer> getPokemon() {
        return pokemon;
    }

    /**
     * Setter for the pokemon list.
     *
     * @param pokemon A list of pokemon indices.
     */
    public void setPokemon(ArrayList<Integer> pokemon) {
        this.pokemon = pokemon;
    }

    /**
     * Getter for the shiny pokemon list.
     *
     * @return A list.
     */
    public ArrayList<Integer> getShinyPokemon() {
        return shinyPokemon;
    }

    /**
     * Setter for the shiny pokemon list.
     *
     * @param shinyPokemon A list of shiny pokemon indices.
     */
    public void setShinyPokemon(ArrayList<Integer> shinyPokemon) {
        this.shinyPokemon = shinyPokemon;
    }

    /**
     * Returns the size of the pokemon array.
     *
     * @return An integer indicating the size of the pokemon array.
     */
    public int pokemonSize() {
        return pokemon.size();
    }

    /**
     * Returns the size of the shiny pokemon array.
     *
     * @return An integer indicating the size of the shiny pokemon array.
     */
    public int shinyPokemonSize() {
        return shinyPokemon.size();
    }


}