package com.example.mygrocerystore.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adminAdapters.PopularAdminAdapter;
import com.example.mygrocerystore.models.PopularModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PopularproductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    ImageView back;
    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpopularproduct);

        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PopularproductActivity.this, AdminActivity.class));
                finish();
            }
        });

        add=findViewById(R.id.add_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PopularproductActivity.this,AddpopularproductActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.popular_products);
        recyclerView.setNestedScrollingEnabled(false);
        db = FirebaseFirestore.getInstance();


        List<PopularModel> popularModelList;
        PopularAdminAdapter popularAdminAdapter;

        //popular Item...
        recyclerView.setLayoutManager(new LinearLayoutManager(PopularproductActivity.this, RecyclerView.VERTICAL, false));
        popularModelList = new ArrayList<>();
        popularAdminAdapter = new PopularAdminAdapter(PopularproductActivity.this, popularModelList);
        recyclerView.setAdapter(popularAdminAdapter);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                String documentId = document.getId();
                                PopularModel popularModel = document.toObject(PopularModel.class);
                                popularModel.setDocumentId(documentId);
                                popularModelList.add(popularModel);
                                popularAdminAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(PopularproductActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}