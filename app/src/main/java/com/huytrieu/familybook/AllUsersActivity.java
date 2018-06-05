package com.huytrieu.familybook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.huytrieu.familybook.Model.AllUsers;
import com.huytrieu.chatstudentapp.R;
import com.huytrieu.familybook.ViewHolder.AllUsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllUsersActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView allUsersList;
    DatabaseReference allDatabaseUsersReference;

    EditText SearchInputText;
    ImageButton SearchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        toolbar = findViewById(R.id.all_users_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchButton = findViewById(R.id.search_friend_button);
        SearchInputText = findViewById(R.id.search_input_text);

        allUsersList = findViewById(R.id.all_user_recyclerView);
        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(this));

        allDatabaseUsersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        allDatabaseUsersReference.keepSynced(true);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchUserName = SearchInputText.getText().toString();
                if (TextUtils.isEmpty(searchUserName)){
                    Toast.makeText(AllUsersActivity.this, "Viết tên bạn cần tìm....", Toast.LENGTH_SHORT).show();
                }
                SearchForPeopleAndFriend(searchUserName);
            }
        });
    }


    private void SearchForPeopleAndFriend(String searchUserName) {
        Toast.makeText(this, "Đang tìm kiếm", Toast.LENGTH_SHORT).show();
        Query searchPeopleAndFriend = allDatabaseUsersReference.orderByChild("user_name").startAt(searchUserName)
                .endAt(searchUserName+"\uf8ff");
        FirebaseRecyclerAdapter<AllUsers,AllUsersViewHolder> adapter = new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>(
                AllUsers.class,
                R.layout.all_users_display_layout,
                AllUsersViewHolder.class,
                searchPeopleAndFriend
        ) {
            @Override
            protected void populateViewHolder(AllUsersViewHolder viewHolder, AllUsers model, final int position) {
                viewHolder.setUser_name(model.getUser_name());
                viewHolder.setUser_status(model.getUser_status());
                viewHolder.setUser_thumb_image(getApplicationContext(),model.getUser_thumb_image());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(position).getKey();
                        Intent profileIntent = new Intent(AllUsersActivity.this,ProfileActivity.class);
                        profileIntent.putExtra("visit_user_id",visit_user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };
        allUsersList.setAdapter(adapter);
    }
}
