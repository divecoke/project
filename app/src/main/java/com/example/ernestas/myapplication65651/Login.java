package com.example.ernestas.myapplication65651;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button bLogin;
    private EditText etEmail, etPassword;
    private TextView tvRegisterHere;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            //Profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        progressdialog = new ProgressDialog(this);

        bLogin = (Button) findViewById(R.id.bLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegisterHere = (TextView) findViewById(R.id.tvRegisterHere);

        bLogin.setOnClickListener(this);
        tvRegisterHere.setOnClickListener(this);

    }
    private void tvRegisterOn() {
        Intent goToRegister = new Intent(getApplicationContext(),Register.class);
        startActivity(goToRegister);
        finish();
    }

    public void loginRequest() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter the username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressdialog.setMessage("Logging in please wait..");
        progressdialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressdialog.dismiss();
                if(task.isSuccessful()) {
                    //start main activity
                    finish();
                    Toast.makeText(Login.this, "You are logged in successfull", Toast.LENGTH_SHORT).show();
                    Intent goInLogin = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(goInLogin);
                } else {
                    Toast.makeText(Login.this, "Something is wrong please check information and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        if(v == bLogin) {
            loginRequest();
        }
        if(v == tvRegisterHere) {
            tvRegisterOn();
        }
    }
}
