package com.example.jungeb.seoulapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jungeb.seoulapp.WebRequest.RequestHttpURLConnection;
import com.example.jungeb.seoulapp.fragment.DetailFirstFragment;
import com.example.jungeb.seoulapp.fragment.DetailSecondFragment;
import com.example.jungeb.seoulapp.fragment.DetailThirdFragment;
import com.example.jungeb.seoulapp.fragment.TodayWeatherFragment;
import com.example.jungeb.seoulapp.fragment.TomorrowWeatherFragment;
import com.example.jungeb.seoulapp.fragment.TourFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;

public class DetailPageActivity extends AppCompatActivity implements OnMapReadyCallback{
    double lat;
    double lon;
    ImageButton imbDetailBack,detailbookmark;
    ViewPager vpDetailImages;
    PagerDetailAdapter pagerDetailAdapter;
    boolean isBookMark;
    int m_id;

    //정보 위젯들
    TextView tvDetailTitle,tvDetailContent,tvDetailAddress,tvDetailDate,tvDetailTime,tvDetailNumber;
    RatingBar ratingStar;

    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);


        String categorys[] = getResources().getStringArray(R.array.category_kor);

        Intent intent = getIntent();
        m_id = intent.getIntExtra("ID",0);
        String placeID = intent.getStringExtra("PlaceID");
        int category = intent.getIntExtra("Category",1) - 1;
        isBookMark = intent.getBooleanExtra("BookMakr",false);
        lat = intent.getDoubleExtra("Latitude",0);
        lon = intent.getDoubleExtra("Longitude",0);

        ((TextView)findViewById(R.id.tvDetailKind)).setText(categorys[category]);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.detailMap) ;
        mapFragment.getMapAsync(this);

        detailbookmark = (ImageButton)findViewById(R.id.detailbookmark);

        if (isBookMark)
        {
            detailbookmark.setImageResource(R.drawable.bookmark_activation);
        }

        detailbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng marker = new LatLng(lat,lon);
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions.position(marker);

        mMap.addMarker(makerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
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
    private class GooglePlaceRequest extends AsyncTask<Void,Void,String>
    {
        final String API = "AIzaSyBNcrLP0WNGsICB3aUy7Up9g5yC3f-XX38";
        private String REQUEST_GOOGLEPLACE = "https://maps.googleapis.com/maps/api/place/details/json";
        private URL url;
        private ContentValues values;

        GooglePlaceRequest(String _placeID)
        {
            values = new ContentValues();
            values.put("key" , API);
            values.put("placeid", _placeID);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //JSON 처리
            JSONObject json = null;
            try {
                json = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... strings) {
            String result;
            RequestHttpURLConnection request = new RequestHttpURLConnection();
            result = request.request(REQUEST_GOOGLEPLACE,values);
            return result;
        }
    }

}
