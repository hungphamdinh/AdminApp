package com.example.adminapp.Presenter.DetailUpdateTutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.adminapp.Model.Tutor;
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

public class DetailTutor {
    private DetailUpdateTutorListener detailUpdateTutorListener;
    public DetailTutor(DetailUpdateTutorListener detailUpdateTutorListener){
        this.detailUpdateTutorListener=detailUpdateTutorListener;
    }
    public void updateTutor(final String phoneKey) {
        final HashMap<String,Object>firebaseMap=new HashMap<>();
        DatabaseReference table_user=FirebaseDatabase.getInstance().getReference("Tutor");
        table_user.child(phoneKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                firebaseMap.put("userName", tutor.getUsername());
                firebaseMap.put("exp", tutor.getExperience());
                firebaseMap.put("email", tutor.getEmail());
                firebaseMap.put("password", tutor.getPassword());
                firebaseMap.put("image",tutor.getAvatar());
                detailUpdateTutorListener.onLoadData(firebaseMap);

                //      }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void setData(final HashMap<String, Object> edtMap, final String phoneKey) {
        DatabaseReference table_user=FirebaseDatabase.getInstance().getReference("Tutor");
        table_user.child(phoneKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";
                final String usernameTemp = edtMap.get("name").toString();
                final String passwordTemp = edtMap.get("password").toString();
                final String emailTemp = edtMap.get("email").toString();
                final String expTemp=edtMap.get("exp").toString();
                //    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                if (usernameTemp.equals("") || passwordTemp.equals("") || emailTemp.isEmpty() || expTemp.isEmpty()) {
                    detailUpdateTutorListener.onFailed("Kiểm tra lại thông tin");
                } else {
                    if (emailTemp.trim().matches(emailPattern)) {
                        updateStorage(phoneKey,edtMap);
                        //finish();
                    } else {
                        detailUpdateTutorListener.onFailed("Sai định dạng mail");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateStorage(final String phoneKey, HashMap<String,Object>edtMap) {
        final String usernameTemp = edtMap.get("name").toString();
        final String passwordTemp = edtMap.get("password").toString();
        final String emailTemp = edtMap.get("email").toString();
        final String expTemp = edtMap.get("exp").toString();
        final Uri imageUri = (Uri) edtMap.get("imageUri");
        final String profile=edtMap.get("profile").toString();
        if (imageUri == null) {
            HashMap<String, Object> map = new HashMap<>();
            // map.put("courseId", key);
            map.put("avatar", profile);
            map.put("username", usernameTemp);
            map.put("password", passwordTemp);
            map.put("email", emailTemp);//
            map.put("experience", expTemp);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tutor");
            reference.child(phoneKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        detailUpdateTutorListener.onSuccess("Cập nhật thành công");
                    } else {
                        detailUpdateTutorListener.onFailed("Cập nhật thất bại");
                    }
                }
            });
        }
        else {
            final String fileName = System.currentTimeMillis() + "";
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child("/Image/").child(fileName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    HashMap<String, Object> map = new HashMap<>();
                    String url = taskSnapshot.getDownloadUrl().toString();
                    // map.put("courseId", key);
                    map.put("avatar", url);
                    map.put("username", usernameTemp);
                    map.put("password", passwordTemp);
                    map.put("email", emailTemp);//
                    map.put("experience", expTemp);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tutor");
                    reference.child(phoneKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                detailUpdateTutorListener.onSuccess("Cập nhật thành công");
                            } else {
                                detailUpdateTutorListener.onFailed("Cập nhật thất bại");
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    detailUpdateTutorListener.onFailed("Tải lại file");
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    detailUpdateTutorListener.onLoading(currentProgress);
                }
            });
        }
    }
}
