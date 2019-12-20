package com.example.adminapp.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.Model.Doc;
import com.example.adminapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class TestAdapter extends RecyclerView.Adapter<TestAdapter.DocViewHolder>  {
    private Context context;
    private ArrayList<Doc> doc;
    private ArrayList<String>docKey;
    public TestAdapter(Context context, ArrayList<Doc> doc,ArrayList<String>docKey) {
        this.context = context;
        this.doc = doc;
        this.docKey=docKey;
    }
    public TestAdapter() {
    }
    @NonNull
    @Override
    public TestAdapter.DocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.test_layout, parent, false);
        DocViewHolder holder = new DocViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DocViewHolder holder, final int position) {
            holder.txtTestName.setText(doc.get(position).getDocName());

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    showUpdateDialog(position, docKey.get(position));
                }
            });
            holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDialog(docKey.get(position),holder);
                }
            });

    }

    private void deleteDialog(final String key, final DocViewHolder holder) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Xóa");
        alertDialog.setMessage("Bạn có chắc muốn xóa?");
        //alertDialog.create();
        //alertDialog.show();
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                final DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
                docRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Doc doc=dataSnapshot.getValue(Doc.class);
                        if(doc.getStatus()==0){
                            holder.itemView.setVisibility(View.GONE);
                        }
                        else {
                            HashMap<String,Object>map=new HashMap<>();
                            map.put("status",0);
                            docRef.child(key).updateChildren(map);
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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

    private void showUpdateDialog(final int pos, final String testKey) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View subView=inflater.inflate(R.layout.alert_dialog_update_test,null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Cập nhật");
        alertDialog.setMessage("Xin hãy điền thông tin");
        //alertDialog.create();
        final EditText name=(EditText) subView.findViewById(R.id.edtNameTestUp);
        final EditText url=(EditText) subView.findViewById(R.id.edtUrlTestUp);
        name.setText(doc.get(pos).getDocName());
        url.setText(doc.get(pos).getDocUrl());
        alertDialog.setView(subView);
        //alertDialog.show();
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(name.getText().toString().equals("")||url.getText().toString().equals("")){
                    Toast.makeText(context,"Lỗi",Toast.LENGTH_SHORT).show();
                }
                else {
                    dialogInterface.dismiss();
                    DatabaseReference docRef = FirebaseDatabase.getInstance().getReference("Doc");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("courseId", doc.get(pos).getCourseId());
                    map.put("docName", name.getText().toString());
                    map.put("type", "test");
                    map.put("docUrl", url.getText().toString());
                    docRef.child(testKey).updateChildren(map);
                    Toast.makeText(context,"Cập nhật bài test thành công",Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public int getItemCount() {
        return doc.size();
    }


    public class DocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private TextView txtTestName;
        private ImageView imgBtnDelete;
        private ItemClickListener itemClickListener;

        public DocViewHolder(View itemView) {
            super(itemView);
            txtTestName =(TextView) itemView.findViewById(R.id.txtTest);
            imgBtnDelete=(ImageView)itemView.findViewById(R.id.imgBtnDelete);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select this action");
            contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
            contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);

        }

    }

}


