package com.example.adminapp.ViewHolder;


import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.R;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtName,txtCourseName,txtDescript,txtEmail,txtSchedule;
    private ItemClickListener itemClickListener;
    public ImageView btnDelete,profile;
    public UserViewHolder(View itemView) {
        super(itemView);
        txtName=(TextView)itemView.findViewById(R.id.txtUsernameUser);
        txtEmail=(TextView)itemView.findViewById(R.id.txtEmailUser);
        btnDelete=(ImageView)itemView.findViewById(R.id.btnDeleteUser);
        profile=(ImageView)itemView.findViewById(R.id.imgProfileUser);
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