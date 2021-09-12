package com.example.petductivity.planner;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petductivity.R;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A ViewHolder class used for the adapter of the recyclerview for the PLannerMainActivity layout.
 *
 * @author Team Petductivity
 */
public class PlannerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * UI elements for the PlannerViewHolder layout
     */
    private final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth;
    private final PlannerAdapter.OnItemListener onItemListener;

    /**
     * Constructor for a class instance.
     *
     * @param itemView The view used.
     * @param onItemListener The listener used.
     * @param days The list of dates to be used.
     */
    public PlannerViewHolder(@NonNull View itemView, PlannerAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days) {
        super(itemView);
        this.parentView = itemView.findViewById(R.id.parentView);
        this.dayOfMonth = itemView.findViewById(R.id.calenderDayText);
        this.days = days;
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }
}
