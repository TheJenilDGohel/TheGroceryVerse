package com.example.mygrocerystore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mygrocerystore.activities.PlacedOrderActivity;
import com.example.mygrocerystore.adapters.MyCartAdapter;
import com.example.mygrocerystore.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyCartsFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;
    TextView overTotalAmount;
    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;
    FloatingActionButton buyNow;
    ImageView imageView;
    private ImageView progressBar, progressBar1;
    private LottieAnimationView progressBar2;
    int totalBill;

    public MyCartsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);

        //ProgresBar ID Fetch
        progressBar = root.findViewById(R.id.progress_bar);
        progressBar1 = root.findViewById(R.id.progress_bar1);
        progressBar2 = root.findViewById(R.id.progress_bar2);

        //Gone ProgresBar
        if (cartModelList == null) {
            progressBar.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            progressBar1.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
        }


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recycleview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        overTotalAmount = root.findViewById(R.id.textView3);
        buyNow = root.findViewById(R.id.buy_now);
        imageView = root.findViewById(R.id.cart_products_img);

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);


        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                String documentId = documentSnapshot.getId();

                                MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                                cartModel.setDocumentId(documentId);
                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);
                                progressBar2.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            calculaterTotalAmount(cartModelList);
                        }
                    }
                });
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butNowAt();
            }
        });

        return root;
    }

    private void butNowAt() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are You Sure ?")
                .setConfirmText("YES")
                .setCancelText("NO")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
                        intent.putExtra("itemList", (Serializable) cartModelList);
                        startActivity(intent);
                    }
                }).show();
    }

    private void calculaterTotalAmount(List<MyCartModel> cartModelList) {
        double totalAmount = 0.0;
        for (MyCartModel myCartModel : cartModelList) {
            totalAmount += myCartModel.getTotalPrice();
        }
        overTotalAmount.setText("Total Amount :" + totalAmount);
        refresh(500);
        if (totalAmount == 0.0) {
            buyNow.setVisibility(View.GONE);
        }
    }

    private void refresh(int millseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                calculaterTotalAmount(cartModelList);
            }
        };
        handler.postDelayed(runnable, millseconds);
    }
}