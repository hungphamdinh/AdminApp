package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnInsert, btnUpdate,btnInsertTutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInsert=(Button)findViewById(R.id.btnInsertCourse);
        btnUpdate=(Button)findViewById(R.id.btnUpdateCourse);
        btnInsertTutor=(Button)findViewById(R.id.btnInsertTutor);
        onClickInsert();
        onClickUpdate();
        onClickInsertTutor();
    }

    private void onClickInsertTutor() {
        btnInsertTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,InsertTutorActivity.class));

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
}
