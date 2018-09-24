package com.example.jungeb.seoulapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailPageActivity extends AppCompatActivity implements OnMapReadyCallback{
    double lat;
    double lon;
    ImageButton imbDetailBack,detailbookmark;
    ViewPager vpDetailImages;
    PagerDetailAdapter pagerDetailAdapter;
    boolean isBookMark;
    int m_id;
    ImageView ivDetailFirst, ivDetailSecond, ivDetailThird;


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

        ratingStar = findViewById(R.id.ratingStar); //별점
        tvDetailNumber = findViewById(R.id.tvDetailNumber); //전화번호
        tvDetailAddress = findViewById(R.id.tvDetailAddress); //주소
        tvDetailDate = findViewById(R.id.tvDetailDate); //운영 시간

        ivDetailFirst = findViewById(R.id.ivDetailFirst); //View pager 1번째 fragment
        ivDetailSecond = findViewById(R.id.ivDetailSecond); //2번째
        ivDetailThird = findViewById(R.id.ivDetailThird); //3번쨰
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

        final String API = "AIzaSyBNcrLP0WNGsICB3aUy7Up9g5yC3f-XX38"; //웹 테스트용 키 : AIzaSyBlzb9XBS0o-AshssTlleIvaBnKXj0pr8s
        private String REQUEST_GOOGLEPLACE = "https://maps.googleapis.com/maps/api/place/details/json";
        private URL url;
        private ContentValues values;

        GooglePlaceRequest(String _placeID) {
            values = new ContentValues();
            values.put("key", API);
            values.put("placeid", _placeID);
            //fields=address_component,opening_hours,geometry
        }
        private Bitmap getPhoto(String photo_reference){ //사진 가져오기 함수
            HttpURLConnection urlConn = null;
            String _url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
            _url = _url + photo_reference + "&key=" + API;
            try{
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                // [2-1]. urlConn 설정.
                urlConn.setRequestMethod("GET"); // URL 요청에 대한 메소드 설정 : POST.
                urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.

                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return null;

                // [2-4]. 읽어온 결과물 리턴.
                InputStream inputStream = urlConn.getInputStream();
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;
            } catch (MalformedURLException e) { // for URL.
                e.printStackTrace();
            } catch (IOException e) { // for openConnection().
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //JSON 처리
            JSONObject json = null;
            try {
                json = new JSONObject(s);
                //가져오는 키 이름, 옆의 0d은 처리한 데이터
                //result - rating 0 , formatted_address 0, formatted_phone_number 0
                //result - reviews 0 - [0~4] author_name, rating, text, time
                //result - opening_hours - weekday_text
                //result - photo 0 - [0~5] width, height, photo_reference
                //JSONParsing -> JSONObject 저장 -> JSONArray에 항목별로 저장
                //JSONObject {} key,value

                JSONObject result = json.getJSONObject("result"); //결과 가져오기

                String address = result.get("formatted_address").toString(); //주소 가져오기
                tvDetailAddress.setText(address);

                String phone_number = json.get("formatted_phone_number").toString(); //전화번호
                tvDetailNumber.setText(phone_number);

                Float rating = Float.parseFloat(json.get("rating").toString()); //별점
                ratingStar.setRating(rating);

                //JSONArray review_array = result.getJSONArray("reviews"); //리뷰 가져오기

                JSONArray photo_array = result.getJSONArray("photos"); //사진 가져오기
                JSONObject photo_Object;
                String photo_reference;
                int photo_cnt = 3;
                for (int i = 0; i< photo_cnt;i++){
                    photo_Object = photo_array.getJSONObject(i); //JSON array에서 JSON Object가져옴
                    photo_reference = photo_Object.get("photo_reference").toString(); //거기서 photo_reference 가져옴
                    if (i == 0){ //사진 가져오기
                        ivDetailFirst.setImageBitmap(getPhoto(photo_reference));
                    }else if (i == 1){
                        ivDetailSecond.setImageBitmap(getPhoto(photo_reference));
                    }else{
                        ivDetailThird.setImageBitmap(getPhoto(photo_reference));
                    }
                }

                JSONObject opening_Object = result.getJSONObject("opening_hours"); //여는 시간 가져오기
                JSONArray weekday_Array = result.getJSONArray("weekday_text"); //여는 요일과 시간 0~6 0 = 일요일 ~ 6 = 토요일
                int weekday_cnt = 7;
                String weekday_str = "";
                for (int i=0;i<weekday_cnt;i++){
                    weekday_str = weekday_str + weekday_Array.get(0).toString();
                    if (i < weekday_cnt - 1) {weekday_str += "\n";}
                }

                tvDetailDate.setText(weekday_str);
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
