package com.example.adminapp.View.InsertCourse;

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
import com.example.adminapp.Presenter.InsertCourse.InsertCoursePresenter;
import com.example.adminapp.R;
import com.example.adminapp.View.TestActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class InsertCourseActivity extends AppCompatActivity implements IInsertCourseView{
    private EditText edtName,edtPrice,edtDiscount,edtSchedule,edtDescript,edtPhone,edtCourse;
    private TextView txtDateEnd,txtDateBegin;
    private Button btnInsert,btnChooseFile,btnUploadFile,btnImage;
    private DatabaseReference courseRef;
    private Uri pdfUri,imageUri;
    private ProgressDialog progressDialog;
    private InsertCoursePresenter insertCoursePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_course);
        edtName=(EditText)findViewById(R.id.edtCourseName);
        edtPrice=(EditText)findViewById(R.id.edtCoursePrice);
        edtDiscount=(EditText)findViewById(R.id.edtCourseDiscount);
        edtSchedule=(EditText)findViewById(R.id.edtCourseSchedule);
        edtDescript=(EditText)findViewById(R.id.edtCourseDescript);
        edtPhone=(EditText)findViewById(R.id.edtCoursePhone);
        btnInsert=(Button)findViewById(R.id.btnInsert);
        btnImage=(Button)findViewById(R.id.btnChooseImageInsert);
        edtCourse=(EditText)findViewById(R.id.edtCourseDoc);
        txtDateEnd=(TextView) findViewById(R.id.txtDateEnd);
        txtDateBegin=(TextView) findViewById(R.id.txtDateBeign);
        btnChooseFile=(Button)findViewById(R.id.btnChooseFile);
        insertCoursePresenter=new InsertCoursePresenter(this);
        btnInsert.setEnabled(false);
        btnImage.setEnabled(false);
        onClickChooseImage();
        chooseFilePdf();
        setDateEnd(txtDateEnd);
        setDateBegin(txtDateBegin);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object>edtMap=new HashMap<>();
                edtMap.put("name",edtName.getText().toString());
                edtMap.put("price",edtPrice.getText().toString());
                edtMap.put("descript",edtDescript.getText().toString());
                edtMap.put("schedule",edtSchedule.getText().toString());
                edtMap.put("discount",edtDiscount.getText().toString());
                edtMap.put("phone",edtPhone.getText().toString());
                edtMap.put("imageUri",imageUri);
                edtMap.put("dateBegin",txtDateBegin.getText().toString());
                edtMap.put("dateEnd",txtDateEnd.getText().toString());
                edtMap.put("pdf",pdfUri);
                progressDialog=getDialog();
                insertCoursePresenter.loadCourse(edtMap,pdfUri);
            }
        });
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

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);
    }

    private void chooseFilePdf() {
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(InsertCourseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                }
                else{
                    ActivityCompat.requestPermissions(InsertCourseActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

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
    private ProgressDialog getDialog(){
        progressDialog=new ProgressDialog(InsertCourseActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Đang tải...");
        progressDialog.setProgress(0);
        progressDialog.show();
        return progressDialog;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
            //chooseImage();
        }
        else
            Toast.makeText(InsertCourseActivity.this,"Provide pemrission",Toast.LENGTH_SHORT).show();
    }
    private void onClickChooseImage() {
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==86&&resultCode==RESULT_OK&&data!=null){
            pdfUri=data.getData();
            //edtCourse.setText(data.getData().getLastPathSegment());
            btnImage.setEnabled(true);
        }
        else if(requestCode==Common.PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null){
            imageUri=data.getData();
            //edtImage.setText(data.getData().getLastPathSegment());
            btnInsert.setEnabled(true);
        }
        else{
            btnInsert.setEnabled(false);
            Toast.makeText(InsertCourseActivity.this,"Chọn file mà bạn muốn",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSuccess(String msg) {
        progressDialog.cancel();
        Toast.makeText(InsertCourseActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(String msg) {
        progressDialog.cancel();
        Toast.makeText(InsertCourseActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessUploadStorage(String courseId) {
//        Intent intent=new Intent(InsertCourseActivity.this,TestActivity.class);
//        intent.putExtra("courseID",courseId);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onLoadDing(int percent) {
        progressDialog.setProgress(percent);
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
