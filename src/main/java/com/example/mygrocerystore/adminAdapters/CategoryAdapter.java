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
import com.example.mygrocerystore.models.NavCategoryModel;
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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    List<NavCategoryModel> navCategoryModelList;
    FirebaseFirestore firestore;

    public CategoryAdapter(Context context, List<NavCategoryModel> navCategoryModelList) {
        this.context = context;
        this.navCategoryModelList = navCategoryModelList;
        firestore=FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_category_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(navCategoryModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(navCategoryModelList.get(position).getName());
        holder.discount.setText(navCategoryModelList.get(position).getDiscount());
        holder.description.setText(navCategoryModelList.get(position).getDescription());
        holder.type.setText(navCategoryModelList.get(position).getType());

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
                                firestore.collection("NavCategory").document(navCategoryModelList.get(holder.getAdapterPosition()).getDocumentId())
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("Category Deleted.")
                                                            .show();
                                                }
                                                else
                                                {
                                                    new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Category Not Delete.")
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
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.imageView.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.admin_category_edit_card))
                        .setExpanded(true,1900)
                        .create();
                View myview=dialogPlus.getHolderView();
                EditText img_url=myview.findViewById(R.id.catimgurl);
                EditText name=myview.findViewById(R.id.catname);
                EditText type=myview.findViewById(R.id.cattype);
                EditText discount=myview.findViewById(R.id.catdiscount);
                EditText description=myview.findViewById(R.id.catdescription);
                Button submit=myview.findViewById(R.id.usubmit);

                name.setText(navCategoryModelList.get(holder.getAdapterPosition()).getName());
                type.setText(navCategoryModelList.get(holder.getAdapterPosition()).getType());
                discount.setText(navCategoryModelList.get(holder.getAdapterPosition()).getDiscount());
                description.setText(navCategoryModelList.get(holder.getAdapterPosition()).getDescription());
                img_url.setText(navCategoryModelList.get(holder.getAdapterPosition()).getImg_url());

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("type",type.getText().toString());
                        map.put("discount",discount.getText().toString());
                        map.put("description",description.getText().toString());
                        map.put("img_url",img_url.getText().toString());
                        FirebaseFirestore.getInstance().collection("NavCategory")
                                .document(navCategoryModelList.get(holder.getAdapterPosition()).getDocumentId())
                                .update(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                        new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Category Updated.")
                                                .show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Category Not Update.")
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
        return navCategoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView edit,delete;
        TextView name,description,discount,type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.category_img);
            name=itemView.findViewById(R.id.category_name);
            description=itemView.findViewById(R.id.category_desc);
            discount=itemView.findViewById(R.id.category_dis);
            type=itemView.findViewById(R.id.category_type);

            delete=itemView.findViewById(R.id.delete);
            edit=itemView.findViewById(R.id.edit);
        }
    }
}
