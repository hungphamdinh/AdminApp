package com.example.adminapp.View.DetailUpdateCourse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Presenter.DetailUpdateCourse.UpdateDetailCoursePresenter;
import com.example.adminapp.R;
import com.example.adminapp.View.TestActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailUpdateCourseActivity extends AppCompatActivity implements IDetailUpdateView{
    private EditText edtName,edtPrice,edtDiscount,edtSchedule,edtDescript,edtPhone,edtCourseDoc,edtImage;
    private Button btnUpdate,btnUpdateFile,btnChooseImage,btnUpdateTest;
    private TextView txtDateEnd,txtDateBegin;
    private ArrayList<String> courseDetailList;
    private String courseID;
    private Uri pdfUri,imageUri;
    private ProgressDialog progressDialog;
    private UpdateDetailCoursePresenter updateDetailCoursePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);
        edtName=(EditText)findViewById(R.id.edtCourseNameU);
        edtPrice=(EditText)findViewById(R.id.edtCoursePriceU);
        edtImage=(EditText)findViewById(R.id.edtCourseImageUrl);
        edtDiscount=(EditText)findViewById(R.id.edtCourseDiscountU);
        edtSchedule=(EditText)findViewById(R.id.edtCourseScheduleU);
        edtDescript=(EditText)findViewById(R.id.edtCourseDescriptU);
        edtPhone=(EditText)findViewById(R.id.edtCoursePhoneU);
        edtCourseDoc=(EditText)findViewById(R.id.edtCourseDocName);
        txtDateBegin=(TextView)findViewById(R.id.txtDateBeignUp);
        txtDateEnd=(TextView)findViewById(R.id.txtDateEndUp);
        btnUpdateFile=(Button)findViewById(R.id.btnUpdateFile);
        btnChooseImage=(Button)findViewById(R.id.btnChooseImage);
        btnUpdateTest=(Button)findViewById(R.id.btnUpdateTest);
        HashMap<String,Object>edtMap=new HashMap<>();
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        if (getIntent() != null) {
            courseID = getIntent().getStringExtra("DetailList");

        }
        if (!courseID.isEmpty() && courseID != null) {
            if (Common.isConnectedToInternet(this)) {
                updateDetailCoursePresenter=new UpdateDetailCoursePresenter(DetailUpdateCourseActivity.this);
                updateDetailCoursePresenter.LoadDataCourse(courseID);
                updateDetailCoursePresenter.onLoadDoc(courseID);
                setDateBegin(txtDateBegin);
                setDateEnd(txtDateEnd);
                setEdtMap(edtMap);
                onClickChooseImage();
                chooseFilePdf();
                onClickUpdateTest();
            }
        }

    }
    private void setDateBegin(TextView txtDateBegin) {
        txtDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateBegin();
            }
        });
    }
    private void setDateEnd(TextView txtDateEnd) {
        txtDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateEnd();
            }
        });
    }
    private void setEdtMap(final HashMap<String, Object> edtMap) {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=openDialog();
                edtMap.put("name",edtName.getText().toString());
                edtMap.put("price",edtPrice.getText().toString());
                edtMap.put("edtImage",edtImage.getText().toString());
                edtMap.put("discount",edtDiscount.getText().toString());
                edtMap.put("schedule",edtSchedule.getText().toString());
                edtMap.put("descript",edtDescript.getText().toString());
                edtMap.put("courseDoc",edtCourseDoc.getText().toString());
                edtMap.put("phone",edtPhone.getText().toString());
                edtMap.put("imageUri",imageUri);
                edtMap.put("pdfUri",pdfUri);
                edtMap.put("dateBegin",txtDateBegin.getText().toString());
                edtMap.put("dateEnd",txtDateEnd.getText().toString());
                updateDetailCoursePresenter.onClickUpdate(courseID,edtMap);
            }
        });

    }

    private void onClickUpdateTest() {
        btnUpdateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailUpdateCourseActivity.this, TestActivity.class);
                intent.putExtra("courseID",courseID);
                startActivity(intent);
            }
        });
    }
    private void onClickChooseImage() {
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        if(ContextCompat.checkSelfPermission(MyAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                chooseImage();
                //         }
//            else{
//                ActivityCompat.requestPermissions(MyAccountActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
//
//            }

            }
        });
    }


    private ProgressDialog openDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Đang tải...");
        //progressDialog.setProgress(0);
        progressDialog.show();
        return progressDialog;
    }

    private void chooseFilePdf() {
        btnUpdateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(DetailUpdateCourseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                }
                else{
                    ActivityCompat.requestPermissions(DetailUpdateCourseActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

                }
            }
        });
    }

    private void selectPdf() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }
    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
//        {
            selectPdf();
            chooseImage();
        //}
       // else
       //     Toast.makeText(DetailUpdateCourseActivity.this,"Provide pemrission",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==86&&resultCode==RESULT_OK&&data!=null){
            pdfUri=data.getData();
            //edtCourseDoc.setText(data.getData().getLastPathSegment());
        }
        if(requestCode==Common.PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null){
            imageUri=data.getData();
            //edtImage.setText(data.getData().getLastPathSegment());
        }
//        else{
//            Toast.makeText(DetailUpdateCourseActivity.this,"Chọn file mà bạn muốn",Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onSuccess(String msg) {
        progressDialog.cancel();
        Toast.makeText(DetailUpdateCourseActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(String msg) {
        progressDialog.cancel();
        Toast.makeText(DetailUpdateCourseActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading(int percent) {
        progressDialog.setProgress(percent);
    }

    @Override
    public void onLoadCourseDoc(HashMap<String, Object> data) {
        edtCourseDoc.setText(data.get("name").toString());
        //edtImage.setText(data.get("url").toString());
    }

    @Override
    public void onLoadCourse(HashMap<String, Object> data) {
        edtName.setText(data.get("name").toString());
        edtPrice.setText(data.get("price").toString());
        edtDescript.setText(data.get("descript").toString());
        edtDiscount.setText(data.get("discount").toString());
        edtSchedule.setText(data.get("schedule").toString());
        edtPhone.setText(data.get("phone").toString());
        edtImage.setText(data.get("image").toString());
        txtDateBegin.setText(data.get("begin").toString());
        txtDateEnd.setText(data.get("end").toString());
    }

    @Override
    public void onNullItem(String msg) {
        Toast.makeText(DetailUpdateCourseActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    private void showDateEnd() {
        LayoutInflater inflater=LayoutInflater.from(this);
        View subView=inflater.inflate(R.layout.alert_calendar_view,null);
        final CalendarView calendarView=(CalendarView)subView.findViewById(R.id.calendarView);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Lịch");
        alertDialog.setMessage("Chọn ngày bạn muốn");
        //alertDialog.create();
        alertDialog.setView(subView);
        //alertDialog.show();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                String curDate =String.valueOf(d);
                int y = year;
                String curYear =String.valueOf(y);
                int m = month;
                String curMonth =String.valueOf(m);
                txtDateEnd.setText(curDate+"/"+curMonth+"/"+curYear);
            }
        });
        alertDialog.setNegativeButton("Chọn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    private void showDateBegin() {
        LayoutInflater inflater=LayoutInflater.from(this);
        View subView=inflater.inflate(R.layout.alert_calendar_view,null);
        final CalendarView calendarView=(CalendarView)subView.findViewById(R.id.calendarView);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Lịch");
        alertDialog.setMessage("Chọn ngày bạn muốn");
        //alertDialog.create();
        alertDialog.setView(subView);
        //alertDialog.show();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                String curDate =String.valueOf(d);
                int y = year;
                String curYear =String.valueOf(y);
                int m = month;
                String curMonth =String.valueOf(m);
                txtDateBegin.setText(curDate+"/"+curMonth+"/"+curYear);
            }
        });
        alertDialog.setNegativeButton("Chọn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
