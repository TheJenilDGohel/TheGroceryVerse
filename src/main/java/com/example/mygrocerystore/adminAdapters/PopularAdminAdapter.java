package com.example.mygrocerystore.adminAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.models.PopularModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PopularAdminAdapter extends RecyclerView.Adapter<PopularAdminAdapter.ViewHolder> {
    Context context;
    List<PopularModel> popularModelList;
    FirebaseFirestore firestore;

    public PopularAdminAdapter(Context context, List<PopularModel> popularModelList) {
        this.context = context;
        this.popularModelList = popularModelList;
        firestore=FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularAdminAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_popular_product_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        Glide.with(context).load(popularModelList.get(position).getImg_url()).into(holder.popimg);
        holder.name.setText(popularModelList.get(position).getName());
        holder.description.setText(popularModelList.get(position).getDescription());
        holder.rating.setText(popularModelList.get(position).getRating());
        holder.discount.setText(popularModelList.get(position).getDiscount());
        holder.type.setText(popularModelList.get(position).getType());

        //DELETE
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You won't be able to recover this file!")
                        .setConfirmText("Delete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                firestore.collection("PopularProducts").document(popularModelList.get(holder.getAdapterPosition()).getDocumentId())
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("Product Deleted.")
                                                            .show();
                                                }
                                                else
                                                {
                                                    new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Product Not Deleted.")
                                                            .show();
                                                }
                                            }
                                        });
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

        //EDIT
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.popimg.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.admin_popular_product_edit_card))
                        .setExpanded(true,1900)
                        .create();
                View myview=dialogPlus.getHolderView();
                EditText img_url=myview.findViewById(R.id.prodimgurl);
                EditText name=myview.findViewById(R.id.prodname);
                EditText type=myview.findViewById(R.id.prodtype);
                EditText discount=myview.findViewById(R.id.proddiscount);
                EditText rating=myview.findViewById(R.id.prodrating);
                EditText description=myview.findViewById(R.id.proddescription);
                Button submit=myview.findViewById(R.id.usubmit);

                name.setText(popularModelList.get(holder.getAdapterPosition()).getName());
                type.setText(popularModelList.get(holder.getAdapterPosition()).getType());
                discount.setText(popularModelList.get(holder.getAdapterPosition()).getDiscount());
                rating.setText(popularModelList.get(holder.getAdapterPosition()).getRating());
                description.setText(popularModelList.get(holder.getAdapterPosition()).getDescription());
                img_url.setText(popularModelList.get(holder.getAdapterPosition()).getImg_url());


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("type",type.getText().toString());
                        map.put("discount",discount.getText().toString());
                        map.put("rating",rating.getText().toString());
                        map.put("description",description.getText().toString());
                        map.put("img_url",img_url.getText().toString());
                        FirebaseFirestore.getInstance().collection("PopularProducts")
                                .document(popularModelList.get(holder.getAdapterPosition()).getDocumentId()).update(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                        new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Category Deleted.")
                                                .show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Category Deleted.")
                                                .show();
                                    }
                                });
                    }
                });

                dialogPlus.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView popimg;
        TextView edit,delete;
        TextView name,description,rating,discount,type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            popimg=itemView.findViewById(R.id.pop_img);
            name=itemView.findViewById(R.id.pop_name);
            description=itemView.findViewById(R.id.pop_desc);
            rating=itemView.findViewById(R.id.pop_rating);
            discount=itemView.findViewById(R.id.pop_disc);
            type=itemView.findViewById(R.id.pop_type);

            delete=itemView.findViewById(R.id.delete);
            edit=itemView.findViewById(R.id.edit);
        }
    }
}
