package com.example.mygrocerystore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.models.MyCartModel;

import java.util.List;

public class UserOrderProductListAdapter extends RecyclerView.Adapter<UserOrderProductListAdapter.ViewHolder> {
    Context context;
    List<MyCartModel> list;

    public UserOrderProductListAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserOrderProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserOrderProductListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_product_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderProductListAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getProductImage()).into(holder.imageView);
        holder.productName.setText("Product Name : "+list.get(holder.getAdapterPosition()).getProductName());
        holder.price.setText("Price : "+list.get(holder.getAdapterPosition()).getProductPrice());
        holder.quantity.setText("Quantity : "+list.get(holder.getAdapterPosition()).getTotalQuantity());
        holder.totalPrice.setText("Total Price : "+list.get(holder.getAdapterPosition()).getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView productName,price,quantity,totalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.orderProduct_img);
            productName=itemView.findViewById(R.id.orderProduct_name);
            price=itemView.findViewById(R.id.orderProduct_price);
            quantity=itemView.findViewById(R.id.orderProduct_quantity);
            totalPrice=itemView.findViewById(R.id.orderProduct_totPrice);
        }
    }
}
