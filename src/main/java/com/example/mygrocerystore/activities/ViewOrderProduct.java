package com.example.mygrocerystore.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adapters.UserOrderProductListAdapter;
import com.example.mygrocerystore.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewOrderProduct extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    UserOrderProductListAdapter userOrderProductListAdapter;
    List<MyCartModel> list;
    Toolbar toolbar;
    ImageView imageView;
    Window window;

    private ImageView progressBar, progressBar1;
    private LottieAnimationView progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_product);

        imageView = findViewById(R.id.show_img);
        imageView.setVisibility(View.GONE);
        window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ViewOrderProduct.this, R.color.dark_green));

        //ProgresBar ID Fetch
        progressBar = findViewById(R.id.progress_bar);
        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);

        //Gone ProgresBar
        progressBar.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firestore = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        String orderId = getIntent().getStringExtra("orderId");
        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        list = new ArrayList<>();
        userOrderProductListAdapter = new UserOrderProductListAdapter(this, list);
        recyclerView.setAdapter(userOrderProductListAdapter);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Order").document(orderId).collection("productList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            MyCartModel myCartModel = documentSnapshot.toObject(MyCartModel.class);
                            list.add(myCartModel);
                            userOrderProductListAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            progressBar1.setVisibility(View.GONE);
                            progressBar2.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}