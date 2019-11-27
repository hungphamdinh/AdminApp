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

import java.util.HashMap;

public class InsertCourseActivity extends AppCompatActivity {
    private EditText edtName,edtPrice,edtDiscount,edtSchedule,edtDescript,edtPhone,edtCourse;
    private Button btnInsert,btnChooseFile,btnUploadFile;
    private DatabaseReference courseRef;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private Uri pdfUri;
    private ProgressDialog progressDialog;
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
        edtCourse=(EditText)findViewById(R.id.edtCourseDoc);
        btnChooseFile=(Button)findViewById(R.id.btnChooseFile);
        chooseFilePdf();
        insertCourse();
    }

    private void upLoadToStorage(Uri pdfUri, final String key) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Đang tải...");
        progressDialog.setProgress(0);
        progressDialog.show();
        final String fileName=System.currentTimeMillis()+"";
        StorageReference storageReference=FirebaseStorage.getInstance().getReference();
        storageReference.child("/CourseFile/").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        HashMap<String, String> map = new HashMap<>();
                        String url=taskSnapshot.getDownloadUrl().toString();
                        map.put("courseId", key);
                        map.put("docName", "Tài liệu");
                        map.put("docUrl", url);
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Doc");
                        reference.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(InsertCourseActivity.this,"Tải file lên thành công",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(InsertCourseActivity.this,"Tải file lên thất bại",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertCourseActivity.this,"Tải file lên thất bại",Toast.LENGTH_SHORT).show();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else
            Toast.makeText(InsertCourseActivity.this,"Provide pemrission",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==86&&resultCode==RESULT_OK&&data!=null){
            pdfUri=data.getData();
            //edtCourse.setText(data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(InsertCourseActivity.this,"Chọn file mà bạn muốn",Toast.LENGTH_SHORT).show();
        }
    }

    private void insertCourse(){
        courseRef= FirebaseDatabase.getInstance().getReference("Course");
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTemp=edtName.getText().toString();
                String priceTemp=edtPrice.getText().toString();
                String discountTemp=edtDiscount.getText().toString();
                String scheduleTemp=edtSchedule.getText().toString();
                String descriptTemp=edtDescript.getText().toString();
                String phoneTemp=edtPhone.getText().toString();
                HashMap<String, String> map = new HashMap<>();
                //User user = new User(usernameTemp, passwordTemp,"");
                map.put("courseName", nameTemp);
                map.put("descript", descriptTemp);
                map.put("discount", discountTemp);
                map.put("schedule", scheduleTemp);
                map.put("price", priceTemp);
                map.put("tutorPhone", phoneTemp);
                map.put("isBuy", "false");
                courseRef.push().setValue(map);
                uploadDoc();
            }
        });
    }

    private void uploadDoc() {
        if(pdfUri!=null){
            DatabaseReference courseRefAf= FirebaseDatabase.getInstance().getReference("Course");
            courseRefAf.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String key = "";
                    for(DataSnapshot childSnap:dataSnapshot.getChildren()) {
                        key= childSnap.getKey();
                    }
                    upLoadToStorage(pdfUri,key);
                    Toast.makeText(InsertCourseActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
            Toast.makeText(InsertCourseActivity.this,"Chọn file",Toast.LENGTH_SHORT).show();
    }
}
