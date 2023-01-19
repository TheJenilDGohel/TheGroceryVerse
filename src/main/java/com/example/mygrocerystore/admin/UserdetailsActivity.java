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
import com.example.mygrocerystore.adminAdapters.UserDetailsAdapter;
import com.example.mygrocerystore.adminModels.UserDetailsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserdetailsActivity extends AppCompatActivity {

    RecyclerView user_data;
    List<UserDetailsModel> userDetailsModelList;
    UserDetailsAdapter userDetailsAdapter;
    FirebaseFirestore firestore;

    SearchView searchView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);
        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_green));

        //BACK BUTTON
        back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserdetailsActivity.this,AdminActivity.class));
                finish();
            }
        });

        //GET THE USER DATA.
        firestore = FirebaseFirestore.getInstance();
        user_data = (RecyclerView) findViewById(R.id.user_data);
        user_data.setNestedScrollingEnabled(false);
        user_data.setLayoutManager(new LinearLayoutManager(this));
        userDetailsModelList = new ArrayList<>();
        userDetailsAdapter = new UserDetailsAdapter(this, userDetailsModelList);
        user_data.setAdapter(userDetailsAdapter);

        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        UserDetailsModel userDetailsModel = document.toObject(UserDetailsModel.class);
                        String USERID = document.getId();
                        userDetailsModel.setDocumentId(USERID);
                        userDetailsModelList.add(userDetailsModel);
                        userDetailsAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(UserdetailsActivity.this, "Error :" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //SEARCH VIEW
        searchView=(SearchView) findViewById(R.id.search);
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
        List<UserDetailsModel> filteredlist = new ArrayList<>();
        for (UserDetailsModel userDetailsModel : userDetailsModelList) {
            if (userDetailsModel.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(userDetailsModel);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No User Found.", Toast.LENGTH_SHORT).show();
        } else {
            userDetailsAdapter.setFilteredList(filteredlist);
        }
    }
}
