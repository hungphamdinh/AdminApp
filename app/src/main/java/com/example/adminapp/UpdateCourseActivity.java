package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Model.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateCourseActivity extends AppCompatActivity {
    private EditText edtName,edtPrice,edtDiscount,edtSchedule,edtDescript,edtPhone;
    private Button btnUpdate;
    private DatabaseReference courseRef;
    private ArrayList<String> courseDetailList;
    private String courseID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);
        edtName=(EditText)findViewById(R.id.edtCourseNameU);
        edtPrice=(EditText)findViewById(R.id.edtCoursePriceU);
        edtDiscount=(EditText)findViewById(R.id.edtCourseDiscountU);
        edtSchedule=(EditText)findViewById(R.id.edtCourseScheduleU);
        edtDescript=(EditText)findViewById(R.id.edtCourseDescriptU);
        edtPhone=(EditText)findViewById(R.id.edtCoursePhoneU);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        if (getIntent() != null) {
            courseDetailList = getIntent().getStringArrayListExtra("DetailList");

        }
        if (!courseDetailList.isEmpty() && courseDetailList != null) {
            if (Common.isConnectedToInternet(this)) {
                courseID=courseDetailList.get(4);
                loadDetaillCourse(courseID);
            }
        }

    }
    private void loadDetaillCourse(String courseId) {
        courseRef= FirebaseDatabase.getInstance().getReference("Course");
        courseRef.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course = dataSnapshot.getValue(Course.class);
                //Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodImage);
                edtName.setText(course.getCourseName());
                edtPrice.setText(course.getPrice());
                edtDescript.setText(course.getDescript());
                edtDiscount.setText(course.getDiscount());
                edtSchedule.setText(course.getSchedule());
                edtPhone.setText(course.getTutorPhone());
                //            txtCourseDoc.setText(course.getCourseDoc());
                String tutorPhone = course.getTutorPhone();
//                loadDetailTutor(tutorPhone);
                HashMap<Object, Object> orderMap = new HashMap<>();
                orderMap.put("courseName", course.getCourseName());
                orderMap.put("courseId", courseDetailList.get(4));
                orderMap.put("discount", course.getDiscount());
                orderMap.put("price", course.getPrice());
                orderMap.put("schedule", course.getSchedule());
                orderMap.put("tutorPhone", course.getTutorPhone());
            }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            }
    private void updateCourse(){

    }
}
