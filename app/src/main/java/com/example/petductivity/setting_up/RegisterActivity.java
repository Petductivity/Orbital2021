package com.example.petductivity.setting_up;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.MainActivity;
import com.example.petductivity.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This class encapsulates the information about the Registration activity.
 *
 * @author Team Petductivity
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * UI elements for RegisterActivity layout.
     */
    private EditText email, name, password, confirmPassword;
    private Button registerButton;
    private TextView loginHere;
    private ProgressBar bar;
    private static String selectedUsername;

    /**
     * Firebase component.
     */
    private FirebaseAuth fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // setup the UI for this activity
        setUpUI();

        // connects to the firebase of this app
        fb = FirebaseAuth.getInstance();

        // user already logged in -> sent them to the next activity
        // user not logged in yet -> remains on the registration page
        if (fb.getCurrentUser() != null) {
            isUserEmailVerified();
        }

        // register button pressed
        registerButton.setOnClickListener(listener -> {
            String mEmail = email.getText().toString().trim();
            String mName = name.getText().toString().trim();
            String pw = password.getText().toString().trim();
            String cpw = confirmPassword.getText().toString().trim();

            // check that all required fields has been filled
            if (TextUtils.isEmpty(mEmail)) {
                email.setError("Email is Required!");
                return;
            }

            if (TextUtils.isEmpty(mName)) {
                name.setError("Name is required");
                return;
            }

            if (mName.length() < 4 || mName.length() > 10) {
                name.setError("Name has to be between 4 to 10 characters");
                return;
            }

            if (TextUtils.isEmpty(pw)) {
                password.setError("Password is Required!");
                return;
            }

            if (pw.length() < 6) {
                password.setError("Password must be atleast 4 characters long");
                return;
            }

            if (TextUtils.isEmpty(cpw)) {
                confirmPassword.setError("Please Fill in this field!");
                return;
            }

            if (!pw.equals(cpw)) {
                password.setError("Passwords does not match!");
                confirmPassword.setError("Passwords does not match!");
                return;
            } else {
                password.setError(null);
                confirmPassword.setError(null);
            }


            // notify the user that the system is creating the account
            bar.setVisibility(View.VISIBLE);

            // register the user account in the firebase
            createUserAcc(mEmail, pw);
            selectedUsername = mName;
        });


        // already have acc line pressed
        loginHere.setOnClickListener(listener -> {
            // notify the user that he has pressed on this line
            loginHere.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            // bring user to the login page
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }

    /**
     * Set up the UI elements used for this layout.
     */
    private void setUpUI() {
        email = findViewById(R.id.emailEditText);
        name = findViewById(R.id.nameEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginHere = findViewById(R.id.loginHereTextView);
        bar = findViewById(R.id.progressBarRegister);
    }

    /**
     * Check if the user has verified his/her account.
     */
    private void isUserEmailVerified() {
        if (!fb.getCurrentUser().isEmailVerified()) {
            // bring user to the verification page
            startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
        } else {
            // bring user to the home page
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        finish();
    }

    /**
     * Create the user account in the firebase.
     *
     * @param email Email address of the user.
     * @param pw Password selected by the user.
     */
    private void createUserAcc(String email, String pw) {
        fb.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(task -> {
            // check if the account is created successfully
            if (task.isSuccessful()) {
                Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                // bring user to the verification page
                startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Unexpected Error has Occurred!", Toast.LENGTH_SHORT).show();
                Log.d("Registration", "Firebase unable to create new account due to: " + task.getException().toString());
                bar.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Return the user's account name.
     *
     * @return A string representing the user's account name.
     */
    public static String retreiveUsername() {
        return selectedUsername;
    }
}