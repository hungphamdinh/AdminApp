package com.example.adminapp.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.adminapp.Model.Request;
import com.example.adminapp.R;
import com.example.adminapp.View.DetailUpdateCourse.DetailUpdateCourseActivity;
import com.example.adminapp.ViewHolder.CourseViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference course;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> adapter;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> searchAdapter;

    private String docKey;
    private List<String> suggestList=new ArrayList<>();
    private MaterialSearchBar materialSearchBar;
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
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.search_bar);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        //materialSearchBar.setTextColor(Color.parseColor("#000"));
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<String> suggest=new ArrayList<String>();
                for(String search:suggestList ){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);//set suggest
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When searchbar is close, becoming original adapter
                if(!enabled)
                    recyclerMenu.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search is finish, show result
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter=new FirebaseRecyclerAdapter<Course, CourseViewHolder>(
                Course.class,
                R.layout.course_layout,
                CourseViewHolder.class,
                course.orderByChild("courseName").equalTo(text.toString())//compare by itemName
        ) {
            @Override
            protected void populateViewHolder(CourseViewHolder viewHolder, Course model, int position) {
                viewHolder.txtName.setText(model.getCourseName());
                viewHolder.txtPrice.setText("Giá: " + model.getPrice());
                viewHolder.txtDescript.setText(model.getDescript());
                final Course local=model;
                loadCourseDoc(searchAdapter.getRef(position).getKey(),viewHolder);

            }
        };
        recyclerMenu.setAdapter(searchAdapter);//Set adapter for RecycleView when search Result
    }
    private void loadSuggest() {
        course.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren() ){
                            Course courseItem=postSnapshot.getValue(Course.class);
                            suggestList.add(courseItem.getCourseName());//load list of suggest item

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
//                ArrayList<String> listIntent = new ArrayList<>();
//                listIntent.add(key);
            //    listIntent.add(docKey);
                intent.putExtra("DetailList", key);
                //intent.putExtra("DetailList", (Parcelable) listIntent);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
          //  setOnClickItem(item.getOrder(),docKey);
            // showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else {
            onRequest(item);
            onDoc(item);
        }
        deleteCourse(adapter.getRef(item.getOrder()).getKey());
        return super.onContextItemSelected(item);

    }

    private void onDoc(MenuItem item) {
        DatabaseReference docRef= FirebaseDatabase.getInstance().getReference("Doc");
        docRef.orderByChild("courseId").equalTo(adapter.getRef(item.getOrder()).getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Doc doc = childSnap.getValue(Doc.class);
                    docKey=childSnap.getKey();
                    deletDoc(docKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onRequest(MenuItem item) {
        DatabaseReference requestRef= FirebaseDatabase.getInstance().getReference("Requests");
        requestRef.orderByChild("courseId").equalTo(adapter.getRef(item.getOrder()).getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Request request = childSnap.getValue(Request.class);
                    String requestKey=childSnap.getKey();
                    deleteRequest(requestKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deletDoc(String key) {
        try {
            if(key!=null) {
                DatabaseReference doc=FirebaseDatabase.getInstance().getReference("Doc");
                doc.child(key).removeValue();
            }
            else {
                recyclerMenu.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex){
            Log.e("Error",ex.getMessage());
        }

    }
    private void deleteRequest(final String key) {
        try {
            if(key!=null) {
                DatabaseReference doc=FirebaseDatabase.getInstance().getReference("Requests");
                        doc.child(key).removeValue();

            }
            else {
                recyclerMenu.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex){
            Log.e("Error",ex.getMessage());
        }

    }
    private void deleteCourse(String key) {
        try {
            if(key!=null) {
                course.child(key).removeValue();
            }
            else {
                recyclerMenu.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex){
            Log.e("Error",ex.getMessage());
        }

    }
}


