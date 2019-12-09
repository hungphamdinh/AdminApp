package com.example.adminapp.Presenter.InsertCourse;

public interface InsertCourseListener {
    void onSuccess(String msg);
    void onFailed(String msg);
    void onSuccessUploadStorage(String courseId);
    void onLoadDing(int percent);
}
