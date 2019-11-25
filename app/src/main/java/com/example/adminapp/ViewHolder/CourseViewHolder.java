package com.example.adminapp.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.R;


public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtName,txtPrice,txtDescript, txtTutorName;
    private ItemClickListener itemClickListener;
    public CourseViewHolder(View itemView) {
        super(itemView);
        txtName=(TextView)itemView.findViewById(R.id.txtCourseName);
        txtPrice=(TextView)itemView.findViewById(R.id.txtCoursePrice);
        txtDescript=(TextView)itemView.findViewById(R.id.txtCourseDescript);
        txtTutorName =(TextView)itemView.findViewById(R.id.txtTutorName);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }


}
