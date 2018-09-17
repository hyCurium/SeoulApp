package com.example.jungeb.seoulapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout linearMainTour, linearMainFacilities;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    TourFragment tourFragment;
    FacilitiesFragment facilitiesFragment;

    ImageView ivTourinfoBar, ivFacilitiesBar;
    TextView tvTourinfo, tvFacilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearMainTour = (LinearLayout) findViewById(R.id.linearMainTour);
        linearMainFacilities = (LinearLayout) findViewById(R.id.linearMainFacilities);

        linearMainTour.setOnClickListener(this);
        linearMainFacilities.setOnClickListener(this);


        //프래그먼트 객체생성
        tourFragment = new TourFragment();
        facilitiesFragment = new FacilitiesFragment();

        setFrag(0);

        //위에 탭 색 변경부분
        ivTourinfoBar = (ImageView) findViewById(R.id.ivTourinfoBar);
        ivFacilitiesBar = (ImageView) findViewById(R.id.ivFacilitiesBar);

        tvTourinfo = (TextView) findViewById(R.id.tvTourinfo);
        tvFacilities = (TextView) findViewById(R.id.tvFacilities);

        ivTourinfoBar.setImageResource(R.drawable.rectangle_purple);
        ivFacilitiesBar.setImageResource(R.drawable.rectangle_gray);
        tvTourinfo.setTextColor(Color.parseColor("#5d38db"));
        tvFacilities.setTextColor(Color.parseColor("#949494"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearMainTour:
                setFrag(0);

                ivTourinfoBar.setImageResource(R.drawable.rectangle_purple);
                ivFacilitiesBar.setImageResource(R.drawable.rectangle_gray);

                tvTourinfo.setTextColor(Color.parseColor("#5d38db"));
                tvFacilities.setTextColor(Color.parseColor("#949494"));
                break;
            case R.id.linearMainFacilities:
                setFrag(1);

                ivTourinfoBar.setImageResource(R.drawable.rectangle_gray);
                ivFacilitiesBar.setImageResource(R.drawable.rectangle_purple);

                tvTourinfo.setTextColor(Color.parseColor("#949494"));
                tvFacilities.setTextColor(Color.parseColor("#5d38db"));
                break;
        }

    }


    //프래그먼트 교체 메소드
    public void setFrag(int i) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (i){
            case 0:
                fragmentTransaction.replace(R.id.mainFrame, tourFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.mainFrame, facilitiesFragment);
                fragmentTransaction.commit();
                break;
        }
    }

}
