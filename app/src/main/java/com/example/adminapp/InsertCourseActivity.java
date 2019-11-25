package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class InsertCourseActivity extends AppCompatActivity {
    private EditText edtName,edtPrice,edtDiscount,edtSchedule,edtDescript,edtPhone;
    private Button btnInsert;
    private DatabaseReference courseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_course);
        edtName=(EditText)findViewById(R.id.edtCourseName);
        edtPrice=(EditText)findViewById(R.id.edtCoursePrice);
        edtDiscount=(EditText)findViewById(R.id.edtCourseDiscount);
        edtSchedule=(EditText)findViewById(R.id.edtCourseSchedule);
        edtDescript=(EditText)findViewById(R.id.edtCourseDescript);
        edtPhone=(EditText)findViewById(R.id.edtCoursePhone);
        btnInsert=(Button)findViewById(R.id.btnInsert);
        insertCourse();
    }
    private void insertCourse(){
        courseRef= FirebaseDatabase.getInstance().getReference("Course");
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTemp=edtName.getText().toString();
                String priceTemp=edtPrice.getText().toString();
                String discountTemp=edtDiscount.getText().toString();
                String scheduleTemp=edtSchedule.getText().toString();
                String descriptTemp=edtDescript.getText().toString();
                String phoneTemp=edtPhone.getText().toString();
                HashMap<String, String> map = new HashMap<>();
                //User user = new User(usernameTemp, passwordTemp,"");
                map.put("courseName", nameTemp);
                map.put("descript", descriptTemp);
                map.put("discount", discountTemp);
                map.put("schedule", scheduleTemp);
                map.put("price", priceTemp);
                map.put("tutorPhone", phoneTemp);
                map.put("isBuy", "false");
                courseRef.push().setValue(map);
                Toast.makeText(InsertCourseActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
