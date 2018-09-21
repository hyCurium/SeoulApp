package com.example.jungeb.seoulapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class TravelActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btnTravelBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        btnTravelBack = (ImageButton)findViewById(R.id.imbTravelBack);


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
