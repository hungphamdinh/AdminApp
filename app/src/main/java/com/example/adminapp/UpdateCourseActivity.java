package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Model.Course;
import com.example.adminapp.Model.Doc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateCourseActivity extends AppCompatActivity {
    private EditText edtName,edtPrice,edtDiscount,edtSchedule,edtDescript,edtPhone,edtCourseDoc;
    private Button btnUpdate,btnUpdateFile;
    private DatabaseReference courseRef;
    private ArrayList<String> courseDetailList;
    private String courseID;
    private Uri pdfUri;
    private ProgressDialog progressDialog;
    private String docID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);
        edtName=(EditText)findViewById(R.id.edtCourseNameU);
        edtPrice=(EditText)findViewById(R.id.edtCoursePriceU);
        edtDiscount=(EditText)findViewById(R.id.edtCourseDiscountU);
        edtSchedule=(EditText)findViewById(R.id.edtCourseScheduleU);
        edtDescript=(EditText)findViewById(R.id.edtCourseDescriptU);
        edtPhone=(EditText)findViewById(R.id.edtCoursePhoneU);
        edtCourseDoc=(EditText)findViewById(R.id.edtCourseDocName);
        btnUpdateFile=(Button)findViewById(R.id.btnUpdateFile);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        if (getIntent() != null) {
            courseDetailList = getIntent().getStringArrayListExtra("DetailList");

        }
        if (!courseDetailList.isEmpty() && courseDetailList != null) {
            if (Common.isConnectedToInternet(this)) {
                courseID=courseDetailList.get(0);
                docID=courseDetailList.get(1);
                loadDetaillCourse(courseID);
                loadDetailDoc(courseID);
                chooseFilePdf();
            }
        }

    }

    private void loadDetailDoc(String courseID) {
        DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
        docRef.orderByChild("courseId").equalTo(courseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Doc doc=childSnap.getValue(Doc.class);
                    edtCourseDoc.setText(doc.getDocName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadDetaillCourse(final String courseId) {
        courseRef= FirebaseDatabase.getInstance().getReference("Course");
        courseRef.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course = dataSnapshot.getValue(Course.class);
                //Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodImage);
                edtName.setText(course.getCourseName());
                edtPrice.setText(course.getPrice());
                edtDescript.setText(course.getDescript());
                edtDiscount.setText(course.getDiscount());
                edtSchedule.setText(course.getSchedule());
                edtPhone.setText(course.getTutorPhone());
                String tutorPhone = course.getTutorPhone();
//                loadDetailTutor(tutorPhone);
                onClickUpdate(courseId);

            }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            }

    private void onClickUpdate(final String courseId) {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameTemp=edtName.getText().toString();
                final String priceTemp=edtPrice.getText().toString();
                final String discountTemp=edtDiscount.getText().toString();
                final String scheduleTemp=edtSchedule.getText().toString();
                final String descriptTemp=edtDescript.getText().toString();
                final String phoneTemp=edtPhone.getText().toString();
                final String courseDoc=edtCourseDoc.getText().toString();
                final DatabaseReference tutorRef=FirebaseDatabase.getInstance().getReference("Tutor");
                tutorRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(nameTemp.isEmpty()||priceTemp.isEmpty()||discountTemp.isEmpty()||scheduleTemp.isEmpty()
                                ||descriptTemp.isEmpty()||phoneTemp.isEmpty()||courseDoc.isEmpty()){
                            Toast.makeText(UpdateCourseActivity.this,"Kiểm tra lại thông tin",Toast.LENGTH_SHORT).show();
                        }
                        else if(!dataSnapshot.child(phoneTemp).exists()){
                            Toast.makeText(UpdateCourseActivity.this,"Số điện thoại này không tồn tại",Toast.LENGTH_SHORT).show();
                        }else {
                            HashMap<String, Object> orderMap = new HashMap<>();
                            orderMap.put("courseName", nameTemp);
                            orderMap.put("descript", descriptTemp);
                            orderMap.put("discount", discountTemp);
                            orderMap.put("price", priceTemp);
                            orderMap.put("schedule", scheduleTemp);
                            orderMap.put("tutorPhone", phoneTemp);
                            courseRef.child(courseId).updateChildren(orderMap);
                            uploadDoc();
                            Toast.makeText(UpdateCourseActivity.this, "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
    private void uploadDoc() {
        if(pdfUri!=null){
            upLoadToStorage(pdfUri,docID);
            Toast.makeText(UpdateCourseActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(UpdateCourseActivity.this,"Chọn file",Toast.LENGTH_SHORT).show();
    }
    private void upLoadToStorage(Uri pdfUri, final String key) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Đang tải...");
        progressDialog.setProgress(0);
        progressDialog.show();
        final String fileName=System.currentTimeMillis()+"";
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        storageReference.child("/CourseFile/").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        HashMap<String, Object> map = new HashMap<>();
                        String url=taskSnapshot.getDownloadUrl().toString();
                       // map.put("courseId", key);
                        map.put("docName", edtCourseDoc.getText().toString());
                        map.put("docUrl", url);
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Doc");
                        reference.child(key).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateCourseActivity.this,"Tải file lên thành công",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateCourseActivity.this,"Tải file lên thất bại",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateCourseActivity.this,"Tải file lên thất bại",Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress=(int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }
    private void chooseFilePdf() {
        btnUpdateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(UpdateCourseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                }
                else{
                    ActivityCompat.requestPermissions(UpdateCourseActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else
            Toast.makeText(UpdateCourseActivity.this,"Provide pemrission",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==86&&resultCode==RESULT_OK&&data!=null){
            pdfUri=data.getData();
         //   edtCourseDoc.setText(data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(UpdateCourseActivity.this,"Chọn file mà bạn muốn",Toast.LENGTH_SHORT).show();
        }
    }
}
