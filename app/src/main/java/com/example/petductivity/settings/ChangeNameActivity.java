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
import com.example.petductivity.setting_up.VerificationActivity;
import com.example.petductivity.pet.PetsStatusActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class encapsulates the information when user changes their account name.
 *
 * @author Team Petductivity
 */
public class ChangeNameActivity extends AppCompatActivity {
    /**
     * UI elements for ChangeNameActivity layout.
     */
    private EditText newName;
    private Button change;
    private TextView backArrow;

    /**
     * Firebase component.
     */
    private FirebaseAuth fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        // setup UI for this activity
        setUpUI();

        // connects to the firebase for this app
        fb = FirebaseAuth.getInstance();

        // change name button pressed
        change.setOnClickListener(v -> {
            String mNewName = newName.getText().toString().trim();

            if (TextUtils.isEmpty(mNewName)) {
                newName.setError("Required Field!");
            }

            if (mNewName.length() <  4 || mNewName.length() > 10) {
                newName.setError("New Account Name must be between 4 to 10 characters!");
            }

            DatabaseReference ref = FirebaseDatabase.getInstance(VerificationActivity.databaseURL)
                    .getReference()
                    .child(PetsStatusActivity.pokesysDir)
                    .child(fb.getCurrentUser().getUid());

            // changes the account name stored in the database.
            ref.child(VerificationActivity.nameDir).setValue(mNewName).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ChangeNameActivity.this, "Account Name Changed Successfully!", Toast.LENGTH_SHORT).show();
                    Log.i("Registration", "User's name added successfully");
                } else {
                    Toast.makeText(ChangeNameActivity.this,
                            "Unexpected Error Occurred while changing account name!\nPlease try again later.",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Registration", "Failed to add user's name due to: " + task.getException().toString());
                }
            });

        });

        // back arrow button clicked, brings user back to settings page dashboard
        backArrow.setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * Set up the UI elements used for this layout.
     */
    private void setUpUI() {
        newName = findViewById(R.id.newNameEditText);
        change = findViewById(R.id.changeNameButton);
        backArrow = findViewById(R.id.arrowClickChangeName);
    }
}