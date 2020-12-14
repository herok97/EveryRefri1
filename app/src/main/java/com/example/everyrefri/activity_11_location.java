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

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
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
    private Button save;
    private ImageButton ibt_back;
    private TextView locationText;
    private User user;
    private FirebaseAuth fireAuth;
    private FirebaseStorage storage;
    private Uri filePath;
    private NaverMap naverMap;
    Marker marker=new Marker();
    private CameraUpdate cameraUpdate;
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
       // user.getUserFromIntent(intent);

        bt_search= findViewById(R.id.bt_search);
        txtsearch=findViewById(R.id.et_location);
        locationText=findViewById(R.id.tv_view);
        ibt_back=findViewById(R.id.ibt_back11);



        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_7_myprofile.class);
                intent = user.setUserToIntent(intent);
                startActivityForResult(intent,7);
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
        //Marker marker=new Marker();

        Marker marker1= new Marker();
        marker1.setPosition(latLng);
        marker1.setMap(naverMap);
        cameraUpdate= CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Fly,3000);
//        naverMap.moveCamera(cameraUpdate);
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(address.getLatitude(),address.getLongitude())
                ,16);

        NaverMapOptions options = new NaverMapOptions().camera(cameraPosition);


        //cameraUpdate = CameraUpdate.scrollTo(new LatLng(address.getLatitude(),address.getLongitude()));
        //naverMap.moveCamera(cameraUpdate);
        //CameraUpdate cameraupdate=new CameraUpdate(cameraPosition);

    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

       // marker.setPosition(new LatLng(address.getLatitude(),address.getLongitude()));
       // marker.setMap(naverMap);
       // cameraUpdate = CameraUpdate.scrollTo(new LatLng(address.getLatitude(),address.getLongitude()));
       // naverMap.moveCamera(cameraUpdate);
        LatLng def = new LatLng(37.24341208419288,127.08250012634373);
        marker.setPosition(def);
        marker.setMap(naverMap);

        cameraUpdate = CameraUpdate.scrollTo(def);
        naverMap.moveCamera(cameraUpdate);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


    }




}

