package com.example.everyrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class activity_13_write extends AppCompatActivity {
    TextView textView;
    String[] items = {"채소류", "과일류", "인스턴트류", "기타"};
    String[] methods = {"냉장", "냉동", "실온"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_13_write);

        Spinner sp_category = findViewById(R.id.sp_category);
        Spinner sp_storage = findViewById(R.id.sp_storage);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp_category.setAdapter(adapter);
        sp_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(items[position]);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("종류");
            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, methods
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp_storage.setAdapter(adapter1);
        sp_storage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(methods[position]);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("방법");
            }
        });


    }
}