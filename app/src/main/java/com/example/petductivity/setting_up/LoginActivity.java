package com.example.petductivity.setting_up;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petductivity.MainActivity;
import com.example.petductivity.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This class encapsulates the information about the Login Activity.
 *
 * @author Team Petductivity
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * UI elements for LoginActivity layout.
     */
    private EditText email, password;
    private Button loginButton;
    private TextView newUser, forgetPassword;
    private ProgressBar bar;

    /**
     * Firebase component.
     */
    private FirebaseAuth fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // setup the UI for this activity
        setUpUI();

        // connect to the firebase for this app
        fb = FirebaseAuth.getInstance();

        // check if user is already logged in
        // if logged in, sent them to the next activity page
        // otherwise, stay on login page
        if (fb.getCurrentUser() != null) {
            // check if the user's email has been verified
            isUserEmailVerified();
        }

        // login button pressed
        loginButton.setOnClickListener(listener -> {
            String name = email.getText().toString().trim();
            String pw = password.getText().toString().trim();

            // validate user data
            if (TextUtils.isEmpty(name)) {
                email.setError("Email is Required!");
                return;
            }

            if (TextUtils.isEmpty(pw)) {
                password.setError("Password is Required!");
                return;
            }


            // notify the user that the system is processing his/her login request
            bar.setVisibility(View.VISIBLE);

            // authenticate the user
            authenticateUser(name, pw);
        });


        // create new account line pressed
        newUser.setOnClickListener(listener -> {
            // notify the user that he has clicked on this button
            newUser.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            // bring user to the registration page
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });

        // forget password line pressed
        forgetPassword.setOnClickListener(listener -> {
            // start pop-up message
            setUpResetPasswordPopUp();
        });
    }

    /**
     * Setup UI for Login Page.
     */
    private void setUpUI() {
        email = findViewById(R.id.emailEditTextReset);
        password = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.registerButton);
        newUser = findViewById(R.id.newUserTextView);
        forgetPassword = findViewById(R.id.forgetPasswordTextView);
        bar = findViewById(R.id.progressBarRegister);
    }

    /**
     * Checks if user has verified his/her account.
     */
    private void isUserEmailVerified() {
        // if email not verified, bring user to the verification page
        // if email verified, bring user to the home page
        if (!fb.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
        } else {
            // sync the user to the main activity of the app
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        finish();
    }

    /**
     * Setup the UI for reset password popup.
     */
    private void setUpResetPasswordPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View resetView = inflater.inflate(R.layout.activity_reset_password, null);


        alert.setTitle("Confirm Reset Password?")
                .setMessage("Enter email to reset password.")
                .setPositiveButton("Reset", (dialog, which) -> {
                    EditText email = resetView.findViewById(R.id.emailEditTextReset);
                    String address = email.getText().toString().trim();

                    // validate email address
                    if (TextUtils.isEmpty(address)) {
                        email.setError("Required Field!");
                    } else {
                        // send link to reset password
                        fb.sendPasswordResetEmail(address).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Link sent to email!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Unexpected Error Occurred!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Reset Password", task.getException().toString());
                            }
                        });
                    }
                }).setNegativeButton("Cancel", null)
                .setView(resetView) // insert the layout into the dialog
                .create()
                .show();
    }

    /**
     * Authenticate the user
     *
     * @param email Email Address of the user.
     * @param pw Password selected by user.
     */
    private void authenticateUser(String email, String pw) {
        fb.signInWithEmailAndPassword(email, pw).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                // check if the user's email has been verified
                isUserEmailVerified();
            } else {
                Toast.makeText(LoginActivity.this, "Unexpected Error has Occurred! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Login_Authentication", task.getException().toString());
                bar.setVisibility(View.INVISIBLE);
            }
        });
    }

}
