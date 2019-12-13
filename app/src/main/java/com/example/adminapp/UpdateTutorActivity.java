package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.Model.Tutor;
import com.example.adminapp.View.DetailUpdateTutor.DetailUpdateTutorActivity;
import com.example.adminapp.ViewHolder.StaffViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UpdateTutorActivity extends AppCompatActivity {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference tutorRef;
    private FirebaseDatabase database;
    //private StaffAdapter staffAdapter;
    private FirebaseRecyclerAdapter<Tutor, StaffViewHolder> adapter;
    private ArrayList<Tutor> tutorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tutor);
        database= FirebaseDatabase.getInstance();
        tutorRef =database.getReference("Tutor");
        recyclerMenu=(RecyclerView)findViewById(R.id.listTutor);
        recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        loadTutor();
    }

    private void loadTutor(){
        adapter = new FirebaseRecyclerAdapter<Tutor, StaffViewHolder>
                (Tutor.class, R.layout.tutor_layout,
                        StaffViewHolder.class,
                        tutorRef) {
            @Override
            protected void populateViewHolder(final StaffViewHolder viewHolder, final Tutor model, int position) {
                viewHolder.txtName.setText(model.getUsername());
                viewHolder.txtEmail.setText(model.getEmail());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(UpdateTutorActivity.this, DetailUpdateTutorActivity.class);
                        intent.putExtra("phoneKey",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            //  setOnClickItem(item.getOrder(),docKey);
            // showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else {
            deleteCourse(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);

    }
    private void deleteCourse(String key) {
        if(key!=null){
            deleteDialog(key);
        }
        else{
            Log.e("Error key",key);
        }
    }
    private void deleteDialog(final String key) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Xóa");
        alertDialog.setMessage("Bạn có chắc muốn xóa?");
        //alertDialog.create();
        //alertDialog.show();
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tutorRef.child(key).removeValue();
                Toast.makeText(UpdateTutorActivity.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}