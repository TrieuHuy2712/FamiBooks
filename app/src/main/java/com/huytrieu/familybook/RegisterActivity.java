package com.huytrieu.familybook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.huytrieu.chatstudentapp.R;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference database;
    private Toolbar mToolbar;
    EditText RegisterUserName,RegisterUserEmail,RegisterUserPassword;
    Button CreateAccountButton;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mToolbar =findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        RegisterUserName = findViewById(R.id.edt_name_register);
        RegisterUserEmail =findViewById(R.id.edt_email_register);
        RegisterUserPassword = findViewById(R.id.edt_password_register);
        CreateAccountButton = findViewById(R.id.create_account_button);
        loadingBar = new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = RegisterUserName.getText().toString();
                String email = RegisterUserEmail.getText().toString();
                String password = RegisterUserPassword.getText().toString();
                RegisterAccount(name,email,password);
            }
        });
    }

    private void RegisterAccount(final String name, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please write your email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Creating new account");
            loadingBar.setMessage("Please wait....");
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String DeviceToken = FirebaseInstanceId.getInstance().getToken();
                                String current_user_id = mAuth.getCurrentUser().getUid();
                                database = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
                                database.child("user_name").setValue(name);
                                database.child("user_status").setValue("Nothing");
                                database.child("user_image").setValue("default_profile");
                                database.child("device_token").setValue(DeviceToken);
                                database.child("user_thumb_image").setValue("default_image")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                    finish();
                                                }
                                            }
                                        });
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Error Occured , Try again....", Toast.LENGTH_SHORT).show();
                            }
                            loadingBar.dismiss();
                        }
                    });
        }
    }
}

