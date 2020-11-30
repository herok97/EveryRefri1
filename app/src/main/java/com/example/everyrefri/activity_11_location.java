package com.example.everyrefri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.internal.HTTPRequest;
import com.naver.maps.map.util.FusedLocationSource;

public class activity_11_location extends AppCompatActivity implements OnMapReadyCallback{
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

    }

    // static final String TAG="LOCATION";
    //MapFragment mapFragment;
    //NaverMap map;

    //위치 검색해서 지도에서 넣는건 쉽지 않아보여서
    // 현재 위치 파악으로 버튼눌러 그위치를 저장하는 식으로 하려고하는 중인데 원래나오던 지도도 안나오는중..
    //오류 더 찾고 확인해야함

    public class MapApi extends AppCompatActivity implements OnMapReadyCallback {

        private MapView mapView;

        private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
        private FusedLocationSource locationSource;
        private NaverMap naverMap;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_11_location);


            mapView = findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);

            locationSource =
                    new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);



        }
        //위치정보 권한 설정
        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               @NonNull String[] permissions,  @NonNull int[] grantResults) {
            if (locationSource.onRequestPermissionsResult(
                    requestCode, permissions, grantResults)) {
                return;
            }
            super.onRequestPermissionsResult(
                    requestCode, permissions, grantResults);
        }

        @Override
        public void onMapReady(@NonNull NaverMap naverMap) {
            this.naverMap = naverMap;
            // NaverMap 객체 받아서 NaverMap 객체에 위치 소스 지정
            naverMap.setLocationSource(locationSource);
            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

            UiSettings uiSettings = naverMap.getUiSettings();
            uiSettings.setCompassEnabled(true); // 나침반
            uiSettings.setScaleBarEnabled(true); // 거리
            uiSettings.setZoomControlEnabled(true); // 줌
            uiSettings.setLocationButtonEnabled(true); // 내가 있는곳


        }
    }


    /*protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_11_location);

       mapFragment=(MapFragment)getSupportFragmentManager().findFragmentById(R.id.mapView);
        //mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override
            public void onMapReady(@NonNull NaverMap naverMap) {
                Log.d(TAG,"Naver MAP is ready");
                map=naverMap;
            }
        });

        try{
            MapsInitializer.initialize(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        Button button = (Button)findViewById(R.id.bt_locationsave);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new LocationRequest();
            }
        });*/

    }

    /*public void onLocationChanged(Location location){

    }


    private void showCurrentLocation(double latitude,double longitude){
        LatLng curPoint = new LatLng(latitude,longitude);
        map.moveCamera(CameraUpdate.scrollAndZoomTo(curPoint,15));

    }*/

/*
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

    }
}*/
