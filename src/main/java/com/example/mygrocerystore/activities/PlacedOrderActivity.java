package com.example.mygrocerystore.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adapters.MyCartAdapter;
import com.example.mygrocerystore.models.MyCartModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PlacedOrderActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //MAP
    GoogleMap mMap;

    private FusedLocationProviderClient mLocationClient;
    private int GPS_REQUEST_CODE = 9001;

    double latitude = 0.0, longitude = 0.0;


    FirebaseAuth auth;
    FirebaseFirestore firestore;

    List<MyCartModel> cartModelList;
    MyCartAdapter myCartAdapter;

    DocumentReference documentReference;

    EditText locSearch;
    ImageView searchLoc;
    boolean isPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(PlacedOrderActivity.this, R.color.dark_green));

        checkMyPermission();
        initMap();
        mLocationClient = new FusedLocationProviderClient(PlacedOrderActivity.this);


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        documentReference = firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid());

        locSearch = findViewById(R.id.txtSearch);
        searchLoc = findViewById(R.id.imgSearch);
        searchLoc.setOnClickListener(this::getLocate);

        List<MyCartModel> list = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");

        if (list != null && list.size() > 0) {
            EditText name = findViewById(R.id.order_customer_name);
            EditText address = findViewById(R.id.order_customer_address);
            EditText email = findViewById(R.id.order_customer_email);
            EditText phone = findViewById(R.id.order_customer_number);
            Button button = findViewById(R.id.confirm);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar c = Calendar.getInstance();
                    String date = sdf.format(c.getTime());


                    final String productStatus = "Pending";
                    final String currentUser = name.getText().toString();
                    final String currentUserAddress = address.getText().toString();
                    final String currentUserEmail = email.getText().toString();
                    final String currentUserPhone = phone.getText().toString();
                    final String latiTude = String.valueOf(latitude);
                    final String longiTude = String.valueOf(longitude);
                    final String orderId = getSaltString();
                    final HashMap<String, Object> cartMap1 = new HashMap<>();
                    cartMap1.put("userId", auth.getCurrentUser().getUid());
                    cartMap1.put("orderId", orderId);
                    cartMap1.put("status", productStatus);
                    cartMap1.put("current_date", date);
                    cartMap1.put("user_name", currentUser);
                    cartMap1.put("user_address", currentUserAddress);
                    cartMap1.put("user_email", currentUserEmail);
                    cartMap1.put("user_phone", currentUserPhone);
                    cartMap1.put("latitude", latiTude);
                    cartMap1.put("longitude", longiTude);
                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid()).
                            collection("Order").document(orderId).set(cartMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String docId = firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("Order").document().getPath();
                                    Log.e("TAG", "onComplete: "+docId);
                                    fetchProduct(list,orderId);
                                }
                            });
                }
            });

        }
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = "GV-"+salt.toString();
        return saltStr;

    }

    private void fetchProduct(List<MyCartModel> list, String orderId) {
        for (MyCartModel model : list) {
            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("productName", model.getProductName());
            cartMap.put("productPrice", model.getProductPrice());
            cartMap.put("currentDate", model.getCurrentDate());
            cartMap.put("currentTime", model.getCurrentTime());
            cartMap.put("totalQuantity", model.getTotalQuantity());
            cartMap.put("totalPrice", model.getTotalPrice());

            firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid()).
                    collection("Order").document(orderId).collection("productList")
                    .add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Intent intent = new Intent(PlacedOrderActivity.this, FinalActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }

    }

    private void getLocate(View view) {
        String locationName = locSearch.getText().toString();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 3);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                gotoLocation(address.getLatitude(), address.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));
                latitude = address.getLatitude();
                longitude = address.getLongitude();
                Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {

        }
    }


    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(PlacedOrderActivity.this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getCurrLoc();
                return true;
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getCurrLoc() {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                gotoLocation(location.getLatitude(), location.getLongitude());
            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        mMap.moveCamera(cameraUpdate);
    }

    private void initMap() {
        if (isPermissionGranted) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
