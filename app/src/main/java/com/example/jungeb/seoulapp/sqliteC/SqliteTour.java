package com.example.jungeb.seoulapp.sqliteC;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jungeb.seoulapp.PushAlarm.PushAlarm;
import com.example.jungeb.seoulapp.R;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqliteTour {
    SQLiteDatabase DBinstance;
    String m_dbName;
    public SqliteTour(Activity _activity, String _dbName)
    {
        m_dbName = _dbName;
        try {
            DBinstance = _activity.openOrCreateDatabase(m_dbName, Context.MODE_PRIVATE, null);
            createTableAndInsertData();
            Log.d("DB : ","DB 열기 완료");
        }catch(Exception e)
        {
            Log.d("DB : ","DB 열기 실패");
        }
    }
    //visit code 갱신 쿼리
    private String SelectTourName(String language, String tourID){
        String tourName = "";
        String sql, name;
        if (language == "ko"){ //시스템 언어 한글이면 DetailInfo_kor에서 이름 값 가져옴
            sql = "Select NAME From TourInfo, TourDetailInfo_KOR Where TourInfo.TourID = TourDetailInfo_KOR.TourID";
        }else{ //그외 언어면 TourDetailInfo_ENG
            sql = "Select NAME From TourInfo, TourDetailInfo_ENG Where TourInfo.TourID = TourDetailInfo_ENG.TourID";
        }
        Cursor resultset = DBinstance.rawQuery(sql, null);
        int count = resultset.getCount();
        if (count == 0){
            Log.e("Select", "tourID에 Name이 DetailInfo에 없음");
            return null;
        }else {
            resultset.moveToNext();
            name = resultset.getString(0);
            return name;
        }
    }
    public List CheckVisit(double Latitude, double Longitude, String language){
        String sql = "Select Latitude, Longitude, TourID From TourInfo";
        Cursor resultset = DBinstance.rawQuery(sql, null);
        int count = resultset.getCount();

        Log.d("Select","TourInfo Selete");

        int cnt = 0;
        String temp_Tour = "";
        double tourlat, tourlong;
        String tourID = "", name = "";
        List resultlist = new ArrayList();

        for (int i = 0; i < count; i++) {
            resultset.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
            tourlat = resultset.getDouble(0);
            tourlong = resultset.getDouble(1);
            tourID = resultset.getString(2);
            if (Math.sqrt(Math.pow(tourlat-Latitude,2)+Math.pow(tourlong-Longitude,2)) > 0.001){ //약 100m
                UpdateVisitCode(tourID); //방문시 DB에 업데이트
                cnt++;
                temp_Tour = tourID;
            }
        }
        if (cnt>0){ //방문지역 하나라도 있으면 출력
            resultlist.add(cnt);
            name = SelectTourName(language, tourID);
            if (name != null){ //name이 존재할때만 값 반환
                Log.d("Select", "result 반환");
                resultlist.add(name);
                return resultlist;
            }
        }
        Log.d("Select", "result 미존재");
        return null;
    }
    public void UpdateVisitCode(String TourID){
        try{
            String sql;
            long now = System.currentTimeMillis();
            Date date = new Date(now);

            sql = "UPDATE TourInfo SET VistCode = 1 where TourID = " + TourID;
            DBinstance.execSQL(sql);
            Log.d("UPDATE : ","TourInfo VisitCode Update 성공");

            sql = "INSERT INTO VisitedTour (TourID, VisitedDate) VALUES ("+ TourID + "," + date + ")";
            Log.d("UPDATE : ","VisitedTour Insert 성공");

        }catch(Exception e){
            Log.d("UPDATE : ","VisitCode Update 실패");
        }
    }
    private void InsertCSV(){

    }
    private void createTableAndInsertData()
    {
        try {
            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS TourInfo (" +
                    "TourID INTEGER Primary Key AUTOINCREMENT, " +
                    "Latitude double, Longitude double, " +
                    "PhoneNumber phoneNum, " +
                    "VisitCode Boolean DEFAULT 0, " +
                    "TourCategori INTEGER, " +
                    "GooglePlaceID String, " +
                    "URL String, " +
                    "BookMark Boolean DEFAULT 0);");

            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS TourDetailInfo_KOR(" +
                    "TourID INTEGER," +
                    "NAME String," +
                    "DetailInfo String," +
                    "CONSTRAINT TourID_fk FOREIGN KEY(TourID)" +
                    "REFERENCES TourInfo(TourID));");

            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS TourDetailInfo_ENG(" +
                    "TourID INTEGER," +
                    "NAME String," +
                    "DetailInfo String," +
                    "CONSTRAINT TourID_fk FOREIGN KEY(TourID)" +
                    "REFERENCES TourInfo(TourID));");

            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS VisitedTour(" +
                    "TourID INTEGER," +
                    "VisitedDate Date," +
                    "CONSTRAINT TourID_fk FOREIGN KEY(TourID)" +
                    "REFERENCES TourInfo(TourID)" +
                    ");");
            Log.d("DB : ","DB 생성 완료");
        }catch(Exception e)
        {
            Log.d("DB : ","DB 생성 실패");
        }
    }
}
