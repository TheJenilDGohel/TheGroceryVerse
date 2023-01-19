package com.example.mygrocerystore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mygrocerystore.MainActivity;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.admin.AdminActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen_Activity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;
    private LottieAnimationView lottieAnimationView;
    private TextView the, grocery, store;
    private Animation The, Grocery, Store;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_activty);

        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_green));

        //TextView Animation
        the = findViewById(R.id.the);
        grocery = findViewById(R.id.grocery);
        store = findViewById(R.id.verse);

        The = AnimationUtils.loadAnimation(this, R.anim.left_to_right_splash);
        Grocery = AnimationUtils.loadAnimation(this, R.anim.right_to_left_splash);
        Store = AnimationUtils.loadAnimation(this, R.anim.left_to_right_splash);

        the.setAnimation(The);
        grocery.setAnimation(Grocery);
        store.setAnimation(Store);

        //Splash Screen Code to call new Activity after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                auth = FirebaseAuth.getInstance();
                firestore = FirebaseFirestore.getInstance();
                if (auth.getCurrentUser() != null) {
                    DocumentReference reference = firestore.collection("Users").document(auth.getCurrentUser().getUid());
                    reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.getString("isAdmin") != null) {
                                startActivity(new Intent(SplashScreen_Activity.this, AdminActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashScreen_Activity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(SplashScreen_Activity.this, HomeActivity.class));
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}