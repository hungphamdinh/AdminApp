package com.example.adminapp.Presenter.CourseList;

import android.view.MenuItem;

import com.example.adminapp.View.CourseList.CourseListView;
import com.example.adminapp.ViewHolder.CourseViewHolder;

public class CourseListPresenter implements ICourseListListener{
    private CourseList mainInterator;
    private CourseListView courseListView;
    private CourseViewHolder courseViewHolder;
    public CourseListPresenter(CourseListView courseListView,CourseViewHolder courseViewHolder){
        this.courseListView=courseListView;
        this.courseViewHolder=courseViewHolder;
        mainInterator=new CourseList(this);
    }
    public void onLoadCourse(String key){
        mainInterator.loadCourseDoc(key);
    }
    public void onDelete(String key){
        mainInterator.onRequest(key);
        mainInterator.onDoc(key);
        mainInterator.deleteCourse(key);
    }
    public void onDeleteCourse(String key){
    }
    @Override
    public void onClickItem(String key, String docName) {
        courseListView.onClickItem(courseViewHolder,key,docName);
    }

    @Override
    public void onError(String msg) {
        courseListView.onError(msg);
    }
}
