package com.example.petductivity.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class encapsulates the information when user changes their account password.
 *
 * @author Team Petductivity
 */
public class ChangePasswordActivity extends AppCompatActivity {
    /**
     * UI elements for ChangePasswordActivity layout.
     */
    private EditText password, confirmPassword;
    private Button change;
    private TextView backArrow;

    /**
     * Firebase component.
     */
    private FirebaseAuth fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // setup UI for this activity
        setUpUI();

        // connects to the firebase for this app
        fb = FirebaseAuth.getInstance();

        // get the current user
        FirebaseUser user = fb.getCurrentUser();

        // change password button pressed
        change.setOnClickListener(v -> {
            String pw = password.getText().toString().trim();
            String cpw = confirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(pw) || TextUtils.isEmpty(cpw)) {
                password.setError("Required Field!");
            }

            if (pw.length() < 6) {
                password.setError("New Password must have atleast 6 characters!");
            }

            if (!pw.equals(cpw)) {
                password.setError("Passwords does not match!");
                confirmPassword.setError("Passwords does not match!");
            }

            // updates the password stored in the database
            user.updatePassword(pw).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Successfully Changed Password.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Change Password", task.getException().toString());
                    Toast.makeText(getApplicationContext(), "Unable to update password due to unexpected error. Please re-login again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // back arrow button clicked, brings user back to setting page dashboard
        backArrow.setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * Set up the UI elements used for this layout.
     */
    private void setUpUI() {
        password = findViewById(R.id.newPasswordEditText);
        confirmPassword = findViewById(R.id.confirmNewPasswordEditText);
        change = findViewById(R.id.changePasswordButton);
        backArrow = findViewById(R.id.arrowClickChangePassword);
    }
}