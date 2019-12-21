package com.example.adminapp.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.R;


public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView txtName,txtPrice,txtDescript, txtIsBuy,txtDoc;
    public ImageView btnDelete,imgCourse,btnCheck;
    private ItemClickListener itemClickListener;
    public CourseViewHolder(View itemView) {
        super(itemView);
        txtName=(TextView)itemView.findViewById(R.id.txtCourseName);
        txtPrice=(TextView)itemView.findViewById(R.id.txtCoursePrice);
        txtDescript=(TextView)itemView.findViewById(R.id.txtCourseDescript);
        txtIsBuy =(TextView)itemView.findViewById(R.id.txtIsBuy);
        btnDelete=(ImageView)itemView.findViewById(R.id.btnDeleteCourse);
        imgCourse=(ImageView)itemView.findViewById(R.id.imgCourse);
        btnCheck=(ImageView)itemView.findViewById(R.id.btnCheckCourse);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select this action");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);

    }
}
