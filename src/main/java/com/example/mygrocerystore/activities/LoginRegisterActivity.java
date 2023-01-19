package com.example.mygrocerystore.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mygrocerystore.MainActivity;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.admin.AdminActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginRegisterActivity extends AppCompatActivity {
    LinearLayoutCompat loginCard, registerCard, gotoRegister_layout, gotologin_layout;
    Animation loginUp, loginDown, registerUp, registerDown, LeftToRight, RightToLeft, LeftToRight1, RightToLeft1;
    AppCompatButton goToRegisterBtn, goToLoginBtn;
    TextView forgetPassword;

    //validation
    private boolean valid = true;


    //Register Items.
    AppCompatEditText registerName, registerEmail, registerPassword, registerRepassword;
    AppCompatButton register;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    //Login Items.
    AppCompatEditText loginEmail, loginPassword;
    AppCompatCheckBox checkBox, checkBox1;
    AppCompatButton login;
    private ImageView progressBar, progressBar1;
    private LottieAnimationView progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        loginCard = findViewById(R.id.login_layout);
        registerCard = findViewById(R.id.register_layout);
        gotologin_layout = findViewById(R.id.gotologin_layout);
        gotoRegister_layout = findViewById(R.id.gotoRegister_layout);

        //forget password
        forgetPassword = findViewById(R.id.forgetPasswordText);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });


        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        //Go To Login Page....
        goToLoginBtn = findViewById(R.id.gotoLogin_btn);
        goToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeftToRight1 = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.left_to_right_1);
                gotoRegister_layout.setAnimation(LeftToRight1);
                RightToLeft1 = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.right_to_left_1);
                gotologin_layout.setAnimation(RightToLeft1);
                loginDown = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.login_up_to_down);
                registerDown = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.register_up_to_down);
                loginCard.setAnimation(loginDown);
                registerCard.setAnimation(registerDown);
                loginCard.setVisibility(View.VISIBLE);
                registerCard.setVisibility(View.GONE);
                window.setStatusBarColor(ContextCompat.getColor(LoginRegisterActivity.this, R.color.white));
            }
        });

        //Go To Register Page....
        goToRegisterBtn = findViewById(R.id.gotoRegister_btn);
        goToRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeftToRight = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.left_to_right);
                gotoRegister_layout.setAnimation(LeftToRight);
                RightToLeft = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.right_to_left);
                gotologin_layout.setAnimation(RightToLeft);
                registerUp = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.register_down_to_up);
                loginUp = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.login_down_to_up);
                registerCard.setAnimation(registerUp);
                loginCard.setAnimation(loginUp);
                loginCard.setVisibility(View.GONE);
                registerCard.setVisibility(View.VISIBLE);
                window.setStatusBarColor(ContextCompat.getColor(LoginRegisterActivity.this, R.color.white));
            }
        });

//        Register Id Fetch
        checkBox1 = (AppCompatCheckBox) findViewById(R.id.checkbox1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    registerPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    registerRepassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    registerPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    registerRepassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        registerName = (AppCompatEditText) findViewById(R.id.register_name);
        registerEmail = (AppCompatEditText) findViewById(R.id.register_email);
        registerPassword = (AppCompatEditText) findViewById(R.id.register_password);
        registerRepassword = (AppCompatEditText) findViewById(R.id.register_repassword);
        register = (AppCompatButton) findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateName(registerName);
                validateEmail(registerEmail);
                validatePassword(registerPassword);
                validateRePassword(registerPassword, registerRepassword);
                Toast.makeText(LoginRegisterActivity.this, "Valid :" + valid, Toast.LENGTH_SHORT).show();
                if (valid) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar1.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.VISIBLE);
                    userRegister(registerName, registerEmail, registerPassword);
                }
            }
        });

        //        Login Id Fetch.
        loginEmail = (AppCompatEditText) findViewById(R.id.login_email);
        loginPassword = (AppCompatEditText) findViewById(R.id.login_password);

        //ProgresBar ID Fetch
        progressBar = findViewById(R.id.progress_bar);
        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);

        //Gone ProgresBar
        progressBar.setVisibility(View.GONE);
        progressBar1.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);


        checkBox = (AppCompatCheckBox) findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        login = (AppCompatButton) findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail(loginEmail);
                validatePassword(loginPassword);
                if (valid) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar1.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    progressBar1.setVisibility(View.GONE);
                                    progressBar2.setVisibility(View.GONE);
                                    Toast.makeText(LoginRegisterActivity.this, "Error :" + e, Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String user_id = task.getResult().getUser().getUid();
                                        userLogin(user_id);
                                    }
                                }
                            });
                }
            }
        });

    }

    ProgressDialog loadingBar;

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);

        // write the email using which you registered
        emailet.setHint("Enter Your Email");
        emailet.setText("");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailet.getText().toString().trim();
                if (email.isEmpty()) {
                    emailet.setError("Pl..Fill Email Field");
                } else {
                    beginRecovery(email);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {
        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if (task.isSuccessful()) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(LoginRegisterActivity.this, "Done sent", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(LoginRegisterActivity.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void userLogin(String user_id) {
        DocumentReference reference = firestore.collection("Users").document(user_id);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("isAdmin") != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginRegisterActivity.this, AdminActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(LoginRegisterActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                }
                if (documentSnapshot.getString("isUser") != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(LoginRegisterActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void userRegister(AppCompatEditText registerName, AppCompatEditText registerEmail, AppCompatEditText registerPassword) {
        String name = registerName.getText().toString();
        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        String user_id = task.getResult().getUser().getUid();
                        DocumentReference reference = firestore.collection("Users").document(user_id);
                        Map<String, Object> map = new HashMap<>();
                        map.put("Name", name);
                        map.put("Email", email);
                        map.put("Password", password);
                        map.put("isUser", "1");
                        map.put("UserType", "user");
                        reference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(LoginRegisterActivity.this, "Register Success.", Toast.LENGTH_SHORT).show();
                                loginDown = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.login_up_to_down);
                                registerDown = AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.register_up_to_down);
                                loginCard.setAnimation(loginDown);
                                registerCard.setAnimation(registerDown);
                                loginCard.setVisibility(View.VISIBLE);
                                registerCard.setVisibility(View.GONE);
                                registerName.setText("");
                                registerEmail.setText("");
                                registerPassword.setText("");
                                registerRepassword.setText("");
                            }
                        });
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(LoginRegisterActivity.this, "E:" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private boolean validateRePassword(AppCompatEditText registerPassword, AppCompatEditText registerRepassword) {
        String Password = registerPassword.getText().toString().trim();
        String RePassword = registerRepassword.getText().toString().trim();
        if (RePassword.isEmpty()) {
            Toast.makeText(this, "Please Enter Re-Password !", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (!Password.equals(RePassword)) {
            Toast.makeText(this, "Password Not Match !", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    private boolean validatePassword(AppCompatEditText registerPassword) {
        String Password = registerPassword.getText().toString().trim();
        if (Password.isEmpty()) {
            Toast.makeText(this, "Please Enter Password !", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    private boolean validateEmail(AppCompatEditText registerEmail) {
        String Email = registerEmail.getText().toString().trim();
        if (Email.isEmpty()) {
            Toast.makeText(this, "Please Enter Email !", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            Toast.makeText(this, "Please Enter Valid Email Adderess !", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    private boolean validateName(AppCompatEditText registerName) {
        String Name = registerName.getText().toString().trim();
        if (Name.isEmpty()) {
            Toast.makeText(this, "Please Enter Name !", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }


}