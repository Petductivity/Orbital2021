
package com.example.petductivity.planner;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petductivity.achievement.AchievementMainActivity;
import com.example.petductivity.focus_mode.FocusModeActivity;
import com.example.petductivity.pet.PetsUpdateStatus;
import com.example.petductivity.R;
import com.example.petductivity.setting_up.VerificationActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.petductivity.planner.PlannerUtils.monthYearDateFormat;
import static com.example.petductivity.planner.PlannerUtils.setUpWeekArray;

/**
 * A class encapsulating the Planner Week Activity.
 *
 * @author Team Petductivity
 */
public class PlannerWeekActivity extends AppCompatActivity implements PlannerAdapter.OnItemListener {

    /**
     * Directory names used in database for this activity.
     */
    public static final String plannerDir = "planner";
    private final String startTimeSubDir = "startTime";

    /**
     * UI elements for the PlannerWeekActivity layout.
     */
    private TextView monthHeader;
    private RecyclerView calRV, activityRV;
    private ImageView prevWeek, nextWeek;
    private TextView navBack;
    private FloatingActionButton fab;
    private ProgressDialog loader;

    /**
     * Firebase Components
     */
    private DatabaseReference reference;
    private FirebaseAuth fb;
    private FirebaseUser user;
    private String onlineUserID;

    /**
     * Other temporary variables
     */
    private String selectedDate;
    private int startHour, startMin, endHour, endMin;

    /**
     * Entry for the different directories in the database.
     */
    private String key = "";
    private String activity;
    private String startTime;
    private String endTime;
    private String date;
    private int duration;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_week_view);

        setUpUI();
        setUpWeekView();
        setUpActivityView();
        setUpFirebase();

        selectedDate = PlannerUtils.dateFormat(PlannerUtils.selectedDate);

        loader = new ProgressDialog(this);

        prevWeek.setOnClickListener(this::previousWeek);

        nextWeek.setOnClickListener(this::nextWeek);

        navBack.setOnClickListener(listener -> {
            navBack.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            finish();
        });

        fab.setOnClickListener(listener -> addActivity());
    }

    /**
     * Set up the UI of the weekly planner page.
     */
    private void setUpUI() {
        calRV = findViewById(R.id.calendarRV);
        monthHeader = findViewById(R.id.monthTV);
        prevWeek = findViewById(R.id.prevWeek);
        nextWeek = findViewById(R.id.nextWeek);
        navBack = findViewById(R.id.plannerWeekBackToMonthPage);
        fab = findViewById(R.id.plannerWeekFab);
    }

    /**
     * Sets up the recyclerview for the layout.
     */
    private void setUpActivityView() {
        activityRV = findViewById(R.id.plannerWeekRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activityRV.setHasFixedSize(true);
        activityRV.setLayoutManager(linearLayoutManager);
    }

    /**
     * Setup Firebase.
     */
    private void setUpFirebase() {
        fb = FirebaseAuth.getInstance();
        user = fb.getCurrentUser();
        onlineUserID = user.getUid();
        reference = FirebaseDatabase.getInstance(VerificationActivity.databaseURL)
                .getReference()
                .child(plannerDir)
                .child(onlineUserID);
    }

    /**
     * Setup the weekly calendar.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpWeekView() {
        monthHeader.setText(monthYearDateFormat(PlannerUtils.selectedDate));
        ArrayList<LocalDate> days = setUpWeekArray(PlannerUtils.selectedDate);

        PlannerAdapter calendarAdapter = new PlannerAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calRV.setLayoutManager(layoutManager);
        calRV.setAdapter(calendarAdapter);
    }

    /**
     * The activity that follows when user clicks on add task button.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addActivity() {
        FocusModeActivity.setUpFocusMode(selectedDate, onlineUserID);

        // set up the pop-up view for user to add task
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View addTaskView = inflater.inflate(R.layout.planner_event_add, null);

        // set up the pop-up view
        builder.setView(addTaskView);
        final AlertDialog popUp = builder.create();

        // prevent the pop-up from disappearing when user touches region outside of the pop-up
        popUp.setCancelable(false);

        // set up UI for add task pop-up activity
        TextView date = addTaskView.findViewById(R.id.eventDateTV);
        EditText task = addTaskView.findViewById(R.id.activityNameET);
        Button startTime = addTaskView.findViewById(R.id.startTimeButton);
        Button endTime = addTaskView.findViewById(R.id.endTimeButton);
        Button save = addTaskView.findViewById(R.id.plannerSaveBtn);
        ImageView cancel = addTaskView.findViewById(R.id.plannerCancelBtn);

        date.setText(PlannerUtils.dateFormat(PlannerUtils.selectedDate));
        startTime.setOnClickListener(listener -> setStartTime(startTime, endTime));
        endTime.setOnClickListener(listener -> setEndTime(endTime));

        // user clicks cancel button
        cancel.setOnClickListener(listener -> popUp.dismiss());

        // user clicks save button
        save.setOnClickListener(listener -> {
            String mTask = task.getText().toString().trim();
            String mStartTime = startTime.getText().toString().trim();
            String mEndTime = endTime.getText().toString().trim();
            String id = reference.push().getKey();
            String mDate = date.getText().toString().trim();

            // calculates the duration of task
            int mduration = calcDurationInMins(mStartTime, mEndTime);

            if (TextUtils.isEmpty(mTask)) {
                task.setError("Cannot be blank!");
                return;
            } else {
                loader.setMessage("Processing");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                PlannerWeekEventModel model = new PlannerWeekEventModel(mDate, mTask, id, mStartTime, mEndTime, mduration);

                reference.child(mDate).child(id).setValue(model).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                        duration = mduration;
                        FocusModeActivity.updateFocusModeAdd(selectedDate, onlineUserID, duration);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to add task!", Toast.LENGTH_SHORT).show();
                        Log.d("Planner", "Add Task Failed: " + task1.getException().toString());
                    }

                    loader.dismiss();
                });

            }

            popUp.dismiss();
        });

        popUp.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // retrieve data from data base
        retrieveFirebase();
    }

    /**
     * Retrieve the relevant data from firebase.
     */
    private void retrieveFirebase() {
        FirebaseRecyclerOptions<PlannerWeekEventModel> options = new FirebaseRecyclerOptions.Builder<PlannerWeekEventModel>()
                .setQuery(reference.child(selectedDate).orderByChild(startTimeSubDir), PlannerWeekEventModel.class)
                .build();

        FirebaseRecyclerAdapter<PlannerWeekEventModel, PlannerWeekActivity.MyViewHolder> adapter = new FirebaseRecyclerAdapter<PlannerWeekEventModel, PlannerWeekActivity.MyViewHolder>(options) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull PlannerWeekActivity.MyViewHolder holder, final int position, @NonNull final PlannerWeekEventModel model) {
                holder.setTime(model.getStartTime());
                holder.setTask(model.getActivity());
                holder.setCheckBoxToFalse();
                DatabaseReference ref = getRef(position);

                holder.currView.setOnClickListener(listener -> {
                    key = ref.getKey();
                    activity = model.getActivity();
                    startTime = model.getStartTime();
                    endTime = model.getEndTime();
                    duration = model.getDuration();

                    updateTask();
                });

                holder.getCheckBox().setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        // get the amount of exp awarded to the user for completing the task with the specific duration
                        long exp = PetsUpdateStatus.expAwarded(duration);
                        Log.i("exp", "" + exp);

                        reference.child(selectedDate).child(ref.getKey()).removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(PlannerWeekActivity.this, "Completed!", Toast.LENGTH_SHORT).show();

                                // update the exp of user pokemon in the database
                                AchievementMainActivity.giveAchievement(onlineUserID, PlannerWeekActivity.this, R.layout.achievement_award, duration);
                                FocusModeActivity.updateFocusModeComplete(selectedDate, onlineUserID);
                                FocusModeActivity.checkUnlockFocusMode(selectedDate, onlineUserID, exp, PlannerWeekActivity.this, duration);
                            } else {
                                Toast.makeText(PlannerWeekActivity.this, "Unexpected Error Occurred!", Toast.LENGTH_SHORT).show();
                                Log.d("Planner", "Checked Task Failed: " + task.getException().toString());
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public PlannerWeekActivity.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planner_event_cell, parent, false);
                return new PlannerWeekActivity.MyViewHolder(view);
            }
        };

        activityRV.setAdapter(adapter);
        adapter.startListening();
    }

    /**
     * Helper class to retrieve data.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * Layout used for the recyclerview.
         */
        private final View currView;

        /**
         * UI elements for the current view.
         */
        private final CheckBox checkbox;

        /**
         * Constructor for MyViewHolder class.
         *
         * @param itemView The view used.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            currView = itemView;
            checkbox = currView.findViewById(R.id.plannerCheckBox);
        }

        /**
         * Setter for the title of task.
         *
         * @param task Title of task.
         */
        public void setTask(String task) {
            TextView taskTextView = currView.findViewById(R.id.eventCellTV);
            taskTextView.setText(task);
        }

        /**
         * Setter for the time of the activity.
         *
         * @param time The time of the activity.
         */
        public void setTime(String time) {
            TextView dateTextView = currView.findViewById(R.id.timeCellTV);
            dateTextView.setText(time);
        }

        /**
         * Set checkbox to uncheck.
         */
        public void setCheckBoxToFalse() {
            checkbox.setChecked(false);
        }

        /**
         * Getter to retrieve checkbox for the current view.
         *
         * @return Checkbox for the current view
         */
        public CheckBox getCheckBox() {
            return checkbox;
        }
    }

    /**
     * The activity that follows when the user clicks on the task
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View updateTaskView = inflater.inflate(R.layout.planner_event_update, null);

        builder.setView(updateTaskView);

        final AlertDialog popUp = builder.create();

        TextView mDate = updateTaskView.findViewById(R.id.eventDateUpdateTV);
        EditText mTask = updateTaskView.findViewById(R.id.activityNameUpdateET);
        Button mStartTime = updateTaskView.findViewById(R.id.startTimeUpdateButton);
        Button mEndTime = updateTaskView.findViewById(R.id.endTimeUpdateButton);
        Button save = updateTaskView.findViewById(R.id.plannerSaveUpdateBtn);
        ImageView cancel = updateTaskView.findViewById(R.id.plannerCancelUpdateBtn);
        Button delete = updateTaskView.findViewById(R.id.plannerDeleteUpdateBtn);

        mDate.setText(PlannerUtils.dateFormat(PlannerUtils.selectedDate));
        mStartTime.setOnClickListener(listener -> setStartTime(mStartTime, mEndTime));
        mEndTime.setOnClickListener(listener -> setEndTime(mEndTime));

        mTask.setText(activity);
        mTask.setSelection(activity.length());

        mStartTime.setText(startTime);
        mEndTime.setText(endTime);


        save.setOnClickListener(listener -> {
            activity = mTask.getText().toString().trim();
            startTime = mStartTime.getText().toString().trim();
            endTime = mEndTime.getText().toString().trim();
            date = mDate.getText().toString().trim();

            // calculates the duration of task
            int oldDuration = duration;
            duration = calcDurationInMins(startTime, endTime);

            PlannerWeekEventModel model = new PlannerWeekEventModel(date, activity, key, startTime, endTime, duration);

            reference.child(selectedDate).child(key).setValue(model).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                    FocusModeActivity.updateTotalDuration(selectedDate, onlineUserID, duration, oldDuration);
                } else {
                    Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_SHORT).show();
                    Log.d("Planner", "Update Task Failed: " + task.getException().toString());
                }

            });

            popUp.dismiss();
        });

        delete.setOnClickListener(listener -> {
            reference.child(selectedDate).child(key).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                    FocusModeActivity.updateFocusModeDelete(selectedDate, onlineUserID, duration);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to delete task!", Toast.LENGTH_SHORT).show();
                    Log.d("Planner", "Delete Task Failed: " + task.getException().toString());
                }
            });

            popUp.dismiss();
        });

        cancel.setOnClickListener(listener -> popUp.dismiss());

        popUp.show();
    }

    /**
     * Toggles the view to the previous week.
     *
     * @param view The current view.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeek(View view) {
        PlannerUtils.selectedDate = PlannerUtils.selectedDate.minusWeeks(1);
        setUpWeekView();
    }

    /**
     * Toggles the view to the next week.
     *
     * @param view The current view.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeek(View view) {
        PlannerUtils.selectedDate = PlannerUtils.selectedDate.plusWeeks(1);
        setUpWeekView();
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
        PlannerUtils.selectedDate = date;
        selectedDate = PlannerUtils.dateFormat(date);
        retrieveFirebase();
        FocusModeActivity.setUpFocusMode(selectedDate, onlineUserID);
        setUpWeekView();
    }

    /**
     * Sets the start time of the Timer widget.
     *
     * @param startTime The startTime button of the layout.
     * @param endTime The endTime button of the layout.
     */
    private void setStartTime(Button startTime, Button endTime) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view1, hourOfDay, minute) -> {
            startHour = hourOfDay;
            startMin = minute;
            if (!checkValidTime(startHour, startMin, endHour, endMin)) {
                endTime.setError("Invalid End Time Selected!");
                Toast.makeText(this, "Are you sure? Invalid End Time Selected! Please check again!", Toast.LENGTH_SHORT).show();
            } else {
                endTime.setError(null);
            }
            startTime.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMin));
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, startHour, startMin, true);
        timePickerDialog.show();
    }

    /**
     * Sets the end time of the Timer widget.
     *
     * @param endTime The endTime button of the layout.
     */
    private void setEndTime(Button endTime) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view1, hourOfDay, minute) -> {
            endHour = hourOfDay;
            endMin = minute;
            if (!checkValidTime(startHour, startMin, endHour, endMin)) {
                endTime.setError("Invalid End Time Selected!");
                Toast.makeText(this, "Are you sure? Invalid End Time Selected! Please check again!", Toast.LENGTH_SHORT).show();
            } else {
                endTime.setError(null);
            }
            endTime.setText(String.format(Locale.getDefault(), "%02d:%02d", endHour, endMin));
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, endHour, endMin, true);
        timePickerDialog.show();
    }

    /**
     * Calculates the duration of the activity, given the start and end time.
     *
     * @param start The start time.
     * @param end The end time.
     * @return The duration of the activity in minutes.
     */
    private int calcDurationInMins(String start, String end) {
        int hour = Integer.parseInt(end.substring(0,2)) - Integer.parseInt(start.substring(0,2));
        int min = Integer.parseInt(end.substring(3)) - Integer.parseInt(start.substring(3));
        int duration = (hour * 60) + min;

        return duration;
    }

    /**
     * Checks if a specified start and end time is valid, i.e. if the end time is after the start time.
     *
     * @param startHour The start hour.
     * @param startMin The start minute.
     * @param endHour The end hour.
     * @param endMin The end minute.
     * @return A boolean indicating the result.
     */
    private boolean checkValidTime(int startHour, int startMin, int endHour, int endMin) {

        if (endHour < startHour) {
            return false;
        } else if (endHour == startHour) {
            return endMin > startMin;
        } else {
            return true;
        }
    }
}