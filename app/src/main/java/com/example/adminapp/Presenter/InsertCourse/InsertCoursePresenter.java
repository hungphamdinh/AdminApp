package com.example.adminapp.Presenter.InsertCourse;

import android.net.Uri;

import com.example.adminapp.View.InsertCourse.IInsertCourseView;

import java.util.HashMap;

public class InsertCoursePresenter implements InsertCourseListener{
    private IInsertCourseView insertCourseView;
    private InsertCourse mainInterator;
    public InsertCoursePresenter(IInsertCourseView insertCourseView){
        this.insertCourseView=insertCourseView;
        mainInterator=new InsertCourse(this);
    }
    public void loadCourse(HashMap<String,Object>editMap, Uri uri){
        mainInterator.onInsert(editMap);
    }
    @Override
    public void onSuccess(String msg) {
        insertCourseView.onSuccess(msg);
    }

    @Override
    public void onFailed(String msg) {
        insertCourseView.onFailed(msg);
    }

    @Override
    public void onSuccessUploadStorage(String courseId) {
        insertCourseView.onSuccessUploadStorage(courseId);
    }

    @Override
    public void onLoadDing(int percent) {
        insertCourseView.onLoadDing(percent);
    }
}
