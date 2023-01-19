package com.example.mygrocerystore.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mygrocerystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddProductActivity extends AppCompatActivity {

    EditText prod_name,prod_type,prod_price,prod_rating,prod_description;
    Button submit,back,prod_imgurl;
    FirebaseStorage storage;
    String url;
    SweetAlertDialog sweetAlertDialog;

    private StorageReference Folder;

    private static final int ImageBack =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        Folder = FirebaseStorage.getInstance().getReference().child("ALL PRODUCTS");

        prod_name=(EditText) findViewById(R.id.prod_name);
        prod_type=(EditText) findViewById(R.id.prod_type);
        prod_price=(EditText) findViewById(R.id.prod_price);
        prod_rating=(EditText) findViewById(R.id.prod_rating);
        prod_description=(EditText) findViewById(R.id.prod_description);
        prod_imgurl=(Button) findViewById(R.id.prod_imgurl);

        storage=FirebaseStorage.getInstance();

        back=(Button) findViewById(R.id.add_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AllProductActivity.class));
                finish();
            }
        });

        submit=(Button) findViewById(R.id.add_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processInsert();
            }
        });
        prod_imgurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,ImageBack);
            }
        });
    }

    private void processInsert() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference pop_prod = db.collection("AllProducts").document();

        Map<String,Object> map=new HashMap<>();
        map.put("name",prod_name.getText().toString());
        map.put("type",prod_type.getText().toString());
        map.put("rating",prod_rating.getText().toString());
        map.put("price",Integer.parseInt(prod_price.getText().toString()));
        map.put("description",prod_description.getText().toString());
        map.put("img_url",url);
        pop_prod
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(AddpopularproductActivity.this, "Inserted Success.", Toast.LENGTH_SHORT).show();
                        new SweetAlertDialog(AddProductActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Product Add Success.")
                                .show();
                        prod_name.setText("");
                        prod_type.setText("");
                        prod_rating.setText("");
                        prod_price.setText("");
                        prod_description.setText("");
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ImageBack)
        {
            if(resultCode==RESULT_OK)
            {
                Uri ImageData=data.getData();
                StorageReference ImageName=Folder.child("image"+ImageData.getLastPathSegment());
                ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddProductActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url=uri.toString();
                                Toast.makeText(AddProductActivity.this, "Product Picture Uploaded.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        sweetAlertDialog = new SweetAlertDialog(AddProductActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                        sweetAlertDialog.setTitleText("Image Uploading Please Wait...");
                        sweetAlertDialog.show();
                    }
                });
            }
        }
    }
}