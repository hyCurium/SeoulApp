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
import java.io.FileReader;
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
            createTableAndInsertData(); //DB 열기
            Log.d("DB : ","DB 열기 완료");
        }catch(Exception e)
        {
            Log.d("DB : ","DB 열기 실패");
        }
    }
    //여행지 이름 DB에서 불러오는 부분
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
            Log.d("Select", "Name 정상적으로 가져옴");
            return name;
        }
    }
    //방분 여부 체크 부분, MainActivity에 Broadcast에서 위도,경도 가져옴
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
                if (!temp_Tour.isEmpty()) {temp_Tour = tourID;}
            }
        }
        if (cnt>0){ //방문지역 하나라도 있으면 출력
            resultlist.add(cnt);
            name = SelectTourName(language, tourID); //여행지 이름 DB에서 불러오기
            if (name != null){ //name이 존재할때만 값 반환
                Log.d("Select", "result 반환");
                resultlist.add(name);
                return resultlist;
            }
        }
        Log.d("Select", "result 미존재");
        return null;
    }
    //VisitCode 업데이트 부분
    public void UpdateVisitCode(String TourID){
        try{
            String sql;
            long now = System.currentTimeMillis();
            Date date = new Date(now);

            //방문 코드 업데이트
            sql = "UPDATE TourInfo SET VistCode = 1 where TourID = " + TourID;
            DBinstance.execSQL(sql);
            Log.d("UpdateVisitCode : ","TourInfo VisitCode Update 성공");

            //방문한 장소 테이블에 추가
            sql = "INSERT INTO VisitedTour (TourID, VisitedDate) VALUES ("+ TourID + "," + date + ")";
            DBinstance.execSQL(sql);
            Log.d("UpdateVisitCode : ","VisitedTour Insert 성공");

        }catch(Exception e){
            Log.d("UpdateVisitCode : ","VisitCode Update 실패");
        }
    }

    //최초 생성 시 CSV에서 값 읽어와 데이터 입력
    private void FirstInsertCSV(){
        InsertCSV("TourInfo.csv","Tourinfo");
        InsertCSV("TourDetailInfo_KOR.csv","TourDetailInfo_KOR");
        InsertCSV("TourDetailInfo_ENG.csv","TourDetailInfo_ENG");
        InsertCSV("VisitedTour.csv","VisitedTour");
    }

    //CSV 읽어오는 부분
    private void InsertCSV(String csvFileName, String tableName){
        String sql = "";
        try{
            CSVReader reader = new CSVReader(new FileReader(csvFileName), ',');
            String []nextLine;
            int cnt;
            sql = "INSERT INTO (" + tableName + ") VALUES (";
            while ((nextLine = reader.readNext())!= null){
                cnt = 0; //항목마다 다를 수 있기 때문에 카운트해줌
                while (nextLine[cnt] != null){
                    cnt++;
                    sql = sql + nextLine[cnt] + ",";
                }
                sql = sql.substring(0,sql.length()-2) + ");";
                DBinstance.execSQL(sql);
            }
            Log.e("CSVFile", "Insert success");
        }catch(Exception e){
            Log.e("CSVFile", "Error");
        }

    }

    //테이블 생성 부분
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
            FirstInsertCSV(); //DB CSV 파일 읽어서 입력
        }catch(Exception e)
        {
            Log.d("DB : ","DB 생성 실패");
        }
    }
}
