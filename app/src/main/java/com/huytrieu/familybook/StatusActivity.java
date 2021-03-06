package com.huytrieu.familybook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huytrieu.chatstudentapp.R;

public class StatusActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText StatusInput;
    Button SaveChangesButton;
    DatabaseReference changeStatusRef;
    ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        changeStatusRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        toolbar = findViewById(R.id.status_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar = new ProgressDialog(this);

        StatusInput = findViewById(R.id.edt_status);
        SaveChangesButton = findViewById(R.id.btn_save_changes);
        String old_status = getIntent().getExtras().get("user_status").toString();
        StatusInput.setText(old_status);
        SaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_status = StatusInput.getText().toString();
                ChangeProfileStatus(new_status);
            }
        });

    }

    private void ChangeProfileStatus(String new_status) {
        if (TextUtils.isEmpty(new_status)){
            Toast.makeText(this, "Please write status", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Changes profile status");
            loadingBar.setMessage("Please wait.....");
            loadingBar.show();
            changeStatusRef.child("user_status").setValue(new_status)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent settingIntent = new Intent(StatusActivity.this, SettingsActivity.class);
                                startActivity(settingIntent);
                                Toast.makeText(StatusActivity.this, "Profile status updated successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(StatusActivity.this, "Error occured...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}