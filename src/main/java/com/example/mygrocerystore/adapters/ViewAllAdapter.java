package com.example.mygrocerystore.adapters;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mygrocerystore.models.ViewAllModel;
import com.example.mygrocerystore.activities.*;
import com.example.mygrocerystore.R;

import com.bumptech.glide.request.transition.Transition;

import java.util.List;
import java.util.Random;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {

    Context context;
    List<ViewAllModel> list;
    private int lastPosition = -1;

    public ViewAllAdapter(Context context, List<ViewAllModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setFilteredList(List<ViewAllModel> filteredList){
        this.list=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      float ratingCount = Float.valueOf(list.get(holder.getAdapterPosition()).getRating());

        Glide.with(context).load(list.get(holder.getAdapterPosition()).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(holder.getAdapterPosition()).getName());
        holder.description.setText(list.get(holder.getAdapterPosition()).getDescription());
        holder.price.setText("Price : "+list.get(holder.getAdapterPosition()).getPrice()+"/KG");
        holder.rating.setRating(ratingCount);
        holder.rating.setStepSize(ratingCount);

        if(list.get(holder.getAdapterPosition()).getType().equals("eggs"))
        {
            holder.price.setText(list.get(holder.getAdapterPosition()).getPrice()+"/Dozen");
        }
        if(list.get(holder.getAdapterPosition()).getType().equals("milk"))
        {
            holder.price.setText(list.get(holder.getAdapterPosition()).getPrice()+"/Liter");
        }
        if(list.get(holder.getAdapterPosition()).getType().equals("drink"))
        {
            holder.price.setText(list.get(holder.getAdapterPosition()).getPrice()+"/Liter");
        }

        setAnimation(holder.itemView,holder.getAdapterPosition());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,DetailedActivity.class);
                intent.putExtra("detail",list.get(holder.getAdapterPosition()));
                try{
                    context.startActivity(intent);
                }
                catch (Exception e)
                {
                    Log.d("TAG", "onClick: "+ e);
                }
            }
        });
    }

    //RecycleView animation
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(900));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,description,price;
        RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.view_img);
            name=itemView.findViewById(R.id.view_name);
            description=itemView.findViewById(R.id.view_description);
            price=itemView.findViewById(R.id.view_price);
            rating=itemView.findViewById(R.id.view_rating);
        }
    }
}
