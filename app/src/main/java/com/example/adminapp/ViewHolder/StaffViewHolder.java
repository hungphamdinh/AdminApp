package com.example.adminapp.ViewHolder;


import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Interface.ItemClickListener;

import com.example.adminapp.R;

//
//public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder>  {
//    private Context context;
//    private ArrayList<Tutor> tutor;
//
//    public StaffAdapter(Context context, ArrayList<Tutor> tutor) {
//        this.context = context;
//        this.tutor = tutor;
//    }
//
//    public StaffAdapter() {
//
//    }
//
//    @NonNull
//    @Override
//    public StaffAdapter.StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.tutor_layout, parent, false);
//        StaffViewHolder holder = new StaffViewHolder(v);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final StaffViewHolder holder, int position) {
//        holder.txtName.setText(tutor.get(position).getUsername());
//        setOnClick(holder);
//
//    }
//
//    private void setOnClick(@NonNull final StaffViewHolder holder) {
//                DatabaseReference tutorRef=FirebaseDatabase.getInstance().getReference("Tutor");
//                tutorRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot childSnap:dataSnapshot.getChildren()){
//                            Tutor tutorCk=childSnap.getValue(Tutor.class);
//                            String key=childSnap.getKey();
//                            clickItem(key, holder);
//                            holder.txtEmail.setText(key);
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//    }
//
//    private void clickItem(final String key, @NonNull StaffViewHolder holder) {
//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                Intent intent=new Intent(context, DetailUpdateTutorActivity.class);
//                intent.putExtra("phoneKey",key);
//                context.startActivity(intent);
//            }
//        });
//    }
//
////    private void onClickItem(Course course, StaffViewHolder viewHolder) {
////        viewHolder.setItemClickListener(new ItemClickListener() {
////            @Override
////            public void onClick(View view, int position, boolean isLongClick) {
////                Intent intent=new Intent(context, TutorDetailAcitivity.class);
////                String tutorID=course.getTutorPhone();
////                String userID=userId;
////                ArrayList<String> listIntent=new ArrayList<>();
////                listIntent.add(tutorID);
////                listIntent.add(userID);
////                listIntent.add(tutor.get(position).courseId);
////                intent.putStringArrayListExtra("ChatID",listIntent);
////                //intent.putExtra("tutorID",adapter.getRef(position).getKey());
////                context.startActivity(intent);                    }
////        });
////    }
//
//    @Override
//    public int getItemCount() {
//        return tutor.size();
//    }
//

    public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        public TextView txtName,txtCourseName,txtDescript,txtEmail,txtSchedule;
        private ItemClickListener itemClickListener;
        public ImageView btnDelete,profileImage;
        public StaffViewHolder(View itemView) {
            super(itemView);
            txtName=(TextView)itemView.findViewById(R.id.txtUsername);
            txtEmail=(TextView)itemView.findViewById(R.id.txtEmail);
            btnDelete=(ImageView)itemView.findViewById(R.id.btnDeleteTutor);
            profileImage=(ImageView)itemView.findViewById(R.id.imgProfile);
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

//}


