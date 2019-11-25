package com.example.adminapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.Model.Course;
import com.example.adminapp.ViewHolder.CourseViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference course;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        database = FirebaseDatabase.getInstance();
        course = database.getReference("Course");
        recyclerMenu = (RecyclerView) findViewById(R.id.listCourse);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        loadListCourse();
    }

    private void loadListCourse() {
        adapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>
                (Course.class, R.layout.course_layout,
                        CourseViewHolder.class,
                        course) {
            @Override
            protected void populateViewHolder(final CourseViewHolder viewHolder, final Course model, int position) {
                viewHolder.txtName.setText(model.getCourseName());
                viewHolder.txtPrice.setText("Giá: " + model.getPrice());
                viewHolder.txtDescript.setText(model.getDescript());
                //viewHolder.txtTutorName.setText("Giảng viên: " + model.getTutorPhone());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(CourseActivity.this, UpdateCourseActivity.class);
                        ArrayList<String> listIntent = new ArrayList<>();
                        listIntent.add(viewHolder.txtName.getText().toString());
                        listIntent.add(viewHolder.txtPrice.getText().toString());
                        listIntent.add(viewHolder.txtTutorName.getText().toString());
                        listIntent.add(viewHolder.txtDescript.getText().toString());
                        listIntent.add(adapter.getRef(position).getKey());
                        intent.putStringArrayListExtra("DetailList", listIntent);
                        //intent.putExtra("DetailList", (Parcelable) listIntent);

                        startActivity(intent);
                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }
}


