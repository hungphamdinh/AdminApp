package com.example.adminapp.Presenter.CourseList;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.adminapp.Model.Doc;
import com.example.adminapp.Model.Request;
import com.example.adminapp.ViewHolder.CourseViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CourseList {
    private ICourseListListener courseListListener;
    public CourseList(ICourseListListener courseListListener){
        this.courseListListener=courseListListener;
    }
    public void loadCourseDoc(final String key) {
        DatabaseReference docRef= FirebaseDatabase.getInstance().getReference("Doc");
        docRef.orderByChild("courseId").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Doc doc = childSnap.getValue(Doc.class);
                    String docKey=childSnap.getKey();
                    courseListListener.onClickItem(key,doc.getDocName());


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onRequest(String key) {
        DatabaseReference requestRef= FirebaseDatabase.getInstance().getReference("Requests");
        requestRef.orderByChild("courseId").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void onDoc(String key) {
        DatabaseReference docRef= FirebaseDatabase.getInstance().getReference("Doc");
        docRef.orderByChild("courseId").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Doc doc = childSnap.getValue(Doc.class);
                    String docKey=childSnap.getKey();
                    deletDoc(docKey);
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
                HashMap<String,Object>map=new HashMap<>();
                map.put("status",0);
                doc.child(key).updateChildren(map);
            }
            else {
                courseListListener.onError("Lỗi khi xóa doc");
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
                HashMap<String,Object>map=new HashMap<>();
                map.put("status",0);
                doc.child(key).updateChildren(map);

            }
            else {
                courseListListener.onError("Lỗi khi xóa request");
            }
        }
        catch (Exception ex){
            Log.e("Error",ex.getMessage());
        }

    }
    public void deleteCourse(String key) {
        try {
            if(key!=null) {
                DatabaseReference course=FirebaseDatabase.getInstance().getReference("Course");
                HashMap<String,Object>map=new HashMap<>();
                map.put("status",0);
                course.child(key).updateChildren(map);
            }
            else {
                courseListListener.onError("Lỗi khi xóa khóa học");
            }
        }
        catch (Exception ex){
            Log.e("Error",ex.getMessage());
        }

    }

}
