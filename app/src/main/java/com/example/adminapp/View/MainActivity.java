package com.example.adminapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.adminapp.R;
import com.example.adminapp.View.InsertCourse.InsertCourseActivity;
import com.example.adminapp.View.InsertTutor.InsertTutorActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnInsert, btnUpdate,btnInsertTutor,btnUpdateTutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInsert=(Button)findViewById(R.id.btnInsertCourse);
        btnUpdate=(Button)findViewById(R.id.btnUpdateCourse);
        btnInsertTutor=(Button)findViewById(R.id.btnInsertTutor);
        btnUpdateTutor=(Button)findViewById(R.id.btnUpdateTutor);
        onClickInsert();
        onClickUpdate();
        onClickInsertTutor();
        onClickUpdateTutor();
    }

    private void onClickUpdateTutor() {
        btnUpdateTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,UpdateTutorActivity.class));
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
}
