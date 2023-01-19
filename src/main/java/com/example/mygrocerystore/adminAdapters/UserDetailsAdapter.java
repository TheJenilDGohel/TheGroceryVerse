package com.example.mygrocerystore.adminAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adminModels.UserDetailsModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.ViewHolder> {
    Context context;
    List<UserDetailsModel> list;

    public UserDetailsAdapter(Context context, List<UserDetailsModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setFilteredList(List<UserDetailsModel> filteredlist) {
        this.list=filteredlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserDetailsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_details_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).getProfileImg() == null) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.profile));
        } else {
            Glide.with(context).load(list.get(position).getProfileImg()).into(holder.imageView);
        }
        holder.name.setText(list.get(position).getName());
        holder.email.setText(list.get(position).getEmail());
        holder.mobileno.setText(list.get(position).getMobileNumber());
        holder.address.setText(list.get(position).getAddress());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = list.get(holder.getAdapterPosition()).getMobileNumber();
                if (number == "null") {
                    Toast.makeText(context, "Number Not Available.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + number));
                    context.startActivity(intent);
                }
            }
        });

        holder.Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {list.get(holder.getAdapterPosition()).getEmail()};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject text here...");
                intent.putExtra(Intent.EXTRA_TEXT, "Body of the content here...");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                context.startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name, email, mobileno, address;
        TextView call, Email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_img);
            name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);
            mobileno = itemView.findViewById(R.id.user_mobileno);
            address = itemView.findViewById(R.id.user_address);

            call = itemView.findViewById(R.id.call);
            Email = itemView.findViewById(R.id.email);
        }
    }
}
