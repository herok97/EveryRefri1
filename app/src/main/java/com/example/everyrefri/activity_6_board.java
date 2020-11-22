package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class activity_6_board extends AppCompatActivity {

    private FirebaseAuth fireAuth;
    private ImageButton ibt_back;
    private SearchView searchView;//searchview이용
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_board);

        ibt_back = findViewById(R.id.ibt_back6);
        searchView= findViewById(R.id.sv_board);

        ibt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // back 버튼 클릭시 main으로 이동(?)
                Intent intent = new Intent(getApplicationContext(), activity_4_main.class);
                startActivityForResult(intent,4);
            }
        });

        //좀더공부해봐야함..
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TextView text = (TextView)findViewById(R.id.sv_board);
                return false;//입력받은 문자열 처리
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}