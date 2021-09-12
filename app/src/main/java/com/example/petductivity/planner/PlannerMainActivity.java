package com.example.petductivity.planner;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petductivity.focus_mode.FocusModeActivity;
import com.example.petductivity.R;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.petductivity.planner.PlannerUtils.monthYearDateFormat;
import static com.example.petductivity.planner.PlannerUtils.setUpMonthArray;

/**
 * A class that contains the activities of the Planner and its subactivities.
 *
 * @author Team Petductivity
 */
public class PlannerMainActivity extends AppCompatActivity implements PlannerAdapter.OnItemListener {

    /**
     * UI elements for the PlannerMainActivity layout.
     */
    private TextView monthHeader;
    private RecyclerView calRV;
    private ImageView nextMonth, prevMonth, helpIcon;
    private TextView navBack;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_main);

        setUpUI();
        PlannerUtils.selectedDate = LocalDate.now();
        setUpMonthView();

        nextMonth.setOnClickListener(this::previousMonth);

        prevMonth.setOnClickListener(this::nextMonth);

        navBack.setOnClickListener(listener -> {
            navBack.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            finish();
        });

        helpIcon.setOnClickListener(v -> setUpHelpPopUp());
    }

    /**
     * Set up UI for the planner dashboard page (i.e shows the monthly calender).
     */
    private void setUpUI() {
        calRV = findViewById(R.id.calendarRV);
        monthHeader = findViewById(R.id.monthTV);
        prevMonth = findViewById(R.id.prevMonth);
        nextMonth = findViewById(R.id.nextMonth);
        navBack = findViewById(R.id.plannerMainBackToHomePage);
        helpIcon = findViewById(R.id.plannerHelp);
    }

    /**
     * Set up the monthly calendar.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpMonthView() {
        monthHeader.setText(monthYearDateFormat(PlannerUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = setUpMonthArray(PlannerUtils.selectedDate);
        PlannerAdapter calendarAdapter = new PlannerAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calRV.setLayoutManager(layoutManager);
        calRV.setAdapter(calendarAdapter);
    }

    /**
     * The activity that follows when the user toggles the up arrow.
     *
     * @param view The current View.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonth(View view) {
        PlannerUtils.selectedDate = PlannerUtils.selectedDate.minusMonths(1);
        setUpMonthView();
    }

    /**
     * The activity that follows when the user toggles the down arrow.
     * @param view The current View.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonth(View view) {
        PlannerUtils.selectedDate = PlannerUtils.selectedDate.plusMonths(1);
        setUpMonthView();
    }

    /**
     * The activity that follows when the user clicks on an item.
     *
     * @param position The position of the date.
     * @param date The selected date of the item clicked by the user.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date != null) {
            PlannerUtils.selectedDate = date;
            setUpMonthView();

            String inputDate = PlannerUtils.dateFormat(PlannerUtils.selectedDate);
            String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FocusModeActivity.setUpFocusMode(inputDate, currUser);

            startActivity(new Intent(this, PlannerWeekActivity.class));
        }
    }

    /**
     * Set up the UI elements used for the help popup.
     */
    private void setUpHelpPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.planner_help, null);


        alert.setNegativeButton("Dismiss", null)
                .setView(view) // insert the layout into the dialog
                .create()
                .show();
    }
}








