package com.example.petductivity.focus_mode;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.R;
import com.example.petductivity.setting_up.VerificationActivity;
import com.example.petductivity.achievement.AchievementMainActivity;
import com.example.petductivity.pet.PetsUpdateStatus;
import com.example.petductivity.task.TodoMainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

/**
 * This class encapsulates the information about the Focus Mode activity.
 *
 * @author Team Petductivity
 */
public class FocusModeActivity extends AppCompatActivity {
    /**
     * Directory names used in database for this activity.
     */
    public static final String focusModeDir = "Focus_Mode";
    public static final String taskCompleted = "completed";
    public static final String taskAdded = "added";
    public static final String totalDuration = "total_duration";
    public static final String lastFocusProc = "lastFocusProc";

    /**
     * Base amount of bonus exp to be award when user unlocks this mode.
     */
    private static final long bonusExp = 50;

    /**
     * Firebase component.
     */
    private static final DatabaseReference reference = FirebaseDatabase
            .getInstance(VerificationActivity.databaseURL)
            .getReference(focusModeDir);

    /**
     * Set up the last Focus Date when account is created.
     *
     * @param currUser The UID of the user logged-in/registered.
     */
    public static void initialiseFocusMode(String currUser) {
        DatabaseReference ref = reference.child(currUser);
        ref.child(lastFocusProc).setValue("");
    }

    /**
     * Set up the Focus Mode directory in the database for the selected date.
     *
     * @param date The selected date.
     * @param currUser The UID of the user logged-in/registered.
     */
    public static void setUpFocusMode(String date, String currUser) {
        DatabaseReference ref = reference.child(currUser);

        ref.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Object selectedDate = snapshot.getValue();

                // check if it is already set up, if not, set it up.
                if (selectedDate == null) {
                    ref.child(date).child(taskCompleted).setValue(0).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("Focus Mode", "Task Completed Directory Setup Successful");
                        } else {
                            Log.d("Focus Mode", "Task Completed Directory Setup Failed: " + task.getException().toString());
                        }
                    });

                    ref.child(date).child(taskAdded).setValue(0).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("Focus Mode", "Task Added Directory Setup Successful");
                        } else {
                            Log.d("Focus Mode", "Task Added Directory Setup Failed: " + task.getException().toString());
                        }
                    });

                    ref.child(date).child(totalDuration).setValue(0).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("Focus Mode", "Total Duration Directory Setup Successful");
                        } else {
                            Log.d("Focus Mode", "Total Duration Directory Setup Failed: " + task.getException().toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Focus Mode", "Focus Mode Directory Setup Failed: " + error);
            }
        });
    }

    /**
     * Updates the number of task completed in the taskCompleted directory when a user completes a task in the Planner activity.
     *
     * @param date The selected date.
     * @param currUser The UID of the user logged-in/registered.
     */
    public static void updateFocusModeComplete(String date, String currUser) {
        DatabaseReference ref = reference.child(currUser).child(date);

        ref.child(taskCompleted).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                long currCount = (long) snapshot.getValue();
                long newCount = currCount + 1;

                ref.child(taskCompleted).setValue(newCount);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Focus Mode", "Task Completed Update Failed: " + error);
            }
        });
    }

    /**
     * Updates the number of task added in the taskAdded directory when a user adds a task in the Planner activity.
     *
     * @param date The selected date.
     * @param currUser The UID of the user logged-in/registered.
     * @param duration The duration of the task added.
     */
    public static void updateFocusModeAdd(String date, String currUser, int duration) {
        DatabaseReference ref = reference.child(currUser).child(date);

        ref.child(taskAdded).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                long currCount = (long) snapshot.getValue();
                long newCount = currCount + 1;

                ref.child(taskAdded).setValue(newCount);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Focus Mode", "Task Added Update Failed: " + error);
            }
        });

        updateTotalDuration(date, currUser, duration, 0);
    }

    /**
     * Updates the number of task added in the taskAdded directory when a user deleted a task in the Planner activity.
     *
     * @param date The selected date.
     * @param currUser The UID of the user logged-in/registered.
     * @param duration The duration of the task added.
     */
    public static void updateFocusModeDelete(String date, String currUser, int duration) {
        DatabaseReference ref = reference.child(currUser).child(date);

        ref.child(taskAdded).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                long currCount = (long) snapshot.getValue();
                long newCount = currCount <= 0 ? 0 : currCount - 1;

                ref.child(taskAdded).setValue(newCount);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.i("Focus Mode", "Task Added Update Failed: " + error);
            }
        });

        updateTotalDuration(date, currUser, 0, duration);
    }

    /**
     * Updates the total duration of all activities planned for the selected date.
     *
     * @param date The selected date.
     * @param currUser The UID of the user logged-in/registered.
     * @param nDuration The duration of the new task.
     * @param oDuration The duration of the old tasks.
     */
    public static void updateTotalDuration(String date, String currUser, int nDuration, int oDuration) {
        DatabaseReference ref = reference.child(currUser).child(date);

        ref.child(totalDuration).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                long currDuration = (long) snapshot.getValue();
                long newDuration = (currDuration - oDuration + nDuration) <= 0 ? 0 : (currDuration - oDuration + nDuration);

                ref.child(totalDuration).setValue(newDuration);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Focus Mode", "Total Duration Update Failed: " + error);
            }
        });
    }

    /**
     * Check if user has unlocked Focus Mode.
     *
     * @param date The selected date.
     * @param currUser The UID of the user logged-in/registered.
     * @param exp Amount of exp to be awarded based on duration of completed task.
     * @param context The current context when the method is executed.
     * @param duration The duration of the task completed.
     */
    public static void checkUnlockFocusMode(String date, String currUser, long exp, Context context, long duration) {
        DatabaseReference ref = reference.child(currUser);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                long currCount = (long) snapshot.child(date).child(taskCompleted).getValue();
                long taskCount = (long) snapshot.child(date).child(taskAdded).getValue();
                long totalDura = (long) snapshot.child(date).child(totalDuration).getValue();
                String dateString = (String) snapshot.child(lastFocusProc).getValue();
                String todayDate = TodoMainActivity.getTodaysDate();

                if (currCount != taskCount) { PetsUpdateStatus.updateExp(exp, currUser); }

                else {
                    if (!dateString.equals(todayDate)) {
                        Log.i("Focus Mode", "Focus Mode Unlocked");

                        double scaling = ((double) totalDura) / 100.0;
                        double scaledExp = scaling * (double) bonusExp;
                        long finalExp = (long) scaledExp + exp;
                        ref.child(lastFocusProc).setValue(todayDate);

                        PetsUpdateStatus.updateExp(finalExp, currUser);
                        AchievementMainActivity.giveAchievement(currUser, context, R.layout.achievement_award_fmspecial, duration);
                    } else {
                        Log.i("Focus Mode", "Redeemed Before");

                        PetsUpdateStatus.updateExp(exp, currUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Focus Mode", "Update Failed: " + error);
            }
        });
    }
}
