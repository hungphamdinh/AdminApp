package com.example.adminapp.Presenter.DetailUpdateCourse;

import android.net.Uri;

import androidx.annotation.NonNull;

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

import java.util.HashMap;

public class UpdateDetailCourse {
    private UpdateDetailListener updateDetailListener;
    private String docKeyy;
    public UpdateDetailCourse(UpdateDetailListener updateDetailListener) {
        this.updateDetailListener=updateDetailListener;
    }
    public void loadDetaillCourse(final String courseId, final HashMap<String,Object>map) {
        DatabaseReference courseRef= FirebaseDatabase.getInstance().getReference("Course");
        courseRef.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course = dataSnapshot.getValue(Course.class);
                if(course==null){
                    updateDetailListener.onNullItem("Khóa học rỗng");
                }
                else {
                    //Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodImage);
                    HashMap<String, Object> edtMap = new HashMap<>();
                    edtMap.put("name", course.getCourseName());
                    edtMap.put("price", course.getPrice());
                    edtMap.put("descript", course.getDescript());
                    edtMap.put("discount", course.getDiscount());
                    edtMap.put("schedule", course.getSchedule());
                    edtMap.put("phone", course.getTutorPhone());
                    edtMap.put("image", course.getImage());
                    edtMap.put("begin", course.getBegin());
                    edtMap.put("end", course.getEnd());
                    updateDetailListener.onLoadData(edtMap);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void onCheckImage(final String courseId,HashMap<String,Object>edtMap) {
                final String nameTemp = edtMap.get("name").toString();
                final String priceTemp = edtMap.get("price").toString();
                final String discountTemp = edtMap.get("discount").toString();
                final String scheduleTemp = edtMap.get("schedule").toString();
                final String descriptTemp = edtMap.get("descript").toString();
                final String phoneTemp = edtMap.get("phone").toString();
                final String imageTemp=edtMap.get("edtImage").toString();
                final String dateBeginTemp=edtMap.get("dateBegin").toString();
                final String dateEndTemp=edtMap.get("dateEnd").toString();
                //final String courseDoc=edtMap.get("courseDoc").toString();
                final Uri imageUri= (Uri) edtMap.get("imageUri");
                Uri pdfUri= (Uri) edtMap.get("pdfUri");
                //String docKey=edtMap.get("docKey").toString();
                final HashMap<String, Object> listData = new HashMap<>();
                listData.put("name", nameTemp);
                listData.put("descript", descriptTemp);
                listData.put("discount", discountTemp);
                listData.put("price", priceTemp);
                listData.put("schedule", scheduleTemp);
                listData.put("phone", phoneTemp);
                listData.put("courseId", courseId);
                listData.put("imageUri",imageUri);
                listData.put("pdfUri",pdfUri);
                listData.put("dateBegin",dateBeginTemp);
                listData.put("dateEnd",dateEndTemp);
                final DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
                docRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                            Doc doc=childSnap.getValue(Doc.class);
                            if(doc.getType().equals("doc")) {
                                docKeyy = childSnap.getKey();
                                listData.put("courseDoc", doc.getDocName());
                                listData.put("docKey", docKeyy);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        final DatabaseReference tutorRef=FirebaseDatabase.getInstance().getReference("Tutor");
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (nameTemp.equals("") || priceTemp.equals("") || discountTemp.equals("") || scheduleTemp.equals("")
                        || descriptTemp.equals("") || phoneTemp.equals("")) {
                    updateDetailListener.onFailed("Vui lòng kiểm tra lại thông tin");
                }
                else if (!dataSnapshot.child(phoneTemp).exists()) {
                    updateDetailListener.onFailed("Số điện thoại này không tồn tại");
                }
                else {
                    if (imageUri != null) {
                        onClickUpdate(listData);
                    }
                    else {
                        setDataToFirebase(imageTemp, listData);
                        updateDetailListener.onFailed("Chưa chọn file hình ảnh");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });
    }

            private void onClickUpdate(final HashMap<String, Object> listData) {
                final Uri imageUri = (Uri) listData.get("imageUri");
                final String fileName = System.currentTimeMillis() + "";
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference.child("/Image/").child(fileName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        // map.put("courseId", key);

                        setDataToFirebase(url, listData);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        updateDetailListener.onFailed("Tải file lên thất bại");
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        // int currentProgress=(int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        // progressDialog.setProgress(currentProgress);
                    }
                });
//                            courseRef.child(courseId).updateChildren(orderMap);
            //    updateDetailListener.onSuccess("Thay đổi thành công");
    }

    private void setDataToFirebase(String url,HashMap<String,Object>listData) {
        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("courseName", listData.get("name"));
        orderMap.put("image",url);
        orderMap.put("descript", listData.get("descript"));
        orderMap.put("discount", listData.get("discount"));
        orderMap.put("price", listData.get("price"));
        orderMap.put("schedule", listData.get("schedule"));
        orderMap.put("tutorPhone", listData.get("phone"));
        orderMap.put("begin",listData.get("dateBegin"));
        orderMap.put("end",listData.get("dateEnd"));
        DatabaseReference courseRef= FirebaseDatabase.getInstance().getReference("Course");
        courseRef.child(listData.get("courseId").toString()).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    updateDetailListener.onSuccess("Cập nhật thành công");
                }
                else{
                   updateDetailListener.onFailed("Cập nhật thất bại");
                }
            }
        });
        uploadDoc(listData);

    }
    private void uploadDoc(HashMap<String,Object>listData) {
        final Uri pdfUri= (Uri) listData.get("pdfUri");
        if(pdfUri!=null){
            upLoadToStorage(listData);
           // updateDetailListener.onSuccess("Tải lên thành công");
        }
        else
            updateDetailListener.onFailed("Chưa chọn file");
    }

    private void upLoadToStorage(final HashMap<String,Object>listData) {
        final Uri pdfUri= (Uri) listData.get("pdfUri");
        final String key=listData.get("docKey").toString();
        final String fileName=System.currentTimeMillis()+"";
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        storageReference.child("/CourseFile/").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        HashMap<String, Object> map = new HashMap<>();
                        String url=taskSnapshot.getDownloadUrl().toString();
                        // map.put("courseId", key);
                        map.put("docName", listData.get("courseDoc"));
                        map.put("docUrl", url);
                        map.put("type","doc");
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Doc");
                        reference.child(key).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    updateDetailListener.onSuccess("Tải file lên thành công");
                                }
                                else{
                                    updateDetailListener.onFailed("Tải file lên thất bại");
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    updateDetailListener.onFailed("Tải file lên thất bại");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress=(int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                updateDetailListener.onLoading(currentProgress);
            }
        });
    }
    public void loadDetailDoc(String courseID) {
        DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
        final HashMap<String,Object>listData=new HashMap<>();
        docRef.orderByChild("courseId").equalTo(courseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Doc doc=childSnap.getValue(Doc.class);
                    if(doc.getType().equals("doc")) {
                        listData.put("url", doc.getDocUrl());
                        listData.put("name",doc.getDocName());
                        updateDetailListener.onLoadCourseDoc(listData);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
