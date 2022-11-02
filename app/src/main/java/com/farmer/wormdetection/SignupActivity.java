package com.farmer.wormdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    EditText userName, password, Passwordc,useremail;
    TextView submit_btn, login2;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        useremail = findViewById(R.id.userEmail);
        userName = findViewById(R.id.userName);
        login2 = findViewById(R.id.login2);
        Passwordc = findViewById(R.id.Passwordc);
        password = findViewById(R.id.Password2);
        submit_btn = findViewById(R.id.submit_btn);
        progressBar = findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();

        login2.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        submit_btn.setOnClickListener(view -> {
            submit_btn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            final String email = useremail.getText().toString();
            final String pwd = password.getText().toString();
            final String pwd1 = Passwordc.getText().toString();
            final String name = userName.getText().toString();


            if (TextUtils.isEmpty(name)) {
                userName.setError("Type Name");
                submit_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else if (TextUtils.isEmpty(email)) {
                submit_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                userName.setError("Type Email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(useremail.getText().toString()).matches()) {
                submit_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                useremail.setError("Type Valid E-mail");
            } else if (TextUtils.isEmpty(pwd)) {
                submit_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                password.setError("Type password");
            } else if (pwd.length() != 6) {
                submit_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Passwordc.setError("Password must have 6 or more Digits");
            } else if (TextUtils.isEmpty(pwd1)) {
                submit_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Passwordc.setError("Type password");
            } else if (!pwd1.equals(pwd)) {
                submit_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Passwordc.setError("Type Same passwords");
            } else {
                mAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                submit_btn.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);

                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                submit_btn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                String msg = task.getException().toString();
                                Toast.makeText(SignupActivity.this,
                                        "Error: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}