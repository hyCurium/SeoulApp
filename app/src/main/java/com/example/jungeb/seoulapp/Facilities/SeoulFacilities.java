package com.example.jungeb.seoulapp.Facilities;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class SeoulFacilities {
    URL url=null;//url 주소로 프로토콜, 도메인, 포트, 경로 등을 알 수 있다.
    HttpURLConnection httpURLConnection=null;
    BufferedReader bufferedReader=null;//문자입력 스트림으로부터 문자를 읽어 들이거나 문자
                                        //출력 스트림으로 문자를 보낼 때 버퍼링을 함으로써 문자, 문자배열,
                                        //문자열, 라인 등을 보다 효율적으로 처리할 수 있도록 해준다.
    HashMap<String,String> location;
    String params="";


    String data="";
    static final String REQUEST_SEOULTOILET_URL="http://openapi.seoul.go.kr:8088/4d5042734f64616e3235715a787163/json/";

    public SeoulFacilities(HashMap<String,String> location){
        this.location=location;


    }

    public SeoulFacilities(){

    }




}
