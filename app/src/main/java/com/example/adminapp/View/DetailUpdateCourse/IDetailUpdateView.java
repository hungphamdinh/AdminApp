package com.example.adminapp.View.DetailUpdateCourse;

import java.util.HashMap;

public interface IDetailUpdateView {
    void onSuccess(String msg);
    void onFailed(String msg);
    void onLoading(int percent);
    void onLoadCourseDoc(HashMap<String,Object> data);
    void onLoadCourse(HashMap<String,Object>data);
    void onNullItem(String msg);
}
