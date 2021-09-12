package com.example.petductivity.planner;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petductivity.R;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class contains the Adapter class required for the RecyclerView used in the PlannerMainActivity class.
 *
 * @author Team Petductivity
 */
class PlannerAdapter extends RecyclerView.Adapter<PlannerViewHolder> {

    /**
     * days: The number of days for each month.
     * onItemListener: Listens for when an item is clicked, then performs an activity.
     */
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    /**
     * Constructor for a PlannerAdapter instance.
     *
     * @param days A list.
     * @param onItemListener A listener.
     */
    public PlannerAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public PlannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.planner_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.167);
        else // week view
            layoutParams.height = parent.getHeight();

        return new PlannerViewHolder(view, onItemListener, days);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull PlannerViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        if (date == null)
            holder.dayOfMonth.setText("");
        else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

            if(date.equals(PlannerUtils.selectedDate))
                holder.parentView.setBackgroundColor(Color.LTGRAY);
        }
    }

    /**
     * Returns the number of elements in the view.
     *
     * @return An integer.
     */
    @Override
    public int getItemCount()
    {
        return days.size();
    }

    /**
     * A nested interface required for the adapter.
     */
    public interface  OnItemListener {
        void onItemClick(int position, LocalDate date);
    }
}
