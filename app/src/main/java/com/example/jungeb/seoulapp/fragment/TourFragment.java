package com.example.jungeb.seoulapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jungeb.seoulapp.R;
import com.example.jungeb.seoulapp.TravelActivity;

public class TourFragment extends Fragment implements View.OnClickListener{

    Button btnTravelMore;
    ImageButton imbFold;
    ViewPager vpWeather, vpTourInfo;
    TabLayout tabTourInfo;
    PagerTabAdapter pagerTabAdapter;
    PagerWeatherAdapter pagerWeatherAdapter;
    Context context = null;
    LinearLayout linearFoldTab, linearFoldTab2, linearUnFold;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour, container, false);

        context = container.getContext();


        //접기
        imbFold = (ImageButton) view.findViewById(R.id.imbFold);
        linearUnFold = (LinearLayout)view.findViewById(R.id.linearUnfold);
        linearFoldTab = (LinearLayout) view.findViewById(R.id.linearFoldTab);
        linearFoldTab2 = (LinearLayout) view.findViewById(R.id.linearFoldTab2);

        imbFold.setOnClickListener(foldListener);
        linearUnFold.setOnClickListener(foldListener);


        //스탬프 더보기 버튼
        btnTravelMore = (Button) view.findViewById(R.id.btnTravelMore);

        btnTravelMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TravelActivity.class);
                startActivity(i);
            }
        });


        //날씨 뷰페이저
        vpWeather = (ViewPager) view.findViewById(R.id.vpWeather);

        pagerWeatherAdapter = new PagerWeatherAdapter(getFragmentManager());
        vpWeather.setAdapter(pagerWeatherAdapter);
        vpWeather.setCurrentItem(0);


        //관광정보 탭 레이아웃
        tabTourInfo = (TabLayout) view.findViewById(R.id.tabTourInfo);
        vpTourInfo = (ViewPager) view.findViewById(R.id.vpTourInfo);

        pagerTabAdapter = new PagerTabAdapter(getFragmentManager());
        vpTourInfo.setAdapter(pagerTabAdapter);
        vpTourInfo.setCurrentItem(0);

        tabTourInfo.setupWithViewPager(vpTourInfo);

        TextView bookmark = new TextView(context);
        TextView culture = new TextView(context);
        TextView kpop = new TextView(context);
        TextView festival = new TextView(context);

        bookmark.setText("북마크");
        culture.setText("문화재");
        kpop.setText("한류");
        festival.setText("볼거리");

        bookmark.setTextSize(15);
        culture.setTextSize(15);
        kpop.setTextSize(15);
        festival.setTextSize(15);

        bookmark.setGravity(Gravity.CENTER);
        culture.setGravity(Gravity.CENTER);
        kpop.setGravity(Gravity.CENTER);
        festival.setGravity(Gravity.CENTER);

        bookmark.setTextColor(Color.parseColor("#4170f2"));
        culture.setTextColor(Color.parseColor("#929292"));
        kpop.setTextColor(Color.parseColor("#929292"));
        festival.setTextColor(Color.parseColor("#929292"));

        tabTourInfo.getTabAt(0).setCustomView(bookmark);
        tabTourInfo.getTabAt(1).setCustomView(culture);
        tabTourInfo.getTabAt(2).setCustomView(kpop);
        tabTourInfo.getTabAt(3).setCustomView(festival);

        tabTourInfo.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch(pos) {
                    case 0:
                        TextView tvBookmark0 = (TextView)tab.getCustomView();
                        tvBookmark0.setTextColor(Color.parseColor("#4170f2"));

                        TextView tvCulture0 = (TextView)tabTourInfo.getTabAt(1).getCustomView();
                        tvCulture0.setTextColor(Color.parseColor("#929292"));

                        TextView tvKpop0 = (TextView)tabTourInfo.getTabAt(2).getCustomView();
                        tvKpop0.setTextColor(Color.parseColor("#929292"));

                        TextView tvFestival0 = (TextView)tabTourInfo.getTabAt(3).getCustomView();
                        tvFestival0.setTextColor(Color.parseColor("#929292"));

                        break;
                    case 1:
                        TextView tvBookmark1 = (TextView)tab.getCustomView();
                        tvBookmark1.setTextColor(Color.parseColor("#4170f2"));

                        TextView tvCulture1 = (TextView)tabTourInfo.getTabAt(0).getCustomView();
                        tvCulture1.setTextColor(Color.parseColor("#929292"));

                        TextView tvKpop1 = (TextView)tabTourInfo.getTabAt(2).getCustomView();
                        tvKpop1.setTextColor(Color.parseColor("#929292"));

                        TextView tvFestival1 = (TextView)tabTourInfo.getTabAt(3).getCustomView();
                        tvFestival1.setTextColor(Color.parseColor("#929292"));

                        break;
                    case 2:
                        TextView tvBookmark2 = (TextView)tab.getCustomView();
                        tvBookmark2.setTextColor(Color.parseColor("#4170f2"));

                        TextView tvCulture2 = (TextView)tabTourInfo.getTabAt(0).getCustomView();
                        tvCulture2.setTextColor(Color.parseColor("#929292"));

                        TextView tvKpop2 = (TextView)tabTourInfo.getTabAt(1).getCustomView();
                        tvKpop2.setTextColor(Color.parseColor("#929292"));

                        TextView tvFestival2 = (TextView)tabTourInfo.getTabAt(3).getCustomView();
                        tvFestival2.setTextColor(Color.parseColor("#929292"));

                        break;
                    case 3:
                        TextView tvBookmark3 = (TextView)tab.getCustomView();
                        tvBookmark3.setTextColor(Color.parseColor("#4170f2"));

                        TextView tvCulture3 = (TextView)tabTourInfo.getTabAt(0).getCustomView();
                        tvCulture3.setTextColor(Color.parseColor("#929292"));

                        TextView tvKpop3 = (TextView)tabTourInfo.getTabAt(1).getCustomView();
                        tvKpop3.setTextColor(Color.parseColor("#929292"));

                        TextView tvFestival3 = (TextView)tabTourInfo.getTabAt(2).getCustomView();
                        tvFestival3.setTextColor(Color.parseColor("#929292"));

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;

    }

    @Override
    public void onClick(View v) {

    }

    //접기, 펼치기 리스너
    View.OnClickListener foldListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.imbFold:
                    linearFoldTab.setVisibility(linearFoldTab.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
                    linearFoldTab2.setVisibility(linearFoldTab2.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
                    break;
                case R.id.linearUnfold:
                    linearFoldTab.setVisibility(linearFoldTab.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
                    linearFoldTab2.setVisibility(linearFoldTab2.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
                    break;
            }
        }
    };

    //날씨 뷰페이저 어댑터
    private class PagerWeatherAdapter extends FragmentStatePagerAdapter
    {
        public PagerWeatherAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return new TodayWeatherFragment();
                case 1:
                    return new TomorrowWeatherFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }


    //탭 뷰페이저 어댑터
    private class PagerTabAdapter extends FragmentStatePagerAdapter
    {
        public PagerTabAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return new BookmarkTourFragment();
                case 1:
                    return new CultureTourFragment();
                case 2:
                    return new KpopTourFragment();
                case 3:
                    return new FestivalTourFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }

    
}
