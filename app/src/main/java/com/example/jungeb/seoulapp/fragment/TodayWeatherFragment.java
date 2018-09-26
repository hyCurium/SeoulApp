package com.example.jungeb.seoulapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jungeb.seoulapp.R;
import com.example.jungeb.seoulapp.weather.WeatherTC;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class TodayWeatherFragment extends Fragment {
    ImageView weather_image; //날씨 이미지
    TextView weather_now; // 현재온도
    TextView weather_name; //현재날씨이름
    TextView weather_tmax; //최고기온
    TextView weather_tmin; //최저기온

    double lat; //위도
    double lon; //경도

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);

        //위도 경도를 받아와서 getWeather에 넣어주기


        weather_image = (ImageView)view.findViewById(R.id.weather_image);
        weather_now = (TextView) view.findViewById(R.id.weather_now);
        weather_name = (TextView) view.findViewById(R.id.weather_name);
        weather_tmax = (TextView) view.findViewById(R.id.weather_tmax);
        weather_tmin = (TextView) view.findViewById(R.id.weather_tmin);

        getWeather(37.3366916,126.7375282);
        return view;
    }
    private interface WeatherApiInterface{
        String BASEURL = "https://api2.sktelecom.com/";
        String APPKEY = "9f83e3e1-8412-4ee9-9857-97db3279aa66";

        //get 메소드를 통한 http rest api 통신
        @GET("weather/current/minutely")
        Call<WeatherTC> getMinutely(@Header("appKey") String appKey, @Query("version") int version,
                                    @Query("lat") double lat, @Query("lon") double lon);
    }
    private void getWeather(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(WeatherApiInterface.BASEURL)
                .build();
        WeatherApiInterface apiService = retrofit.create(WeatherApiInterface.class);
        Call< WeatherTC> call = apiService.getMinutely(WeatherApiInterface.APPKEY, 1, latitude, longitude);
        call.enqueue(new Callback< WeatherTC>() { //retrodit callback 구현시 비동기/동기 둘다가능
            @Override
            public void onResponse(@NonNull Call< WeatherTC> call, @NonNull Response< WeatherTC> response) {
                if (response.isSuccessful()) {
                    //날씨데이터를 받아옴
                    Log.d("dda","통신은 함");
                    WeatherTC object = response.body();
                    if (object != null) {
                        Log.d("dda","데이터가 널은아님");
                        //데이터가 null 이 아니라면 날씨 데이터를 텍스트뷰로 보여주기
                        weatherCode(object.getWeather().getMinutely().get(0).getSky().getCode());
                        int tc =(int)Double.parseDouble(object.getWeather().getMinutely().get(0).getTemperature().getTc());
                        int tmax = (int)Double.parseDouble(object.getWeather().getMinutely().get(0).getTemperature().getTmax());
                        int tmin = (int)Double.parseDouble(object.getWeather().getMinutely().get(0).getTemperature().getTmin());
                        weather_name.setText(object.getWeather().getMinutely().get(0).getSky().getName());
                        weather_now.setText(tc+"");
                        weather_tmax.setText(tmax+"");
                        weather_tmin.setText(tmin+"");
                    }else {
                        Log.d("dda","데이터가 널");
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call< WeatherTC> call, @NonNull Throwable t) {
            }
        });
    }
    public void weatherCode(String code){
        Log.d("ddd",code+"");
        switch (code){
            case "SKY_A01":
                weather_image.setImageResource(R.drawable.weather_sun);
                break;
            case "SKY_A02":
                weather_image.setImageResource(R.drawable.weather_cloud);
                break;
            case "SKY_A03":
                weather_image.setImageResource(R.drawable.weather_cloud_more);
                break;
            case "SKY_A04":
                weather_image.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_A05":
                weather_image.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_A06":
                weather_image.setImageResource(R.drawable.weather_snow_or_rain);
                break;
            case "SKY_A07":
                weather_image.setImageResource(R.drawable.weather_blur);
                break;
            case "SKY_A08":
                weather_image.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_A09":
                weather_image.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_A10":
                weather_image.setImageResource(R.drawable.weather_snow_or_rain);
                break;
            case "SKY_A11":
                weather_image.setImageResource(R.drawable.weather_blur);
                break;
            case "SKY_A12":
                weather_image.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_A13":
                weather_image.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_A14":
                weather_image.setImageResource(R.drawable.weather_snow_or_rain);
                break;
            default:
                break;


        }
    }
}
