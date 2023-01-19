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
import com.example.mygrocerystore.models.ViewAllModel;
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

public class AllProdAdminAdapter extends RecyclerView.Adapter<AllProdAdminAdapter.ViewHolder> {

    Context context;
    List<ViewAllModel> viewAllModelList;
    FirebaseFirestore firestore;

    public AllProdAdminAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
        firestore=FirebaseFirestore.getInstance();
    }

    public void setFilteredList(List<ViewAllModel> filteredList){
        this.viewAllModelList=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllProdAdminAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_all_product_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(viewAllModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText("Name: "+viewAllModelList.get(position).getName());
        holder.description.setText("Description: "+viewAllModelList.get(position).getDescription());
        holder.rating.setText("Rating: "+viewAllModelList.get(position).getRating());
        holder.type.setText("Type: "+viewAllModelList.get(position).getType());
        holder.price.setText("Price: "+viewAllModelList.get(position).getPrice()+"/KG");

        if(viewAllModelList.get(position).getType().equals("eggs"))
        {
            holder.price.setText("Price :"+viewAllModelList.get(position).getPrice()+"/Dozen");
        }
        if(viewAllModelList.get(position).getType().equals("milk"))
        {
            holder.price.setText("Price :"+viewAllModelList.get(position).getPrice()+"/Liter");
        }
        if(viewAllModelList.get(position).getType().equals("drink"))
        {
            holder.price.setText("Price :"+viewAllModelList.get(position).getPrice()+"/Liter");
        }
        if(viewAllModelList.get(position).getType().equals("personalcare"))
        {
            holder.price.setText("Price :"+viewAllModelList.get(position).getPrice()+"/Item");
        }

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
                                firestore.collection("AllProducts").document(viewAllModelList.get(holder.getAdapterPosition()).getDocumentId())
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
                                                            .setTitleText("Product Not Delete.")
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
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.admin_allproduct_edit_card))
                        .setExpanded(true,1900)
                        .create();
                View myview=dialogPlus.getHolderView();
                EditText img_url=myview.findViewById(R.id.prodimgurl);
                EditText name=myview.findViewById(R.id.prodname);
                EditText type=myview.findViewById(R.id.prodtype);
                EditText rating=myview.findViewById(R.id.prodrating);
                EditText description=myview.findViewById(R.id.proddescription);
                EditText price=myview.findViewById(R.id.prodprice);
                Button submit=myview.findViewById(R.id.usubmit);

                int prc=viewAllModelList.get(holder.getAdapterPosition()).getPrice();

                name.setText(viewAllModelList.get(holder.getAdapterPosition()).getName());
                type.setText(viewAllModelList.get(holder.getAdapterPosition()).getType());
                rating.setText(viewAllModelList.get(holder.getAdapterPosition()).getRating());
                description.setText(viewAllModelList.get(holder.getAdapterPosition()).getDescription());
                //price.setText(prc);
                img_url.setText(viewAllModelList.get(holder.getAdapterPosition()).getImg_url());

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("type",type.getText().toString());
                        map.put("rating",rating.getText().toString());
                        map.put("description",description.getText().toString());
                        map.put("price",Integer.parseInt(price.getText().toString()));
                        map.put("img_url",img_url.getText().toString());
                        FirebaseFirestore.getInstance().collection("AllProducts")
                                .document(viewAllModelList.get(holder.getAdapterPosition()).getDocumentId()).update(map)
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
                                        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Category Not Updated.")
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
        return viewAllModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView delete,edit;
        TextView name,description,price,rating,type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.pro_img);
            name=itemView.findViewById(R.id.pro_name);
            description=itemView.findViewById(R.id.pro_desc);
            price=itemView.findViewById(R.id.pro_price);
            rating=itemView.findViewById(R.id.pro_rating);
            type=itemView.findViewById(R.id.pro_type);

            delete=itemView.findViewById(R.id.delete);
            edit=itemView.findViewById(R.id.edit);
        }
    }
}
