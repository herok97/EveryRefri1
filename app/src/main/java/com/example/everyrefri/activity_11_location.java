package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.internal.HTTPRequest;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.io.IOException;
import java.util.List;

public class activity_11_location extends AppCompatActivity implements OnMapReadyCallback{

    private MapView mapView;
    private EditText txtsearch;
    private Button bt_search;
    private Button bt_save;
    private ImageButton ibt_back;
    private TextView locationText;
    private User user;
    private FirebaseAuth fireAuth;
    private FirebaseStorage storage;
    private Uri filePath;
    private DatabaseReference ref;
    Marker marker=new Marker();
    CameraUpdate cameraUpdate;
    private NaverMap nmap1;
    private NaverMap nmap2;
    //private LatLng latLng;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_11_location);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        storage = FirebaseStorage.getInstance();
        Intent intent =getIntent();
        user = new User();
        user.getUserFromIntent(intent);

        bt_search= findViewById(R.id.bt_search);
        txtsearch=findViewById(R.id.et_location);
        locationText=findViewById(R.id.tv_view);
        ibt_back=findViewById(R.id.ibt_back11);
        bt_save=findViewById(R.id.bt_locationsave);



        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_7_myprofile.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,7);
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.email.substring(0, user.email.indexOf("@")));
                ref.child("location").setValue(txtsearch.getText().toString());

                Intent intent = new Intent(getApplicationContext(), activity_7_myprofile.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,7);
                Toast.makeText(getApplicationContext(), "위치정보를 저장했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        bt_search.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                String  txt = txtsearch.getText().toString();
                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try{
                    addresses= geocoder.getFromLocationName(txt,3);
                    if(addresses!=null&&!addresses.equals("")){

                        search(addresses);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }



    public void search(List<Address> addresses) {
        Address address= addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());

        String addressText=String.format(
                "%s, %s",
                address.getMaxAddressLineIndex()>0 ? address.getAddressLine(0):" ",address.getFeatureName()
        );
        locationText.setVisibility(View.VISIBLE);
        locationText.setText("Latitude"+address.getLatitude()+"Longitude"+address.getLongitude()+"\n"+addressText);


        marker.setPosition(latLng);


        cameraUpdate= CameraUpdate.scrollTo(latLng);

        nmap2.moveCamera(cameraUpdate); //(이거땜에 오류뜸 진짜 개빡친다....
        //}

    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        nmap1=naverMap;
        nmap2=naverMap;
        LatLng def = new LatLng(37.24341208419288,127.08250012634373);
        marker.setPosition(def);
        marker.setMap(nmap1);

        cameraUpdate = CameraUpdate.scrollTo(def);
        nmap1.moveCamera(cameraUpdate);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }







}

