package com.example.mygrocerystore.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adminAdapters.OrderAdapter;
import com.example.mygrocerystore.adminModels.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore firestore;

    ImageView back;

    List<OrderModel> orderModelList;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_green));

        recyclerView=(RecyclerView) findViewById(R.id.recycleview);
        firestore=FirebaseFirestore.getInstance();

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderActivity.super.onBackPressed();
            }
        });

        //All Product Show.....
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
        orderModelList = new ArrayList<>();
        orderAdapter = new OrderAdapter(OrderActivity.this,orderModelList);
        recyclerView.setAdapter(orderAdapter);

        //Get Data
        firestore.collection("CurrentUser").document("Tagr8yXf26esXqfPpKtbbXNjZXj1").collection("Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                //Toast.makeText(OrderActivity.this, ""+document.getId(), Toast.LENGTH_SHORT).show();
                                OrderModel orderModel=document.toObject(OrderModel.class);
                                orderModelList.add(orderModel);
                                orderAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}