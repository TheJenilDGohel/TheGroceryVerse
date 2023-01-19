package com.example.mygrocerystore.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.activities.ViewOrderProduct;
import com.example.mygrocerystore.models.UserOrderModel;

import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.ViewHolder>{
    Context context;
    List<UserOrderModel> list;

    public UserOrderAdapter(Context context, List<UserOrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserOrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_details,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderAdapter.ViewHolder holder, int position) {
        holder.orderId.setText("Order Id : "+list.get(holder.getAdapterPosition()).getOrderId());
        holder.orderDate.setText("Order Date : "+list.get(holder.getAdapterPosition()).getOrderDate());
        holder.totalPrice.setText("Total Bill : "+"0");
        holder.userAddress.setText("Address : "+list.get(holder.getAdapterPosition()).getUser_address());
        holder.userMobileno.setText("Contact No. : "+list.get(holder.getAdapterPosition()).getUser_phone());
        holder.orderStatus.setText("Order Status : "+list.get(holder.getAdapterPosition()).getStatus());
        String ORDERID=list.get(holder.getAdapterPosition()).getOrderId();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ViewOrderProduct.class);
                intent.putExtra("orderId",ORDERID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId,orderDate,totalPrice,userAddress,userMobileno,orderStatus;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId=itemView.findViewById(R.id.order_id);
            orderDate=itemView.findViewById(R.id.order_date);
            totalPrice=itemView.findViewById(R.id.producttotalprice);
            userAddress=itemView.findViewById(R.id.user_address);
            userMobileno=itemView.findViewById(R.id.user_phone);
            orderStatus=itemView.findViewById(R.id.order_product_status);
            cardView=itemView.findViewById(R.id.card);
        }
    }
}
