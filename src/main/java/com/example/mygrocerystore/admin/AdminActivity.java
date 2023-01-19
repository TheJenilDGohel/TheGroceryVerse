package com.example.mygrocerystore.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.activities.LoginRegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    private Button logout;
    private RelativeLayout user_details, popular_products, product_category, all_products,order_product;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_green));

        //LOGOUT
        logout = (Button) findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth != null) {
                    auth.signOut();
                    startActivity(new Intent(AdminActivity.this, LoginRegisterActivity.class));
                    finish();
                    Toast.makeText(AdminActivity.this, "Logout Success.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //USER_DETAILS
        user_details = (RelativeLayout) findViewById(R.id.user_details);
        user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, UserdetailsActivity.class));
            }
        });

        //POPULAR_PRODUCTS
        popular_products = (RelativeLayout) findViewById(R.id.popular_products);
        popular_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, PopularproductActivity.class));
            }
        });

        //PRODUCT_CATEGORY
        product_category = (RelativeLayout) findViewById(R.id.product_category);
        product_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, CategoryActivity.class));
            }
        });

        //ALL PRODUCTS
        all_products = (RelativeLayout) findViewById(R.id.all_products);
        all_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AllProductActivity.class));
            }
        });

        //ProductOrders
        order_product = (RelativeLayout) findViewById(R.id.order_product);
        order_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, OrderActivity.class));
            }
        });
    }
}