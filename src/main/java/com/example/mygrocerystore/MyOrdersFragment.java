package com.example.mygrocerystore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygrocerystore.models.UserOrderModel;
import com.example.mygrocerystore.adapters.UserOrderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    RecyclerView recyclerView;
    UserOrderAdapter userOrderAdapter;
    List<UserOrderModel> userOrderModelList;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    Context context;
    ImageButton cancel_order;

    RelativeLayout emptyorder;

    public MyOrdersFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_my_orders, container, false);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        emptyorder = root.findViewById(R.id.emptyorder);
        recyclerView = root.findViewById(R.id.myorderData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userOrderModelList = new ArrayList<>();
        userOrderAdapter = new UserOrderAdapter(getActivity(),userOrderModelList);

        emptyorder.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setAdapter(userOrderAdapter);

        String userID = firebaseAuth.getCurrentUser().getUid().toString();

        if(userID != null)
        {
            firestore.collection("CurrentUser").document(userID).
                    collection("Order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            String documentId = documentSnapshot.getId();
                            UserOrderModel userOrderModel = documentSnapshot.toObject(UserOrderModel.class);
                            userOrderModel.setDocumentId(documentId);
                            userOrderModelList.add(userOrderModel);
                            userOrderAdapter.notifyDataSetChanged();

                            emptyorder.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        }

                        if (userOrderModelList.isEmpty())
                        {
                            emptyorder.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

                        }
                    }
                }
            });
        }


        return root;
    }
}