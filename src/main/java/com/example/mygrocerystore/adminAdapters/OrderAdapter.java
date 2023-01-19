package com.example.mygrocerystore.adminAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.admin.ShowOrderProduct;
import com.example.mygrocerystore.adminModels.OrderModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context context;
    List<OrderModel> orderModelList;

    public OrderAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        holder.orderId.setText("Order Id : "+orderModelList.get(position).getOrderId());
        holder.Date.setText("Order Date : "+orderModelList.get(position).getCurrent_date());
        holder.Name.setText("User Name : "+orderModelList.get(position).getUser_name());
        holder.Address.setText("Order Id : "+orderModelList.get(position).getUser_address());
        holder.Phone.setText("Order Id : "+orderModelList.get(position).getUser_phone());
        holder.Status.setText("Order Status : "+orderModelList.get(position).getStatus());

        final List<String>[] orderStatus = new List[]{Arrays.asList("Select Status","Pending","Accepted" ,"Shipped","Delivered")};

        ArrayAdapter adapter = new ArrayAdapter(context.getApplicationContext(), android.R.layout.simple_spinner_item, orderStatus[0]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.orderStatus.setAdapter(adapter);
        
        holder.statusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String productStatus = holder.orderStatus.getSelectedItem().toString();
                final String documentId = orderModelList.get(holder.getAdapterPosition()).getOrderId();
                final HashMap<String,Object> cartMap = new HashMap<>();

                cartMap.put("status", productStatus);

                FirebaseFirestore.getInstance().collection("CurrentUser").document("Tagr8yXf26esXqfPpKtbbXNjZXj1").collection("Order")
                        .document(documentId).update(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Product Status :"+productStatus)
                                        .show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Category Not Updated.")
                                        .show();
                            }
                        });
            }
        });

        holder.showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ShowOrderProduct.class);
                intent.putExtra("orderId",orderModelList.get(holder.getAdapterPosition()).getOrderId());
                Log.d("TAG", "onClick: "+orderModelList.get(holder.getAdapterPosition()).getOrderId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, Date, Name, Phone, Address, TotalPrice, Status;
        Spinner orderStatus;
        ImageButton statusChange;
        TextView showAll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            Date=itemView.findViewById(R.id.currentDate);
            Name=itemView.findViewById(R.id.userName);
            Phone=itemView.findViewById(R.id.userPhone);
            Address=itemView.findViewById(R.id.userAddress);
            Status=itemView.findViewById(R.id.status);

            orderStatus = itemView.findViewById(R.id.order_status_spinner);
            statusChange = itemView.findViewById(R.id.order_submit);

            showAll=itemView.findViewById(R.id.showAllProducts);
        }
    }
}
