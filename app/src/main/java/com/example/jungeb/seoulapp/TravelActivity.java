package com.example.jungeb.seoulapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TravelActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnTravelBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        btnTravelBack = (Button)findViewById(R.id.btnTravelBack);


        //뒤로가기 버튼 눌렀을 때
        btnTravelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
