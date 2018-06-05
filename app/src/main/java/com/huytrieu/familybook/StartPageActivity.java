package com.huytrieu.familybook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.huytrieu.chatstudentapp.R;

public class StartPageActivity extends AppCompatActivity {
    Button already_have_account_button,need_account_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        already_have_account_button = findViewById(R.id.already_have_account_button);
        need_account_button = findViewById(R.id.need_account_button);
        already_have_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(StartPageActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        need_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(StartPageActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
