package com.example.mygrocerystore.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adminAdapters.AllProdAdminAdapter;
import com.example.mygrocerystore.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore db;

    FloatingActionButton add;
    ImageView back;

    SearchView searchView;

    List<ViewAllModel> viewAllModelList;
    AllProdAdminAdapter allProdAdminAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminallproduct);

        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllProductActivity.this, AdminActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        add=findViewById(R.id.add_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllProductActivity.this,AddProductActivity.class));
                finish();
            }
        });


        recyclerView=(RecyclerView) findViewById(R.id.all_product);
        recyclerView.setNestedScrollingEnabled(false);
        db=FirebaseFirestore.getInstance();


        //All Product Show.....
        recyclerView.setLayoutManager(new LinearLayoutManager(AllProductActivity.this,RecyclerView.VERTICAL,false));
        viewAllModelList= new ArrayList<>();
        allProdAdminAdapter=new AllProdAdminAdapter(AllProductActivity.this,viewAllModelList);
        recyclerView.setAdapter(allProdAdminAdapter);

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot document : task.getResult().getDocuments())
                            {
                                String document_id=document.getId();
                                ViewAllModel viewAllModel=document.toObject(ViewAllModel.class);
                                viewAllModel.setDocumentId(document_id);
                                viewAllModelList.add(viewAllModel);
                                allProdAdminAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Toast.makeText(AllProductActivity.this, "Error "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //Search Product
        searchView=(SearchView) findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

    }

    private void filterList(String text) {
        List<ViewAllModel> filteredList=new ArrayList<>();
        for (ViewAllModel viewAllModel:viewAllModelList)
        {
            if(viewAllModel.getName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(viewAllModel);
            }
        }
        if(filteredList.isEmpty())
        {
            Toast.makeText(this, "No Data Found.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            allProdAdminAdapter.setFilteredList(filteredList);
        }
    }
}