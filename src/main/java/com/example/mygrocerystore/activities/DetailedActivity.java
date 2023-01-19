package com.example.mygrocerystore.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    TextView quantity;
    TextView totalPriceText;
    String getTotalPrice;
    int totalQuantity=1;
    int totalPrice=0;

    ImageView detailedImg;
    TextView price,name,description;
    Button addtocart;
    ImageView addItem,removeItem;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    ImageView backbutton,carticon;


    ViewAllModel viewAllModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();


        final Object object=getIntent().getSerializableExtra("detail");

        if(object instanceof ViewAllModel)
        {
            viewAllModel=(ViewAllModel) object;
        }

        quantity=findViewById(R.id.quantity);
        detailedImg=findViewById(R.id.detailded_img);
        price=findViewById(R.id.detailed_price);
        name=findViewById(R.id.detailded_name);
        description=findViewById(R.id.detailed_description);
        addtocart=findViewById(R.id.add_to_cart);
        addItem=findViewById(R.id.add_item);
        removeItem=findViewById(R.id.remove_item);
        totalPriceText=findViewById(R.id.totalPriceTextView1);

        backbutton = findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(DetailedActivity.this,ViewAllActivity.class));
                DetailedActivity.super.onBackPressed();
            }
        });
        carticon = findViewById(R.id.go_to_cart);
        carticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(DetailedActivity.this,MyCartsFragment.class);
            }
        });

        if(viewAllModel != null)
        {
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailedImg);
            name.setText(viewAllModel.getName());
            description.setText(viewAllModel.getDescription());
            price.setText("Price : Rs. "+viewAllModel.getPrice()+"/KG");
            totalPriceText.setText("Rs. "+viewAllModel.getPrice());


            totalPrice=viewAllModel.getPrice()*totalQuantity;

            if(viewAllModel.getType().equals("eggs"))
            {
                price.setText("Price : Rs."+viewAllModel.getPrice()+"/Dozen");
                totalPrice=viewAllModel.getPrice()*totalQuantity;
            }
            if(viewAllModel.getType().equals("milk"))
            {
                price.setText("Price : Rs."+viewAllModel.getPrice()+"/Liter");
                totalPrice=viewAllModel.getPrice()*totalQuantity;
            }
            addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addedToCart();
                }   
            });
            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(totalQuantity <10)
                    {
                        totalQuantity++;
                        if(totalQuantity==10){Toast.makeText(DetailedActivity.this, "Maximum 10 Item.", Toast.LENGTH_SHORT).show();}
                        quantity.setText(String.valueOf(totalQuantity));
                        totalPrice=viewAllModel.getPrice()*totalQuantity;
                        getTotalPrice= String.valueOf(totalPrice);
                        totalPriceText.setText("Rs. "+getTotalPrice);
                    }
                }
            });
            removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(totalQuantity > 1)
                    {
                        totalQuantity--;
                        quantity.setText(String.valueOf(totalQuantity));
                        totalPrice=viewAllModel.getPrice()*totalQuantity;
                        getTotalPrice= String.valueOf(totalPrice);
                        totalPriceText.setText("Rs. "+getTotalPrice);
                    }
                }
            });
        }
    }

    private void addedToCart() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName",viewAllModel.getName());
        cartMap.put("productImage",viewAllModel.getImg_url());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("productType",viewAllModel.getType().toString());
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}