package com.example.petductivity.achievement;

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
 * This class contains the Adapter class required for the RecyclerView used in the AchievementMainActivity class.
 *
 * @author Team Petductivity
 */
public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.MyViewHolder> {

    /**
     * names: The array containing the names to be displayed in the RecyclerView.
     * descs: The array containing the descriptions to be displayed in the RecyclerView.
     * color: The array containing the colors to be displayed in the RecyclerView.
     * images: The array containing the images to be displayed in the RecyclerView.
     * context: The current context where the adapter is operating.
     */
    private final String[] names, descs, color;
    private final int[] images;
    private final Context context;

    /**
     * A nested class that describes the item views and metadata of each row of the RecyclerView.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * UI elements in the row.
         */
        private final TextView myText1, myText2;
        private final ImageView myImage;
        private final ConstraintLayout badge;

        /**
         * Constructor for a class instance.
         *
         * @param itemView The view used.
         */
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.achievementRowTextViewNames);
            myText2 = itemView.findViewById(R.id.achievementRowTextViewDescs);
            myImage = itemView.findViewById(R.id.achievementRowImageView);
            badge = itemView.findViewById(R.id.achievementBg);
        }
    }

    /**
     * Constructor for an AchievementAdapter instance.
     *
     * @param context The current context where the adapter is operating.
     * @param names The array containing the names to be displayed in the RecyclerView.
     * @param descs The array containing the descriptions to be displayed in the RecyclerView.
     * @param color The array containing the colors to be displayed in the RecyclerView.
     * @param images The array containing the images to be displayed in the RecyclerView.
     */
    public AchievementAdapter(Context context, String[] names, String[] descs, String[] color, int[] images) {
        this.context = context;
        this.names = names;
        this.descs = descs;
        this.color = color;
        this.images = images;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.achievement_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.myText1.setText(names[position]);
        holder.myText2.setText(descs[position]);
        holder.myImage.setImageResource(images[position]);

        setBadgeBg(holder, position);
    }

    /**
     * Returns the number of elements to be displayed.
     *
     * @return An integer.
     */
    @Override
    public int getItemCount() {
        return images.length;
    }

    /**
     * Sets the background color of a row within the RecyclerView based on the color.
     *
     * @param holder The MyViewHolder instance of the RecyclerView.
     * @param position The index of the color array to be referenced.
     */
    private void setBadgeBg(@NonNull @NotNull MyViewHolder holder, int position) {
        String bgColor = color[position];

        switch(bgColor) {
            case "green":
                holder.badge.setBackgroundColor(Color.parseColor("#C3FDB8"));
                break;
            case "red":
                holder.badge.setBackgroundColor(Color.parseColor("#FFA07A"));
                break;
            case "blue":
                holder.badge.setBackgroundColor(Color.parseColor("#5CB3FF"));
                break;
            case "darkgreen":
                holder.badge.setBackgroundColor(Color.parseColor("#B2C248"));
                break;
            case "white":
                holder.badge.setBackgroundColor(Color.parseColor("#EDE6D6"));
                break;
            case "purple":
                holder.badge.setBackgroundColor(Color.parseColor("#E6A9EC"));
                break;
            case "yellow":
                holder.badge.setBackgroundColor(Color.parseColor("#FFF380"));
                break;
            case "brown":
                holder.badge.setBackgroundColor(Color.parseColor("#EDC9AF"));
                break;
            case "pink":
                holder.badge.setBackgroundColor(Color.parseColor("#FDD7E4"));
                break;
            case "darkred":
                holder.badge.setBackgroundColor(Color.parseColor("#997070"));
                break;
            case "darkpink":
                holder.badge.setBackgroundColor(Color.parseColor("#F778A1"));
                break;
            case "darkbrown":
                holder.badge.setBackgroundColor(Color.parseColor("#C19A6B"));
                break;
            case "darkpurple":
                holder.badge.setBackgroundColor(Color.parseColor("#5E5A80"));
                break;
            case "lightblue":
                holder.badge.setBackgroundColor(Color.parseColor("#BDEDFF"));
                break;
            case "lightpurple":
                holder.badge.setBackgroundColor(Color.parseColor("#7F38EC"));
                break;
            default:
                holder.badge.setBackgroundColor(Color.parseColor("500063"));
        }
    }
}
