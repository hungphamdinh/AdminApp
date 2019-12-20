package com.example.adminapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Notification.Token;
import com.example.adminapp.View.UserList.UpdateUserActivity;
import com.example.adminapp.View.CourseList.CourseActivity;
import com.example.adminapp.R;
import com.example.adminapp.View.InsertCourse.InsertCourseActivity;
import com.example.adminapp.View.InsertTutor.InsertTutorActivity;
import com.example.adminapp.View.TutorList.UpdateTutorActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    private Button btnInsert, btnUpdate,btnInsertTutor,btnUpdateTutor,btnUpdateUser,btnSignOut;
    private String userPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInsert=(Button)findViewById(R.id.btnInsertCourse);
        btnUpdate=(Button)findViewById(R.id.btnUpdateCourse);
        btnInsertTutor=(Button)findViewById(R.id.btnInsertTutor);
        btnUpdateTutor=(Button)findViewById(R.id.btnUpdateTutor);
        btnUpdateUser=(Button)findViewById(R.id.btnUpdateUser);
        btnSignOut=(Button)findViewById(R.id.btnSignOut);
        if (getIntent() != null)
            userPhone = getIntent().getStringExtra("phoneUser");
        if (!userPhone.isEmpty() && userPhone != null) {
            if (Common.isConnectedToInternet(this)) {
                onClickInsert();
                onClickUpdate();
                onClickInsertTutor();
                onClickUpdateTutor();
                onClickUpdateUser();
                onClickSignOut();
                updateToken(userPhone, FirebaseInstanceId.getInstance().getToken());
            } else {
                Toast.makeText(MainActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void onClickSignOut() {
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void onClickUpdateUser() {
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, UpdateUserActivity.class);
                intent.putExtra("phoneUser",userPhone);
                startActivity(intent);
            }
        });
    }

    private void onClickUpdateTutor() {
        btnUpdateTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UpdateTutorActivity.class));
            }
        });
    }

    private void onClickInsertTutor() {
        btnInsertTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InsertTutorActivity.class));

            }
        });
    }

    private void onClickInsert() {
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InsertCourseActivity.class));
            }
        });
    }
    private void onClickUpdate() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CourseActivity.class));
            }
        });
    }
    public void updateToken(String userId,String token){
        DatabaseReference tokenRef= FirebaseDatabase.getInstance().getReference("Tokens");
        Token newToken=new Token(token);
        tokenRef.child(userId).setValue(newToken);
    }
}
