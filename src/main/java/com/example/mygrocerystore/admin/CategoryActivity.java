package com.example.mygrocerystore.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adminAdapters.CategoryAdapter;
import com.example.mygrocerystore.models.NavCategoryModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    FirebaseFirestore db;
    ImageView back;
    FloatingActionButton add;

    List<NavCategoryModel> navCategoryModelList;
    CategoryAdapter categoryAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincategory);

        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            startActivity(new Intent(CategoryActivity.this, AdminActivity.class));
            finish();
        });

        add=findViewById(R.id.add_btn);
        add.setOnClickListener(view -> startActivity(new Intent(CategoryActivity.this,AddCategoryActivity.class)));

        refreshLayout= findViewById(R.id.refresh);
        recyclerView= findViewById(R.id.product_category);
        recyclerView.setNestedScrollingEnabled(false);
        db=FirebaseFirestore.getInstance();


        //All Product Show.....
        recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this,RecyclerView.VERTICAL,false));
        navCategoryModelList= new ArrayList<>();
        categoryAdapter=new CategoryAdapter(CategoryActivity.this,navCategoryModelList);
        recyclerView.setAdapter(categoryAdapter);

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            RearrangeItems();
        });

        db.collection("NavCategory")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        for (DocumentSnapshot document : task.getResult().getDocuments())
                        {
                            String document_id=document.getId();
                            NavCategoryModel navCategoryModel=document.toObject(NavCategoryModel.class);
                            assert navCategoryModel != null;
                            navCategoryModel.setDocumentId(document_id);
                            navCategoryModelList.add(navCategoryModel);
                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        Toast.makeText(CategoryActivity.this, "Error "+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void RearrangeItems() {
        Collections.shuffle(navCategoryModelList, new Random(System.currentTimeMillis()));
        CategoryAdapter categoryAdapter1=new CategoryAdapter(CategoryActivity.this,navCategoryModelList);
        recyclerView.setAdapter(categoryAdapter1);
    }
}