package com.example.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText contactInfoInput;
    private EditText fullNameInput;
    private EditText usernameSignUpInput;
    private EditText passwordSignUpInput;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        contactInfoInput = findViewById(R.id.email_et);
        fullNameInput = findViewById(R.id.fullname_et);
        usernameSignUpInput = findViewById(R.id.username_signup_et);
        passwordSignUpInput = findViewById(R.id.password_signup_et);
        signupBtn = findViewById(R.id.signupscreen_btn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String contactInfo = contactInfoInput.getText().toString();
                final String fullName = fullNameInput.getText().toString();
                final String username = usernameSignUpInput.getText().toString();
                final String password = passwordSignUpInput.getText().toString();

                userSignUp(contactInfo, fullName, username, password);
            }
        });
    }

    public void userSignUp(String contactInfo, String fullName, String username, String password) {
        // Create the ParseUser
        ParseUser newUser = new ParseUser();

        // Set core properties
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(contactInfo);

        // Set custom properties
        // newUser.put("full name", fullName);

        // Invoke signUpInBackground
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("SignupActivity", "Sign-up successful!");
                    final Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("SignupActivity", "Sign-up failure.");
                    e.printStackTrace();
                }
            }
        });

    }
}
