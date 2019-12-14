package com.example.adminapp.Presenter.DetailUpdateCourse;

import com.example.adminapp.View.DetailUpdateCourse.IDetailUpdateView;

import java.util.HashMap;

public class UpdateDetailCoursePresenter implements UpdateDetailListener {
    private UpdateDetailCourse mainInterator;
    private IDetailUpdateView updateView;
    public UpdateDetailCoursePresenter(IDetailUpdateView updateView){
        this.updateView=updateView;
        this.mainInterator =new UpdateDetailCourse(this);
    }
    public void onLoadDoc(String courseId){
        mainInterator.loadDetailDoc(courseId);
    }
    public void onClickUpdate(String courseId,HashMap<String,Object>map){
        mainInterator.onCheckImage(courseId,map);

    }
    public void LoadDataCourse(String courseId){
        HashMap<String,Object>loadDataMap=new HashMap<>();
        mainInterator.loadDetaillCourse(courseId,loadDataMap);
    }
    @Override
    public void onSuccess(String msg) {
        updateView.onSuccess(msg);
    }

    @Override
    public void onFailed(String msg) {
        updateView.onFailed(msg);
    }

    @Override
    public void onLoading(int percent) {
        updateView.onLoading(percent);
    }

    @Override
    public void onLoadData(HashMap<String, Object> data) {
        updateView.onLoadCourse(data);
    }

    @Override
    public void onLoadCourseDoc(HashMap<String, Object> data) {
        updateView.onLoadCourseDoc(data);
    }

    @Override
    public void onNullItem(String msg) {
        updateView.onNullItem(msg);
    }


}
