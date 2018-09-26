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
import com.example.jungeb.seoulapp.weather.WeatherData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public class TomorrowWeatherFragment extends Fragment {
    ImageView weather_image2; //내일 이미지
    TextView weather_name2; //내일 날씨명
    TextView weather_tmax2; //내일 최고기온
    TextView weather_tmin2; //내일 최저기온

    ImageView weather_image3; //모레 이미지
    TextView weather_name3; //모레 날씨명
    TextView weather_tmax3; //모레 최고기온
    TextView weather_tmin3; //모레 최저기온

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tomorrow_weather, container, false);

        weather_image2 = (ImageView) view.findViewById(R.id.weather_image2);
        weather_name2 = (TextView) view.findViewById(R.id.weather_name2);
        weather_tmax2 = (TextView) view.findViewById(R.id.weather_tmax2);
        weather_tmin2 = (TextView) view.findViewById(R.id.weather_tmin2);

        weather_image3 = (ImageView) view.findViewById(R.id.weather_image3);
        weather_name3 = (TextView) view.findViewById(R.id.weather_name3);
        weather_tmax3 = (TextView) view.findViewById(R.id.weather_tmax3);
        weather_tmin3 = (TextView) view.findViewById(R.id.weather_tmin3);


        getWeather(37.3366916,126.7375282);

        return view;
    }

    private interface WeatherApiInterface{
        String BASEURL = "https://api2.sktelecom.com/";
        String APPKEY = "9f83e3e1-8412-4ee9-9857-97db3279aa66";

        //get 메소드를 통한 http rest api 통신
        @GET("weather/summary")
        Call<WeatherData> getMinutely(@Header("appKey") String appKey, @Query("version") int version,
                                      @Query("lat") double lat, @Query("lon") double lon);
    }
    private void getWeather(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(WeatherApiInterface.BASEURL)
                .build();
        WeatherApiInterface apiService = retrofit.create(WeatherApiInterface.class);
        Call<WeatherData> call = apiService.getMinutely(WeatherApiInterface.APPKEY, 1, latitude, longitude);
        call.enqueue(new Callback<WeatherData>() { //retrodit callback 구현시 비동기/동기 둘다가능
            @Override
            public void onResponse(@NonNull Call<WeatherData> call, @NonNull Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    //날씨데이터를 받아옴
                    Log.d("ddd","통신은 함");
                    WeatherData object = response.body();
                    if (object != null) {
                        Log.d("ddd","데이터가 널은아님");
                        //데이터가 null 이 아니라면 날씨 데이터를 텍스트뷰로 보여주기

                        weatherCode2(object.getWeather().getSummary().get(0).getTomorrow().getSky3().getCode3());

                        weatherCode3(object.getWeather().getSummary().get(0).getDayAftertomorrow().getSky4().getCode4());

                        int tmax3 = (int)Double.parseDouble(object.getWeather().getSummary().get(0).getTomorrow().getTemperature3().getTmax3());
                        int tmin3 = (int)Double.parseDouble(object.getWeather().getSummary().get(0).getTomorrow().getTemperature3().getTmin3());

                        weather_name2.setText(object.getWeather().getSummary().get(0).getTomorrow().getSky3().getName3().toString());
                        weather_tmax2.setText(tmax3+"");
                        weather_tmin2.setText(tmin3+"");

                        int tmax4 = (int)Double.parseDouble(object.getWeather().getSummary().get(0).getDayAftertomorrow().getTemperature4().getTmax4());
                        int tmin4 = (int)Double.parseDouble(object.getWeather().getSummary().get(0).getDayAftertomorrow().getTemperature4().getTmin4());

                        weather_name3.setText(object.getWeather().getSummary().get(0).getDayAftertomorrow().getSky4().getName4().toString());
                        weather_tmax3.setText(tmax4+"");
                        weather_tmin3.setText(tmin4+"");
                    }else {
                        Log.d("ddd","데이터가 널");
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherData> call, @NonNull Throwable t) {
            }
        });
    }
    public void weatherCode2(String code){
        Log.d("ddd",code+"");
        switch (code){
            case "SKY_M01":
                weather_image2.setImageResource(R.drawable.weather_sun);
                break;
            case "SKY_M02":
                weather_image2.setImageResource(R.drawable.weather_cloud);
                break;
            case "SKY_M03":
                weather_image2.setImageResource(R.drawable.weather_cloud_more);
                break;
            case "SKY_M04":
                weather_image2.setImageResource(R.drawable.weather_blur);
                break;
            case "SKY_M05":
                weather_image2.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_M06":
                weather_image2.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_M07":
                weather_image2.setImageResource(R.drawable.weather_snow_or_rain);
                break;
            default:
                break;
        }
    }
    public void weatherCode3(String code){
        Log.d("ddd",code+"");
        switch (code){
            case "SKY_M01":
                weather_image3.setImageResource(R.drawable.weather_sun);

                break;
            case "SKY_M02":
                weather_image3.setImageResource(R.drawable.weather_cloud);
                break;
            case "SKY_M03":
                weather_image3.setImageResource(R.drawable.weather_cloud_more);
                break;
            case "SKY_M04":
                weather_image3.setImageResource(R.drawable.weather_blur);
                break;
            case "SKY_M05":
                weather_image3.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_M06":
                weather_image3.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_M07":
                weather_image3.setImageResource(R.drawable.weather_snow_or_rain);
                break;
            default:
                break;
        }
    }

}
