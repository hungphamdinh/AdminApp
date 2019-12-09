package com.example.adminapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.Model.Course;
import com.example.adminapp.Model.Doc;
import com.example.adminapp.View.DetailUpdateCourse.DetailUpdateCourseActivity;
import com.example.adminapp.ViewHolder.CourseViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference course;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> adapter;
    private String docKey;
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
                loadCourseDoc(adapter.getRef(position).getKey(),viewHolder);

            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }


    private void loadCourseDoc(final String key, final CourseViewHolder viewHolder) {
        DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
        docRef.orderByChild("courseId").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Doc doc = childSnap.getValue(Doc.class);
                        docKey=childSnap.getKey();
                    viewHolder.txtDoc.setText(doc.getDocName());
                    setOnClickItem(viewHolder, key);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setOnClickItem(CourseViewHolder viewHolder, final String key) {
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(CourseActivity.this, DetailUpdateCourseActivity.class);
                ArrayList<String> listIntent = new ArrayList<>();
                listIntent.add(key);
                listIntent.add(docKey);
                intent.putStringArrayListExtra("DetailList", listIntent);
                //intent.putExtra("DetailList", (Parcelable) listIntent);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
          //  setOnClickItem(item.getOrder(),docKey);
            // showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else {
            deleteCourse(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);

    }

    private void deleteCourse(String key) {
        if(key!=null){
            course.child(key).removeValue();
        }
        else{
            Log.e("Error key",key);
        }
    }
}


