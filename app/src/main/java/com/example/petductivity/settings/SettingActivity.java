package com.example.petductivity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.achievement.AchievementMainActivity;
import com.example.petductivity.focus_mode.FocusModeActivity;
import com.example.petductivity.setting_up.LoginActivity;
import com.example.petductivity.R;
import com.example.petductivity.setting_up.VerificationActivity;
import com.example.petductivity.pet.PetsStatusActivity;
import com.example.petductivity.planner.PlannerWeekActivity;
import com.example.petductivity.task.TodoMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This encapsulates the information of the Settings Page.
 *
 * @author Team Petductivity
 */
public class SettingActivity extends AppCompatActivity {

    /**
     * UI elements for SettingActivity layout.
     */
    private TextView changePassword, changeName, help, aboutPetduct, feedback, deleteAcc, logout, backBtn;

    /**
     * Firebase component.
     */
    private FirebaseAuth fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // setup UI for this activity
        setUpUI();

        // connects to the firebase for this app
        fb = FirebaseAuth.getInstance();

        // back arrow button pressed
        backBtn.setOnClickListener(listener -> {
            // bring the user to the home page
            finish();
        });

        // change password tab pressed
        changePassword.setOnClickListener(listener -> {
            // bring user to the change password page
            startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
        });

        changeName.setOnClickListener(listener -> {
            startActivity(new Intent(getApplicationContext(), ChangeNameActivity.class));
        });

        help.setOnClickListener(listener -> {
            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
        });

        aboutPetduct.setOnClickListener(listener -> {
            startActivity(new Intent(getApplicationContext(), AboutPetductivityActivity.class));
        });

        feedback.setOnClickListener(listener -> setUpFeedbackPopUp());

        // delete account tab pressed
        deleteAcc.setOnClickListener(listener -> setUpDeleteAccPopUp());

        // logout tab pressed
        logout.setOnClickListener(listener -> {
            // sign the user out on firebase
            fb.signOut();

            // bring user to login page
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }

    /**
     * Set up the UI elements used for this layout.
     */
    private void setUpUI() {
        changePassword = findViewById(R.id.changePasswordTextView);
        changeName = findViewById(R.id.changeNameTextView);
        help = findViewById(R.id.helpTextViewSetting);
        aboutPetduct = findViewById(R.id.aboutPetductivityTextView);
        feedback = findViewById(R.id.feedbackTextView);
        deleteAcc = findViewById(R.id.deleteAccountTextViewSetting);
        logout = findViewById(R.id.logoutTextViewSetting);
        backBtn = findViewById(R.id.arrowClickSettings);
    }

    /**
     * Set up the UI elements used for the delete account popup.
     */
    private void setUpDeleteAccPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Permanently Delete Account?")
                .setMessage("Account data will also be deleted permanently once the account has been deleted\n\nConfirm Delete Account?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseUser user = fb.getCurrentUser();

                    // delete user account on firebase
                    assert user != null;
                    user.delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show();

                            //wipe data on firebase
                            wipeUserData(user);

                            // sign user out on firebase
                            fb.signOut();

                            // bring user to login page
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unexpected Error Occurred!\nTry Again Later.", Toast.LENGTH_SHORT).show();
                            Log.d("Delete Account", "Failed due to: " + task.getException().toString());
                        }
                    });
                }).setNegativeButton("No", null)
                .create()
                .show();
    }

    /**
     * Set up the UI elements used for the feedback popup.
     */
    private void setUpFeedbackPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.feedback, null);

        TextView hyperlink = view.findViewById(R.id.hyperlinkFeedback);
        hyperlink.setMovementMethod(LinkMovementMethod.getInstance());

        alert.setTitle("Have a feedback?")
                .setMessage("Click on the link below.")
                .setNegativeButton("Return", null)
                .setView(view)
                .create()
                .show();
    }

    /**
     * Deletes the user data in the database when user deletes his/her account.
     *
     * @param user The user to be deleted.
     */
    private void wipeUserData(FirebaseUser user) {
        String currUser = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance(VerificationActivity.databaseURL).getReference();
        ref.child(FocusModeActivity.focusModeDir).child(currUser).removeValue();
        ref.child(AchievementMainActivity.achievementsDir).child(currUser).removeValue();
        ref.child(PetsStatusActivity.pokesysDir).child(currUser).removeValue();
        ref.child(PlannerWeekActivity.plannerDir).child(currUser).removeValue();
        ref.child(TodoMainActivity.tasksDir).child(currUser).removeValue();

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}