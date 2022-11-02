package com.farmer.wormdetection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity  {

    EditText userEmail,password;
    TextView login,signup2,google;
    FirebaseAuth mAuth;
    ProgressBar progressBar,progress;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail =  findViewById(R.id.userEmail);
        password = findViewById(R.id.pwd);
        login =  findViewById(R.id.login_bt2);
        signup2 =  findViewById(R.id.signup2);
        progressBar=findViewById(R.id.progressBar);
        progress=findViewById(R.id.progress);
        google=findViewById(R.id.google);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if (firebaseUser!=null ){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        signup2.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken("284257691080-qqvj332mp97e10fvviji473iraag3pt0.apps.googleusercontent.com").
                requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=gsc.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });

        login.setOnClickListener(view -> {
            login.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            String email = userEmail.getText().toString().trim();
            String pwd = password.getText().toString();
            if (TextUtils.isEmpty(email)) {
                login.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                userEmail.setError("Type E-mail");
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.getText().toString()).matches()) {
                login.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                userEmail.setError("Type Valid E-mail");}
            else if (TextUtils.isEmpty(pwd)) {
                login.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                password.setError("Type Password");
            }
            else {
                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful())
                            {
                                login.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                                Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(MainIntent);
                                finish();
                            } else
                            {
                                login.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                String msg = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                try {
                    progress.setVisibility(View.VISIBLE);

                    GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                    if (googleSignInAccount != null) {

                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),
                                null);
                        mAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                        } else {
                                            String msg = task.getException().toString();
                                            Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }}}