package com.example.jungeb.seoulapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jungeb.seoulapp.Facilities.SeoulFacilities;
import com.example.jungeb.seoulapp.Facilities.SeoulFacilitiesAsyncTask;
import com.example.jungeb.seoulapp.Facilities.SeoulFacilitiesLocation;
import com.example.jungeb.seoulapp.R;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class FacilitiesFragment extends Fragment {
    LinearLayout linMapView;
    TMapView tMapView;
    double myLatitude = 0.0;
    double myLongitude = 0.0;
    LocationManager locationManager;

    ImageButton ibtnSubwayIcon;
    ImageButton ibtnBusIcon;
    ImageButton ibtnTaxiIcon;
    ImageButton ibtnMedicineIcon;
    ImageButton ibtnWCICON;

    TextView tvSubwayText;
    TextView tvBusText;
    TextView tvTaxiText;
    TextView tvMedicineText;
    TextView tvWCText;

    SeoulFacilitiesAsyncTask seoulFacilitiesAsyncTask = null;
    HashMap<String, String> seoulFac = null;
    HashMap<String, String> seoulFac2 = null;
    JSONObject jsonObjectLocation;

    ArrayList<JSONArray> allLocation = null;

    //SeoulFacilitiesLocation[] toiletLocations;
    ArrayList<SeoulFacilitiesLocation> seoulFacilitiesLocations;

    ArrayList<TMapMarkerItem> tMapMarkerItems;
    //TMapMarkerItem[] tMapMarkerItemsCCTV;
    TMapPoint tMapPointCCTV;
    Bitmap bitmapCCTV;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_facilities, container, false);

        linMapView = (LinearLayout) view.findViewById(R.id.linMapView);
        ibtnSubwayIcon = view.findViewById(R.id.ibtnSubwayIcon);
        ibtnBusIcon = view.findViewById(R.id.ibtnBusIcon);
        ibtnTaxiIcon = view.findViewById(R.id.ibtnTaxiIcon);
        ibtnMedicineIcon = view.findViewById(R.id.ibtnMedicineIcon);
        ibtnWCICON = view.findViewById(R.id.ibtnWCIcon);
        tvSubwayText = view.findViewById(R.id.tvSubwayText);
        tvBusText = view.findViewById(R.id.tvBusText);
        tvTaxiText = view.findViewById(R.id.tvTaxiText);
        tvMedicineText = view.findViewById(R.id.tvMedicineText);
        tvWCText = view.findViewById(R.id.tvWCIcon);

        tMapView = new TMapView(getContext());
        tMapView.setSKTMapApiKey("8c01e13b-978f-41bc-8629-b223245f9203");
        linMapView.addView(tMapView);

        tMapMarkerItems = new ArrayList<TMapMarkerItem>();
        seoulFacilitiesLocations = new ArrayList<SeoulFacilitiesLocation>();

        //현재위치를 받아온다 GPS와 NETWORK둘다 사용
//        locationManager = (LocationManager) view.getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return view;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
        Toast.makeText(getContext(), "현재위치 트래킹 시작", Toast.LENGTH_SHORT).show();

        seoulFac = new HashMap<String, String>();
        seoulFac.put("SERVICE", "MgisToilet");
        seoulFac.put("START_INDEX", "1");
        seoulFac.put("END_INDEX", "1000");
        connectCheck(seoulFac, "MgisToilet","row");

//        seoulFac2=new HashMap<String,String>();
//        seoulFac2.put("SERVICE","MgisToilet");
//        seoulFac2.put("START_INDEX","1001");
//        seoulFac2.put("END_INDEX","2000");
//        connectCheck(seoulFac2);

        ibtnWCICON.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locationManager.removeUpdates(mLocationListener);
                return false;
            }
        });


        ibtnSubwayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_activation);
                ibtnBusIcon.setImageResource(R.drawable.bus_disable);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_disable);
                ibtnWCICON.setImageResource(R.drawable.wc_disable);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_disable);
                tvSubwayText.setTextColor(Color.parseColor("#5d38db"));
                tvBusText.setTextColor(Color.parseColor("#929292"));
                tvTaxiText.setTextColor(Color.parseColor("#929292"));
                tvMedicineText.setTextColor(Color.parseColor("#929292"));
                tvWCText.setTextColor(Color.parseColor("#929292"));
            }
        });


        ibtnBusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_disable);
                ibtnBusIcon.setImageResource(R.drawable.bus_activation);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_disable);
                ibtnWCICON.setImageResource(R.drawable.wc_disable);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_disable);
                tvSubwayText.setTextColor(Color.parseColor("#929292"));
                tvBusText.setTextColor(Color.parseColor("#5d38db"));
                tvTaxiText.setTextColor(Color.parseColor("#929292"));
                tvWCText.setTextColor(Color.parseColor("#929292"));
                tvMedicineText.setTextColor(Color.parseColor("#929292"));

                HashMap<String, String> newItem = new HashMap<String, String>();
                newItem.put("SERVICE", "busStopLocationXyInfo");
                newItem.put("START_INDEX", "1");
                newItem.put("END_INDEX", "1000");
                seoulFacilitiesLocations.clear();
                tMapView.removeAllMarkerItem();

                connectCheck(newItem, newItem.get("SERVICE"),"RESULT");


            }
        });

        ibtnTaxiIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_disable);
                ibtnBusIcon.setImageResource(R.drawable.bus_disable);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_activation);
                ibtnWCICON.setImageResource(R.drawable.wc_disable);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_disable);
                tvSubwayText.setTextColor(Color.parseColor("#929292"));
                tvBusText.setTextColor(Color.parseColor("#929292"));
                tvTaxiText.setTextColor(Color.parseColor("#5d38db"));
                tvWCText.setTextColor(Color.parseColor("#929292"));
                tvMedicineText.setTextColor(Color.parseColor("#929292"));

                HashMap<String, String> newItem = new HashMap<String, String>();
                newItem.put("SERVICE", "Mgistaxistop");
                newItem.put("START_INDEX", "1");
                newItem.put("END_INDEX", "1000");
                seoulFacilitiesLocations.clear();
                tMapView.removeAllMarkerItem();

                connectCheck(newItem, newItem.get("SERVICE"),"row");
            }
        });

        ibtnWCICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_disable);
                ibtnBusIcon.setImageResource(R.drawable.bus_disable);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_disable);
                ibtnWCICON.setImageResource(R.drawable.wc_activation);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_disable);
                tvSubwayText.setTextColor(Color.parseColor("#929292"));
                tvBusText.setTextColor(Color.parseColor("#929292"));
                tvTaxiText.setTextColor(Color.parseColor("#929292"));
                tvWCText.setTextColor(Color.parseColor("#5d38db"));
                tvMedicineText.setTextColor(Color.parseColor("#929292"));
                HashMap<String, String> newItem = new HashMap<String, String>();
                newItem.put("SERVICE", "MgisToilet");
                newItem.put("START_INDEX", "1");
                newItem.put("END_INDEX", "1000");
                seoulFacilitiesLocations.clear();
                tMapView.removeAllMarkerItem();

                connectCheck(newItem, newItem.get("SERVICE"),"RESULT");
            }
        });

        ibtnMedicineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_disable);
                ibtnBusIcon.setImageResource(R.drawable.bus_disable);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_disable);
                ibtnWCICON.setImageResource(R.drawable.wc_disable);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_activation);
                tvSubwayText.setTextColor(Color.parseColor("#929292"));
                tvBusText.setTextColor(Color.parseColor("#929292"));
                tvTaxiText.setTextColor(Color.parseColor("#929292"));
                tvWCText.setTextColor(Color.parseColor("#929292"));
                tvMedicineText.setTextColor(Color.parseColor("#5d38db"));
                HashMap<String, String> newItem = new HashMap<String, String>();
                newItem.put("SERVICE", "parmacyBizInfo");
                newItem.put("START_INDEX", "1");
                newItem.put("END_INDEX", "1000");
                seoulFacilitiesLocations.clear();
                tMapView.removeAllMarkerItem();

                connectCheck(newItem, newItem.get("SERVICE"),"RESULT");

            }
        });


        return view;

    }

    public void setLocation(ArrayList<JSONArray> location,String dataSet) {
        Log.i("마이체크", "" + location);
        allLocation = location;
        seoulFacilitiesLocations=new ArrayList<SeoulFacilitiesLocation>();
        String lonType="COT_COORD_X";
        String latType="COT_COORD_Y";

        if(dataSet.equals("busStopLocationXyInfo")){
            lonType="XCODE";
            latType="YCODE";
        }else if(dataSet.equals("MgisToilet")){
            lonType="COT_COORD_X";
            latType="COT_COORD_Y";
        }else if(dataSet.equals("Mgistaxistop")){
            lonType="COT_COORD_X";
            latType="COT_COORD_Y";
        }else if(dataSet.equals("parmacyBizInfo")){
            lonType="XCODE";
            latType="YCODE";
        }

        for (int cnt = 0; cnt < allLocation.size(); cnt++) {
            JSONArray locationTab1 = allLocation.get(cnt);
            Log.i("잘왔닝", "" + locationTab1);

            Log.i("분리각", "" + locationTab1.length());
            try {
                for (int setCnt = 0; setCnt < locationTab1.length(); setCnt++) {

                    JSONObject temp = (JSONObject) locationTab1.get(setCnt);
                    SeoulFacilitiesLocation seoulFacilitiesLocationItem=new SeoulFacilitiesLocation();
                    seoulFacilitiesLocationItem.setLon(Double.parseDouble(temp.get("" +lonType) + ""));
                    seoulFacilitiesLocationItem.setLat(Double.parseDouble(temp.get(""+latType)+""));

                    seoulFacilitiesLocations.add(seoulFacilitiesLocationItem);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("전체는?",""+seoulFacilitiesLocations.size());

        markPoint();


    }

//    public void setLocation(JSONObject location){
//        jsonObjectLocation=location;
//        Log.i("myRest1",""+jsonObjectLocation);
//        JSONObject temp1;
//        JSONArray temp2;
//        try {
//
////            ArrayList<SeoulFacilitiesLocation> seoulFacilitiesLocations;
////
////            ArrayList<TMapMarkerItem> tMapMarkerItems;
//            Log.i("myRest2",""+jsonObjectLocation.get("MgisToilet"));
//            temp1=jsonObjectLocation.getJSONObject("MgisToilet");
//            Log.i("myRest3",""+temp1.length());
//            Log.i("myRest4",""+temp1.get("row"));
//            temp2=temp1.getJSONArray("row");
//            Log.i("myRest5",""+temp2);
//            Log.i("어레이개수",""+temp2.length());
//            //toiletLocations=new SeoulFacilitiesLocation[temp2.length()];
//
//
//
//            for(int i=0;i<temp2.length();i++){
//                JSONObject jsonObject= (JSONObject) temp2.get(i);
//                //toiletLocations[i]=new SeoulFacilitiesLocation();
//
//                SeoulFacilitiesLocation seoulFacilitiesLocationItem=new SeoulFacilitiesLocation();
//                seoulFacilitiesLocationItem.setLon(Double.parseDouble(jsonObject.get("COT_COORD_X")+""));
//                seoulFacilitiesLocationItem.setLon(Double.parseDouble(jsonObject.get("COT_COORD_Y")+""));
//
//                //toiletLocations[i].setLat(Double.parseDouble(jsonObject.get("COT_COORD_Y")+""));
//                //toiletLocations[i].setLon(Double.parseDouble(jsonObject.get("COT_COORD_X")+""));
//                seoulFacilitiesLocations.add(seoulFacilitiesLocationItem);
//                Log.i("숫자샌다",""+i);
//
//
//            }
//            //Log.i("다했냐",""+toiletLocations[temp2.length()-1].getLat());
//            Log.i("다했냐",""+seoulFacilitiesLocations.get(seoulFacilitiesLocations.size()-1));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
////        try {
////            JSONObject temp1=locational.length();
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//
//        markingCCTV();
//
//    }

    //현재위치로 가는 메소드
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("locationListener", "onLocationChanged, Location : " + location);
            double longitude = location.getLongitude();//경도
            double latitude = location.getLatitude();//위도
            double altitude = location.getAltitude();//고도
            float accuracy = location.getAccuracy();//정확도
            String provider = location.getProvider();//위치제공자

            myLatitude = latitude;
            myLongitude = longitude;


            tMapView.setLocationPoint(longitude, latitude);
            tMapView.setCenterPoint(longitude, latitude);
            //GPS위치제공자에 의한 위치변화는 오차범위가 좁다. Network위치 제공자에 의한 위치변화는 GPS에 비해 정확도가 떨어진다.

            //tv.setText("위치정보 : "+ provider+"\n위도 : "+longitude+"\n경도 : "+latitude+"\n고도 : "+altitude+"\n정확도 : "+accuracy );
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("locationListener", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("locationListener", "onProviderEnabled, provider:" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("locationListener", "onProviderDisabled, provider:" + provider);
        }
    };

    public void connectCheck(HashMap<String, String> location, String where,String result) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if ((networkInfo != null) && networkInfo.isConnected()) {
            seoulFacilitiesAsyncTask = new SeoulFacilitiesAsyncTask(location, this);
            seoulFacilitiesAsyncTask.execute("" + where,result);


        } else {
            Toast.makeText(getContext(), "인터넷 연결상태를 확인해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void markPoint(){
        int tCount = 0;
        int tSize = seoulFacilitiesLocations.size();
        bitmapCCTV = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.bus25);

        for(tCount=0;tCount<tSize;tCount++){
            TMapMarkerItem tempMarker = new TMapMarkerItem();
            //TMapPoint tMapPoint=new TMapPoint();
            tempMarker.setTMapPoint(new TMapPoint(seoulFacilitiesLocations.get(tCount).getLat(),seoulFacilitiesLocations.get(tCount).getLon()));
            tempMarker.setIcon(bitmapCCTV);
            tempMarker.setPosition(0.5f, 1.0f);
            tMapView.addMarkerItem(tCount+"",tempMarker);
            Log.i("거리거리",""+seoulFacilitiesLocations.get(tCount).getLat());
            Log.i("찍혔냐","찍?"+tCount);

        }

    }

//    public void markingCCTV() {
//        int tCount = 0;
//        //int tSize=toiletLocations.length-1;
//        int tSize = seoulFacilitiesLocations.size() - 1;
//        //tMapMarkerItemsCCTV=new TMapMarkerItem[tSize];
//
//        bitmapCCTV = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.bus25);
//        for (tCount = 0; tCount < tSize; tCount++) {
//            TMapMarkerItem tempMarker = new TMapMarkerItem();
//            //tMapPointCCTV=new TMapPoint(toiletLocations[tCount].getLat(),toiletLocations[tCount].getLon());
//            tMapPointCCTV = new TMapPoint(seoulFacilitiesLocations.get(tCount).getLat(), seoulFacilitiesLocations.get(tCount).getLon());
//            tempMarker.setIcon(bitmapCCTV);
//            tempMarker.setPosition(0.5f, 1.0f);
//            tempMarker.setTMapPoint(tMapPointCCTV);
//            tMapMarkerItems.add(tCount, tempMarker);
//            //tMapMarkerItemsCCTV[tCount]=new TMapMarkerItem();
//
//            //tMapMarkerItemsCCTV[tCount].setIcon(bitmapCCTV);
//            //tMapMarkerItemsCCTV[tCount].setPosition(0.5f,1.0f);
//            //tMapMarkerItemsCCTV[tCount].setTMapPoint(tMapPointCCTV);
//
//            TMapMarkerItem newTemp = new TMapMarkerItem();
//            newTemp = tMapMarkerItems.get(tCount);
//            tMapView.addMarkerItem(tCount + "tCount", newTemp);
//            Log.i("잘찍었나봐", "" + tCount);
//            Log.i("살펴볼께", "lat : " + tMapPointCCTV.getLatitude() + " lon : " + tMapPointCCTV.getLongitude());
//            tMapPointCCTV = null;
//        }
//    }
}
