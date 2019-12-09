package com.example.adminapp.Presenter.InsertCourse;

import android.net.Uri;

import androidx.annotation.NonNull;

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

public class InsertCourse {
    private InsertCourseListener insertCourseListener;
    public InsertCourse(InsertCourseListener insertCourseListener){
        this.insertCourseListener=insertCourseListener;
    }
    public void insertCourse(HashMap<String,Object>edtMap, final Uri pdfUri){
        final DatabaseReference courseRef= FirebaseDatabase.getInstance().getReference("Course");
                final String nameTemp=edtMap.get("name").toString();
                final String priceTemp=edtMap.get("price").toString();
                final String discountTemp=edtMap.get("discount").toString();
                final String scheduleTemp=edtMap.get("schedule").toString();
                final String descriptTemp=edtMap.get("descript").toString();
                final String phoneTemp=edtMap.get("phone").toString();
//                final String docTemp=edtCourse.getText().toString();
                DatabaseReference tutorRef=FirebaseDatabase.getInstance().getReference("Tutor");
                tutorRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(nameTemp.equals("")||priceTemp.equals("")||discountTemp.equals("")||scheduleTemp.equals("")
                                ||descriptTemp.equals("")||phoneTemp.equals("")){
                            insertCourseListener.onFailed("Kiểm tra lại thông tin");
                        }
                        else if(!dataSnapshot.child(phoneTemp).exists()){
                            insertCourseListener.onFailed("Số điện thoại không tồn tại");
                        }
                        else{
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
                            //   edtName.setText(dataSnapshot.child(phoneTemp).toString());
                            insertCourseListener.onSuccess("Thêm thành công");
                            uploadDoc(pdfUri);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
    private void uploadDoc(final Uri pdfUri) {
        if(pdfUri!=null){
            DatabaseReference courseRefAf= FirebaseDatabase.getInstance().getReference("Course");
            courseRefAf.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String key = "";
                    for(DataSnapshot childSnap:dataSnapshot.getChildren()) {
                        key= childSnap.getKey();
                    }
                    //            upLoadTest(key);
                    upLoadToStorage(pdfUri,key);
                    insertCourseListener.onSuccess("Thêm tài liệu thành công");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
            insertCourseListener.onFailed("Chọn file");
    }

    private void upLoadToStorage(Uri pdfUri, final String key) {
        final String fileName=System.currentTimeMillis()+"";
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        storageReference.child("/CourseFile/").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        HashMap<String, String> map = new HashMap<>();
                        String url=taskSnapshot.getDownloadUrl().toString();
                        map.put("courseId", key);
                        map.put("docName", "Tài liệu");
                        map.put("type","doc");
                        map.put("docUrl", url);
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Doc");
                        reference.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    insertCourseListener.onSuccess("Tải file lên thành công");
                                    insertCourseListener.onSuccessUploadStorage(key);
                                }
                                else{
                                    insertCourseListener.onFailed("Tải file lên thất bại");
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    insertCourseListener.onFailed("Tải file lên thất bại");

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress=(int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                insertCourseListener.onLoadDing(currentProgress);
            }
        });
    }

}
