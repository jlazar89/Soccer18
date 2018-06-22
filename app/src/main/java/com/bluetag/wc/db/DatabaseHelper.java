package com.bluetag.wc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bluetag.wc.utils.Constants;

import static com.bluetag.wc.utils.Constants.DB.MATCHES_TABLE;
import static com.bluetag.wc.utils.Constants.DB.MATCH_STATUS;

/**
 * Created by Dream Land on 2/15/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context) {
        super(context, Constants.DB.DB_NAME, null, Constants.DB.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(Constants.DB.CREATE_MATCHES_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("MATCHES TABLE ERROR", e.getMessage());
        }
        try {
            sqLiteDatabase.execSQL(Constants.DB.CREATE_POINTS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("POINTS TABLE ERROR", e.getMessage());
        }
        try {
            sqLiteDatabase.execSQL(Constants.DB.CREATE_GOAL_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("GOAL TABLE ERROR", e.getMessage());
        }
        try {
            sqLiteDatabase.execSQL(Constants.DB.CREATE_MY_TEAMS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("MY TEAMS TABLE ERROR", e.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //sqLiteDatabase.execSQL(Constants.DB.DROP_MATCHES_TABLE);
        //sqLiteDatabase.execSQL(Constants.DB.DROP_POINTS_TABLE);
        //sqLiteDatabase.execSQL(Constants.DB.DROP_GOAL_TABLE);
        //sqLiteDatabase.execSQL(Constants.DB.DROP_MY_TEAMS_TABLE);
        //onCreate(sqLiteDatabase);
        // If you need to add a column
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " + MATCHES_TABLE + " ADD COLUMN "+ MATCH_STATUS +" TEXT DEFAULT 'Pending'");
        }
    }

    // INSERT VALUES IN MATCHES TABLE
    public boolean insertMatchesData (String id, String date, String round, String team1, String team2, String score, String details, String status) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.DB.M_ID, id);
            cv.put(Constants.DB.DATE, date);
            cv.put(Constants.DB.ROUND, round);
            cv.put(Constants.DB.TEAM1, team1);
            cv.put(Constants.DB.TEAM2, team2);
            cv.put(Constants.DB.SCORE, score);
            cv.put(Constants.DB.DETAILS, details);
            //v2
            cv.put(Constants.DB.MATCH_STATUS, status);
            long result  = this.getWritableDatabase().insert(MATCHES_TABLE, Constants.DB.M_ID, cv);
            this.getWritableDatabase().close();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // UPDATE VALUES IN MATCHES TABLE
    public boolean updateMatchesData (String id, String date, String round, String team1, String team2, String score, String details,  String status) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.DB.DATE, date);
            cv.put(Constants.DB.ROUND, round);
            cv.put(Constants.DB.TEAM1, team1);
            cv.put(Constants.DB.TEAM2, team2);
            cv.put(Constants.DB.SCORE, score);
            cv.put(Constants.DB.DETAILS, details);
            //v2
            cv.put(Constants.DB.MATCH_STATUS, status);
            int result = this.getWritableDatabase()
                    .update(MATCHES_TABLE, cv, Constants.DB.M_ID+"='"+id+"'", null);
            this.getWritableDatabase().close();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // RETRIEVE DATA FROM MATCHES TABLE
    public Cursor retrieveMatchesData () {
        //return this.getReadableDatabase().rawQuery("SELECT * FROM " + Constants.DB.MATCHES_TABLE, null);
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + MATCHES_TABLE + " WHERE " + MATCH_STATUS + " != 'Finish' " , null);

    }

    // RETRIEVE DATA FROM MATCHES TABLE
    public Cursor retrieveFinishedMatchesData () {
        //return this.getReadableDatabase().rawQuery("SELECT * FROM " + Constants.DB.MATCHES_TABLE, null);
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + MATCHES_TABLE + " WHERE " + MATCH_STATUS + " = 'Finish' " , null);
        //return this.getReadableDatabase().rawQuery("SELECT * FROM " + MATCHES_TABLE + " WHERE " + MATCH_STATUS + " != 'Finish' "  + " ORDER BY "+ M_ID+" DESC", null);

    }

    // INSERT VALUES IN POINTS TABLE
    public boolean insertPointsData (String group, String teamNo, String teamName, String status) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.DB.GROUP_NO, group);
            cv.put(Constants.DB.TEAM_NO, teamNo);
            cv.put(Constants.DB.TEAM_NAME, teamName);
            cv.put(Constants.DB.STATUS, status);
            long result = this.getWritableDatabase().insert(Constants.DB.POINT_TABLE, Constants.DB.P_ID, cv);
            this.getWritableDatabase().close();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // UPDATE VALUES IN POINTS TABLE
    public boolean updatePointsData(int id, String group, String teamNo, String teamName, String status) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.DB.GROUP_NO, group);
            cv.put(Constants.DB.TEAM_NO, teamNo);
            cv.put(Constants.DB.TEAM_NAME, teamName);
            cv.put(Constants.DB.STATUS, status);
            int result = this.getWritableDatabase()
                    .update(Constants.DB.POINT_TABLE, cv, Constants.DB.P_ID+" =?", new String[]{String.valueOf(id)});
            this.getWritableDatabase().close();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // RETRIEVE DATA FROM POINTS TABLE
    public Cursor retrievePointsData() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + Constants.DB.POINT_TABLE, null);
    }

    // INSERT OR ADD VALUES IN GOALS TABLE
    public boolean insertGoalsData(String name, String goal, String tag) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.DB.NAME, name);
            cv.put(Constants.DB.GOALS, goal);
            cv.put(Constants.DB.TAG, tag);
            long result = this.getWritableDatabase().insert(Constants.DB.GOAL_TABLE, Constants.DB.G_ID, cv);
            this.getWritableDatabase().close();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // CHECK IF DATA EXISTS IN GOALS TABLE
    public boolean isExistsInGoals(String searchItem) {
        String[] columns = { Constants.DB.NAME };
        String selection = Constants.DB.NAME + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";

        Cursor cursor = this.getReadableDatabase()
                .query(Constants.DB.GOAL_TABLE, columns, selection, selectionArgs, null, null, null, limit);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // UPDATE VALUES IN GOALS TABLE
    public boolean updateGoalsData(int id, String name, String goal, String tag) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.DB.NAME, name);
            cv.put(Constants.DB.GOALS, goal);
            cv.put(Constants.DB.TAG, tag);
            int result = this.getWritableDatabase()
                    .update(Constants.DB.GOAL_TABLE, cv, Constants.DB.G_ID+" =?", new String[]{String.valueOf(id)});
            this.getWritableDatabase().close();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // RETRIEVE ALL DATA FROM GOALS TABLE ORDER BY NUMBER OF GOALS IN DESCENDING MODE
    public Cursor retrieveGoalsData() {
        return this.getReadableDatabase()
                .rawQuery("SELECT * FROM "+Constants.DB.GOAL_TABLE+" ORDER BY "+Constants.DB.GOALS+" DESC", null);
    }

    // INSERT OR ADD VALUES IN MY TEAMS TABLE
    public boolean insertMyTeamsData(String teamName) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.DB.MY_TEAM_NAME, teamName);
            long result = this.getWritableDatabase().insert(Constants.DB.MY_TEAMS_TABLE, Constants.DB.MY_TEAM_NAME, cv);
            this.getWritableDatabase().close();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE / REMOVE FROM MY TEAMS TABLE
    public boolean removeFromMyTeams(String name) {
        try {

            int result = this.getWritableDatabase().delete(Constants.DB.MY_TEAMS_TABLE, Constants.DB.MY_TEAM_NAME +" = '"+name+"'", null);
            if (result > 0){
                return true;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // RETRIEVE ALL DATA FROM GOALS TABLE ORDER BY NUMBER OF GOALS IN DESCENDING MODE
    public Cursor getMyTeams() {
        return this.getReadableDatabase()
                .rawQuery("SELECT * FROM "+Constants.DB.MY_TEAMS_TABLE+" ORDER BY "+Constants.DB.MY_TEAM_NAME+" ASC", null);
    }

}
