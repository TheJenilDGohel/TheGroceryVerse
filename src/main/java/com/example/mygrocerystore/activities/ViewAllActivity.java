package com.example.mygrocerystore.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adapters.ViewAllAdapter;
import com.example.mygrocerystore.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    ViewAllAdapter viewAllAdapter;
    List<ViewAllModel> viewAllModelList;
    Toolbar toolbar;
    ImageView imageView;
    Window window;

    //NEW
    ArrayList<String> category;
    ArrayList<String> categoryDb;

    private ImageView progressBar, progressBar1;
    private LottieAnimationView progressBar2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        imageView = findViewById(R.id.show_img);
        imageView.setVisibility(View.GONE);
        window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ViewAllActivity.this, R.color.dark_green));

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
        String type = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(this, viewAllModelList);
        recyclerView.setAdapter(viewAllAdapter);


        //Getting Fruit
        if (type != null && type.equalsIgnoreCase("fruit")) {
            firestore.collection("AllProducts").whereEqualTo("type", "fruit").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        //Getting Vegetable
        else if (type != null && type.equalsIgnoreCase("vegetable")) {
            firestore.collection("AllProducts").whereEqualTo("type", "vegetable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        //Getting Dairy
        else if (type != null && type.equalsIgnoreCase("dairy")) {
            firestore.collection("AllProducts").whereEqualTo("type", "dairy").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        //Getting Snack
        else if (type != null && type.equalsIgnoreCase("snack")) {
            firestore.collection("AllProducts").whereEqualTo("type", "snack").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        //Getting Backery
        else if (type != null && type.equalsIgnoreCase("backery")) {
            firestore.collection("AllProducts").whereEqualTo("type", "backery").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        //Getting Tea & Coffee
        else if (type != null && type.equalsIgnoreCase("tea&coffee")) {
            firestore.collection("AllProducts").whereEqualTo("type", "tea&coffee").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //Getting Drink
        else if (type != null && type.equalsIgnoreCase("drink")) {
            firestore.collection("AllProducts").whereEqualTo("type", "drink").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //Getting Sweet
        else if (type != null && type.equalsIgnoreCase("sweet")) {
            firestore.collection("AllProducts").whereEqualTo("type", "sweet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //Getting Grain
        else if (type != null && type.equalsIgnoreCase("grain")) {
            firestore.collection("AllProducts").whereEqualTo("type", "grain").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //Getting FoodGrain
        else if (type != null && type.equalsIgnoreCase("foodgrain")) {
            firestore.collection("AllProducts").whereEqualTo("type", "foodgrain").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //Getting Sauces
        else if (type != null && type.equalsIgnoreCase("sauces")) {
            firestore.collection("AllProducts").whereEqualTo("type", "sauces").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                                viewAllModelList.add(viewAllModel);
                                viewAllAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);
                                progressBar2.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            if (viewAllModelList.isEmpty()) {
                                progressBar.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);
                                progressBar2.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
        //Getting Cleaning
        else if (type != null && type.equalsIgnoreCase("cleaning")) {
            firestore.collection("AllProducts").whereEqualTo("type", "cleaning").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //Getting Personal Care
        else if (type != null && type.equalsIgnoreCase("personalcare")) {
            firestore.collection("AllProducts").whereEqualTo("type", "personalcare").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (viewAllModelList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }

        /*String[] cat = new String[]{"fruit", "vegetable", "personalcare", "cleaning", "sauces", "foodgrain", "grain", "sweet", "drink", "tea&coffee", "backery", "dairy", "snack"};
        category = new ArrayList<String>();
        category.addAll(Arrays.asList(cat));
        category.addAll(Arrays.asList(cat));
        for (int i = 0; i < category.size(); i++) {
            if (type != null && type.equalsIgnoreCase(category.get(i))) {
                firestore.collection("AllProducts").whereEqualTo("type", cat[i]).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                            viewAllModelList.add(viewAllModel);
                            viewAllAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            progressBar1.setVisibility(View.GONE);
                            progressBar2.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                progressBar2.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        }*/
    }
}