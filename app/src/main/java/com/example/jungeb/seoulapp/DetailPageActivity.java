package com.example.jungeb.seoulapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.jungeb.seoulapp.fragment.DetailFirstFragment;
import com.example.jungeb.seoulapp.fragment.DetailSecondFragment;
import com.example.jungeb.seoulapp.fragment.DetailThirdFragment;
import com.example.jungeb.seoulapp.fragment.TodayWeatherFragment;
import com.example.jungeb.seoulapp.fragment.TomorrowWeatherFragment;
import com.example.jungeb.seoulapp.fragment.TourFragment;

public class DetailPageActivity extends AppCompatActivity {

    ImageButton imbDetailBack;
    ViewPager vpDetailImages;
    PagerDetailAdapter pagerDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        //뒤로가기 버튼 눌렀을 때
        imbDetailBack = (ImageButton) findViewById(R.id.imbDetailBack);

        imbDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        //사진 뷰페이저
        vpDetailImages = (ViewPager) findViewById(R.id.vpDetailImages);

        pagerDetailAdapter = new PagerDetailAdapter(getSupportFragmentManager());
        vpDetailImages.setAdapter(pagerDetailAdapter);
        vpDetailImages.setCurrentItem(0);

    }

    private class PagerDetailAdapter extends FragmentStatePagerAdapter
    {
        public PagerDetailAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return new DetailFirstFragment();
                case 1:
                    return new DetailSecondFragment();
                case 2:
                    return new DetailThirdFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }
}
