package com.example.mygrocerystore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygrocerystore.MainActivity;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adapters.MyCartAdapter;
import com.example.mygrocerystore.models.MyCartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FinalActivity extends AppCompatActivity {

    TextView GoToHome;

    List<MyCartModel> cartModelList;
    MyCartAdapter myCartAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

//        while (cartModelList!=null)
//        {
//            MyCartModel cartModel=new MyCartModel();
//            firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
//                    .collection("AddToCart").document(cartModel.getDocumentId()).delete()
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(FinalActivity.this, "Cart Deleted.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }

        GoToHome=findViewById(R.id.gotohome);
        GoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FinalActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}