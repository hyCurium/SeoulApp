package com.example.jungeb.seoulapp.sqliteC;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SqliteTour {
    SQLiteDatabase DBinstance;
    String m_dbName;
    public SqliteTour(Activity _activity, String _dbName)
    {
        m_dbName = _dbName;
        try {
            DBinstance = _activity.openOrCreateDatabase(m_dbName, Context.MODE_PRIVATE, null);
            createTableAndInsertData();
        }catch(Exception e)
        {

        }
    }

    private void createTableAndInsertData()
    {
        try {
            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS TourInfo (" +
                    "TourID INTEGER Primary Key AUTOINCREMENT, " +
                    "Latitude double, Longitude double, " +
                    "PhoneNumber phoneNum, " +
                    "VisitCode Boolean DEFAULT (FALSE) , " +
                    "TourCategori INTEGER, " +
                    "GooglePlaceID String, " +
                    "URL String, " +
                    "BookMark Boolean DEFAULT (FALSE))");
            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS TourDetailInfo_KOR(" +
                    "TourID INTEGER," +
                    "NAME String," +
                    "CONSTRAINT TourID_fk FOREIGN KEY(TourID)" +
                    "REFERENCES TourInfo(TourID));");
            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS TourDetailInfo_ENG(" +
                    "TourID INTEGER," +
                    "NAME String," +
                    "CONSTRAINT TourID_fk FOREIGN KEY(TourID)" +
                    "REFERENCES TourInfo(TourID));");
            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS VisitedTour(" +
                    "TourID INTEGER," +
                    "VisitedDate Date," +
                    "CONSTRAINT TourID_fk FOREIGN KEY(TourID)" +
                    "REFERENCES TourInfo(TourID)" +
                    ");");
        }catch(Exception e)
        {

        }
    }
}
