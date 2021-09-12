package com.example.petductivity.task;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petductivity.AlarmReceiver;
import com.example.petductivity.R;
import com.example.petductivity.setting_up.VerificationActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A helper class for the TodoMainActivity class.
 *
 * @author Team Petductivity
 */
public class TodoMainActivity extends AppCompatActivity {
    /**
     * Directory names used in database for this activity.
     */
    public static final String tasksDir = "tasks";
    public static final String orderSubDir = "order";

    /**
     * UI elements for the TodoMainActivity layout.
     */
    private RecyclerView recyclerView;
    private TextView navBack;
    private ProgressDialog loader;
    private Button dateBtn;
    private ImageView helpIcon;

    /**
     * Firebase Components
     */
    private FirebaseAuth fb;
    private FirebaseUser user;
    private String onlineUserID;
    private DatabaseReference reference;

    /**
     * Entry for the different directories in the database.
     */
    private String key = "";
    private String task;
    private String description;
    private String date;
    private String order;
    private boolean isSet;

    /**
     * Date selector widget.
     */
    private DatePickerDialog dpd;

    /**
     * Notification Components
     */
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_main);
        createNotificationChannel();

        // Set up the task layout
        setUpUI();
        setUpRecyclerView();

        // set up the UI when data is being saved to database
        loader = new ProgressDialog(this);

        // set up firebase / database
        setUpFirebase();

        // set up UI for add new tasks
        FloatingActionButton floatingActionButton = findViewById(R.id.todoFab);
        floatingActionButton.setOnClickListener(listener -> addTask());

        // user wants to go back to home page
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
        navBack = findViewById(R.id.todoBackToHomePage);
        helpIcon = findViewById(R.id.taskHelp);
    }

    /**
     * Setup recyclerview used for this layout.
     */
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.todoRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * Setup Firebase.
     */
    private void setUpFirebase() {
        fb = FirebaseAuth.getInstance();
        user = fb.getCurrentUser();
        onlineUserID = user.getUid();
        reference = FirebaseDatabase.getInstance(VerificationActivity.databaseURL).getReference().child(tasksDir).child(onlineUserID);
    }

    /**
     * Setup date selector widget used.
     */
    private void setUpDateWidget(View view, int id) {
        datePicker();
        dateBtn = view.findViewById(id);
        dateBtn.setText(getTodaysDate());
    }

    /**
     * Activity that follows when user clicks on add task button.
     */
    private void addTask() {
        // set up the pop-up view for user to add task
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View addTaskView = inflater.inflate(R.layout.todo_addtask, null);

        // initialise the date picker widget
        setUpDateWidget(addTaskView, R.id.todoSelectDate);

        // set up the pop-up view
        builder.setView(addTaskView);
        final AlertDialog popUp = builder.create();

        // prevent the pop-up from disappearing when user touches region outside of the pop-up
        popUp.setCancelable(false);

        // set up UI for add task pop-up activity
        EditText task = addTaskView.findViewById(R.id.todoAddTask);
        EditText description = addTaskView.findViewById(R.id.todoAddDes);
        Button date = addTaskView.findViewById(R.id.todoSelectDate);
        Button save = addTaskView.findViewById(R.id.saveBtn);
        Button cancel = addTaskView.findViewById(R.id.CancelBtn);
        Switch setReminder = addTaskView.findViewById(R.id.addTaskReminderSwitch);

        // user clicks cancel button
        cancel.setOnClickListener(listener -> popUp.dismiss());


        // user clicks save button
        save.setOnClickListener(listener -> {
            String mTask = task.getText().toString().trim();
            String mDescription = description.getText().toString().trim();
            String id = reference.push().getKey();
            String mDate = date.getText().toString().trim();
            String mOrder = mDate.substring(3,5) + " " + mDate.substring(0,2);
            boolean mIsSet = setReminder.isChecked();


            if (TextUtils.isEmpty(mTask)) {
                task.setError("Task Required");
                return;
            } else {
                // set up loader when data is being sync
                loader.setMessage("Adding your data");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                // added data to the database
                TodoModel model = new TodoModel(mTask, mDescription, id, mDate, mIsSet, mOrder);
                reference.child(id).setValue(model).addOnCompleteListener(listenerTask -> {
                    if (listenerTask.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Task has been inserted successfully", Toast.LENGTH_SHORT).show();
                        setNotificationReminder(setReminder, mDate);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to insert task!", Toast.LENGTH_SHORT).show();
                        Log.d("Important Tasks", "Add Task Failed: " + listenerTask.getException().toString());
                    }

                    loader.dismiss();
                });

            }

            popUp.dismiss();
        });

        popUp.show();
    }

    /**
     * Setup and Retrieve data from data base
     */
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<TodoModel> options = new FirebaseRecyclerOptions.Builder<TodoModel>()
                .setQuery(reference.orderByChild(orderSubDir), TodoModel.class)
                .build();

        FirebaseRecyclerAdapter<TodoModel, MyViewHolder> adapter = new FirebaseRecyclerAdapter<TodoModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final TodoModel model) {
                holder.setDate(model.getDate());
                holder.setTask(model.getTask());
                holder.setDesc(model.getDescription());
                holder.setCheckBoxToFalse();
                DatabaseReference ref = getRef(position);

                //updates the information from database
                holder.currView.setOnClickListener(listener -> {
                    key = ref.getKey();
                    task = model.getTask();
                    description = model.getDescription();
                    date = model.getDate();
                    order = model.getOrder();
                    isSet = model.getIsSet();

                    updateTask();
                });

                // action that follows when checkbox is being checked
                holder.getCheckBox().setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        reference.child(ref.getKey()).removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(TodoMainActivity.this, "Completed!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TodoMainActivity.this, "Unexpected Error Occurred!", Toast.LENGTH_SHORT).show();
                                Log.d("Important Tasks", "Checked Task Failed: " + task.getException().toString());
                            }
                        });
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_retrieved_layout, parent, false);
                return new MyViewHolder(view);
            }
        };

        //set up the adapter used.
        recyclerView.setAdapter(adapter);
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
            checkbox = currView.findViewById(R.id.todoCheckBox);
        }

        /**
         * Setter for the title of task.
         *
         * @param task Title of task.
         */
        public void setTask(String task) {
            TextView taskView = currView.findViewById(R.id.todoRetrieveTask);
            taskView.setText(task);
        }

        /**
         * Setter for the description of task.
         *
         * @param desc Description of task.
         */
        public void setDesc(String desc) {
            TextView descView = currView.findViewById(R.id.todoRetrieveDes);
            descView.setText(desc);
        }

        /**
         * Setter for the due date of task.
         *
         * @param date Due date of task.
         */
        public void setDate(String date) {
            TextView dateView = currView.findViewById(R.id.todoRetrieveDate);
            dateView.setText(date);
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
     * Activity that follows when the user clicks on the task
     */
    private void updateTask() {
        // setup the popup view for updating task
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View updateTaskView = inflater.inflate(R.layout.todo_update_data, null);

        setUpDateWidget(updateTaskView, R.id.todoUpdateDate);

        builder.setView(updateTaskView);

        final AlertDialog popUp = builder.create();

        // UI elements in this popup view
        EditText mTask = updateTaskView.findViewById(R.id.todoUpdateTask);
        EditText mDescription = updateTaskView.findViewById(R.id.todoUpdateDes);
        Button mDate = updateTaskView.findViewById(R.id.todoUpdateDate);
        Switch setReminder = updateTaskView.findViewById(R.id.updateTaskReminderSwitch);
        Button delButton = updateTaskView.findViewById(R.id.todoDeleteBtn);
        Button updateButton = updateTaskView.findViewById(R.id.todoUpdateBtn);

        // setup the UI elements
        setReminder.setChecked(isSet);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        mDate.setText(date);

        // user clicks on update button
        updateButton.setOnClickListener(listener -> {
            task = mTask.getText().toString().trim();
            description = mDescription.getText().toString().trim();
            date = mDate.getText().toString().trim();
            order = date.substring(3,5) + " " + date.substring(0,2);
            isSet = setReminder.isChecked();

            TodoModel model = new TodoModel(task, description, key, date, isSet, order);

            reference.child(key).setValue(model).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Data has been updated successfully", Toast.LENGTH_SHORT).show();
                    setNotificationReminder(setReminder, date);
                }else {
                    Toast.makeText(getApplicationContext(), "Update Failed!", Toast.LENGTH_SHORT).show();
                    Log.d("Important Tasks", "Failed to update task: " + task.getException().toString());
                }

            });

            popUp.dismiss();
        });

        // user clicks on delete button
        delButton.setOnClickListener(listener -> {
            reference.child(key).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show();
                    deleteNotificationReminder();
                }else {
                    Toast.makeText(getApplicationContext(), "Failed to delete task!", Toast.LENGTH_SHORT).show();
                    Log.d("Important Tasks", "Failed to delete task: " + task.getException().toString());
                }
            });

            popUp.dismiss();
        });

        popUp.show();
    }

    /**
     * Returns today's date based on calendar.
     *
     * @return A string representing today's date.
     */
    public static String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    /**
     * Select due date from the date picker widget.
     */
    private void datePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            month += 1;
            String date = makeDateString(dayOfMonth, month, year);

            // check if the date is in the past.
            if (!checkValidDate(date)) {
                dateBtn.setError("Invalid date selected!");
                Toast.makeText(this, "Are you sure? Invalid date selected! Please check again!", Toast.LENGTH_SHORT).show();
            } else {
                dateBtn.setError(null);
            }
            dateBtn.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = android.app.AlertDialog.THEME_HOLO_DARK;
        dpd = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    /**
     * Formats the date.
     *
     * @param day Day
     * @param month Month
     * @param year Year
     *
     * @return A string representing the date inputted.
     */
    public static String makeDateString(int day, int month, int year) {
        return getDayFormat(day) + getMonthFormat(month) + year;
    }

    /**
     * Format the month in the date string.
     *
     * @param month Month
     *
     * @return A string representing the month selected.
     */
    private static String getMonthFormat(int month) {
        switch (month) {
            case 2:
                return " 02 ";
            case 3:
                return " 03 ";
            case 4:
                return " 04 ";
            case 5:
                return " 05 ";
            case 6:
                return " 06 ";
            case 7:
                return " 07 ";
            case 8:
                return " 08 ";
            case 9:
                return " 09 ";
            case 10:
                return " 10 ";
            case 11:
                return " 11 ";
            case 12:
                return " 12 ";
            default:
                return " 01 ";
        }
    }

    /**
     * Format the day in the date string.
     *
     * @param day Day
     *
     * @return A string representing the day selected.
     */
    private static String getDayFormat(int day) {
        if (day < 10) {
            switch (day) {
                case 2:
                    return "02";
                case 3:
                    return "03";
                case 4:
                    return "04";
                case 5:
                    return "05";
                case 6:
                    return "06";
                case 7:
                    return "07";
                case 8:
                    return "08";
                case 9:
                    return "09";
                default:
                    return "01";
            }
        } else {
            return String.valueOf(day);
        }
    }

    /**
     * Checks if the date selected is valid.
     *
     * @param date Date Selected
     *
     * @return A boolean indicating if the selected date is in the past.
     */
    private boolean checkValidDate(String date)  {
        long oneDayInMs = 86400000;

        boolean result = false;
        try {
            long selectedDate = timeToMillis(date);
            result = (selectedDate + oneDayInMs) > System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Show the date widget setup.
     *
     * @param view View to show the date widget.
     */
    public void todoOpenDatePicker(View view) {
        dpd.show();
    }

    /**
     * Creates the task reminder notification channel.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PetductivityReminderChannel";
            String description = "Channel for Petductivity Reminder System";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("petductivity", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Converts selected date to milliseconds.
     *
     * @param dueDate Due date selected.
     *
     * @return Due date in milliseconds.
     * @throws ParseException Error that occurs when parse fails.
     */
    private long timeToMillis(String dueDate) throws ParseException {
        SimpleDateFormat dates = new SimpleDateFormat("dd MM yyyy");
        Date dDate = dates.parse(dueDate);

        return dDate.getTime();
    }

    /**
     * Action that follows when user turn on the set notification switch.
     *
     * @param reminderSwitch Switch for user to choose whether to turn on/off notification reminder.
     * @param dueDate Due date selected.
     */
    private void setNotificationReminder(Switch reminderSwitch, String dueDate) {
        if (reminderSwitch.isChecked()) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            int id = (int) System.currentTimeMillis();
            pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
            try {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeToMillis(dueDate), pendingIntent);
                Log.i("Due Date", "" + dueDate);
                Log.i("Due date in Millis", "" + timeToMillis(dueDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Reminder Set Successfully", Toast.LENGTH_SHORT).show();
        }

        if (!reminderSwitch.isChecked()){
            deleteNotificationReminder();
        }
    }

    /**
     * Remove notification when user deletes task.
     */
    private void deleteNotificationReminder() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Reminder Cancelled", Toast.LENGTH_SHORT).show();
    }

    /**
     * Set up the UI elements used for the help popup.
     */
    private void setUpHelpPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.task_help, null);


        alert.setNegativeButton("Dismiss", null)
                .setView(view) // insert the layout into the dialog
                .create()
                .show();
    }

}
