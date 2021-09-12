package com.example.petductivity.leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petductivity.R;

import org.jetbrains.annotations.NotNull;

/**
 * An adapter for the recyclerview used in the layout of the LeaderboardMainActivity.
 *
 * @author Team Petductivity
 */
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    /**
     * names: The array containing the user names to be displayed.
     * images: The array containing the images to be displayed.
     * levels: The array containing the levels to be displayed.
     * context: The current context where the adapter is operating.
     */
    private final String[] names;
    private final int[] images;
    private final long[] levels;
    private final Context context;

    /**
     * A nested class that describes the item views and metadata of each row of the RecyclerView.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * UI elements in the row.
         */
        private final TextView rank;
        private final TextView name;
        private final TextView level;
        private final ImageView image;
        private final ConstraintLayout bg;

        /**
         * Constructor for a class instance.
         *
         * @param itemView The view used.
         */
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.leaderboardRankView);
            name = itemView.findViewById(R.id.leaderboardRowTextViewNames);
            level = itemView.findViewById(R.id.leaderboardLevelView);
            image = itemView.findViewById(R.id.leaderboardRowImageView);
            bg = itemView.findViewById(R.id.leaderboardBg);
        }
    }

    /**
     * Constructor for an instance of the adapter.
     *
     * @param context The current context where the adapter is operating.
     * @param names The array containing the user names to be displayed.
     * @param images The array containing the images to be displayed.
     * @param levels The array containing the levels to be displayed.
     */
    public LeaderboardAdapter(Context context, String[] names, int[] images, long[] levels) {
        this.names = names;
        this.images = images;
        this.context = context;
        this.levels = levels;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.leaderboard_row, parent, false);

        return new LeaderboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        int rank = 1 + position;
        holder.rank.setText(String.valueOf(rank));
        topUsers(rank, holder.bg);
        holder.image.setImageResource(images[position]);
        holder.name.setText(names[position]);
        long level = levels[position];
        holder.level.setText("Level " + level);
    }

    /**
     * Returns the number of elements to be displayed.
     *
     * @return An integer.
     */
    @Override
    public int getItemCount() {
        return names.length;
    }

    /**
     * Sets background colors for the top 5 users in the leaderboard.
     *
     * @param rank The rank of the user on the leaderboard.
     * @param layout The layout used.
     */
    private void topUsers(int rank, ConstraintLayout layout) {
        switch(rank) {
            case 1:
                layout.setBackgroundColor(Color.parseColor("#EDE275"));
                break;
            case 2:
                layout.setBackgroundColor(Color.parseColor("#C0C0C0"));
                break;
            case 3:
                layout.setBackgroundColor(Color.parseColor("#C19A6B"));
                break;
            case 4:
                layout.setBackgroundColor(Color.parseColor("#DCD0FF"));
                break;
            case 5:
                layout.setBackgroundColor(Color.parseColor("#6AFB92"));
                break;
        }
    }

}