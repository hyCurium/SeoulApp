package com.example.jungeb.seoulapp.sqliteC;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jungeb.seoulapp.R;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

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
    public void CheckVisit(double Latitude, double Longitude){
        String sql = "Select Latitude, Longitude, TourID From TourInfo";
        Cursor resultset = DBinstance.rawQuery(sql, null);
        int count = resultset.getCount();
        Log.d("TourInfo","TourInfo Selete");
        double tourlat, tourlong;
        String tourID;
        if (count >= 1) { //근처 위치 존재 시
            for (int i = 0; i < count; i++) {
                resultset.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                tourlat = resultset.getDouble(0);
                tourlong = resultset.getDouble(1);
                tourID = resultset.getString(2);
                if (Math.sqrt(Math.pow(tourlat-Latitude,2)+Math.pow(tourlong-Longitude,2)) > 0.001){ //약 100m
                    UpdateVisitCode(tourID);
                }
            }
        }
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
            Log.d("UPDATEVisitCode : ","Update 실패");
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
