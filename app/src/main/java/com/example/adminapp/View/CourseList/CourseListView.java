package com.example.adminapp.View.CourseList;

import com.example.adminapp.ViewHolder.CourseViewHolder;

public interface CourseListView {
    void onClickItem(CourseViewHolder courseViewHolder, String key,String docName);
    void onError(String msg);


}
