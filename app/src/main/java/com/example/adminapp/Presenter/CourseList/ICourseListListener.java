package com.example.adminapp.Presenter.CourseList;

import com.example.adminapp.ViewHolder.CourseViewHolder;

public interface ICourseListListener {
    void onClickItem(String key,String docName);
    void onError(String msg);
}
