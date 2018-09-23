package com.example.jungeb.seoulapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jungeb.seoulapp.PushAlarm.PushAlarm;
import com.example.jungeb.seoulapp.fragment.FacilitiesFragment;
import com.example.jungeb.seoulapp.fragment.TourFragment;
import com.example.jungeb.seoulapp.sqliteC.SqliteTour;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linearMainTour, linearMainFacilities;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    TourFragment tourFragment;
    FacilitiesFragment facilitiesFragment;

    ImageView ivTourinfoBar, ivFacilitiesBar;
    TextView tvTourinfo, tvFacilities;

    SqliteTour sqlite;

    PushAlarm pushAlarm;

    Intent GPSintent;
    double Latitude, Longitude;

    Locale systemLocale;
    String strLanguage;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(GPSLocation.str_receiver));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(GPSintent);
        unregisterReceiver(broadcastReceiver);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearMainTour = (LinearLayout) findViewById(R.id.linearMainTour);
        linearMainFacilities = (LinearLayout) findViewById(R.id.linearMainFacilities);

        linearMainTour.setOnClickListener(this);
        linearMainFacilities.setOnClickListener(this);

        sqlite = new SqliteTour(this, "Tour"); //DB 생성

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

        pushAlarm = new PushAlarm(getApplicationContext());

        GPSintent = new Intent(getApplicationContext(), GPSLocation.class); //GPS 서비스 시작
        startService(GPSintent);

        systemLocale = getResources().getConfiguration().locale;
        strLanguage = systemLocale.getLanguage(); //시스템 언어 가져오기
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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MainActivity","Locaiton 수신 완료");
            Latitude = Double.valueOf(intent.getStringExtra("latutide"));
            Longitude = Double.valueOf(intent.getStringExtra("longitude")); //GPS 서비스에서 값 가져옴

            List resultlist = sqlite.CheckVisit(Latitude, Longitude, strLanguage);

            if (!resultlist.isEmpty()){
                pushAlarm.VisitMessages(getApplicationContext(), strLanguage, resultlist); //관광지 방문시 언어에 따른 알림 출력
            }
        }
    };
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
