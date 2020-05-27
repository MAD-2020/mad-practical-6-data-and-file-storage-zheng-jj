package com.example.mad_practical_6_data_and_file_storage_zheng_jj.week_6_whackamole_3_0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    /*
        The Database has the following properties:
        1. Database name is WhackAMole.db
        2. The Columns consist of
            a. Username
            b. Password
            c. Level
            d. Score
        3. Add user method for adding user into the Database.
        4. Find user method that finds the current position of the user and his corresponding
           data information - username, password, level highest score for each level
        5. Delete user method that deletes based on the username
        6. To replace the data in the database, we would make use of find user, delete user and add user

        The database shall look like the following:

        Username | Password | Level | Score
        --------------------------------------
        User A   | XXX      | 1     |    0
        User A   | XXX      | 2     |    0
        User A   | XXX      | 3     |    0
        User A   | XXX      | 4     |    0
        User A   | XXX      | 5     |    0
        User A   | XXX      | 6     |    0
        User A   | XXX      | 7     |    0
        User A   | XXX      | 8     |    0
        User A   | XXX      | 9     |    0
        User A   | XXX      | 10    |    0
        User B   | YYY      | 1     |    0
        User B   | YYY      | 2     |    0
     */
    private static final String DATABASE_NAME = "WhackAMole.db";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_NAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_SCORE = "score";

    private static MyDBHandler instance;

    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        /* HINT:
            This is used to init the database.
         */
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /* HINT:
            This is triggered on DB creation.
            Log.v(TAG, "DB Created: " + CREATE_ACCOUNTS_TABLE);
         */
        Log.v(TAG, "DB Created: " + "WhackAMole.db");
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + "'"+TABLE_USERS+"'" +
                "("+
                    COLUMN_NAME + " TEXT, "+
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_LEVEL + " INTEGER," +
                    COLUMN_SCORE + " INTEGER" +
                ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        /* HINT:
            This is triggered if there is a new version found. ALL DATA are replaced and irreversible.
         */
        db.execSQL("DROP TABLE IF EXISTS " + "'"+TABLE_USERS+"'");
        onCreate(db);
    }

    public void addUser(UserData userData)
    {
            /* HINT:
                This adds the user to the database based on the information given.
                Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
             */
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        for(int level = 0; level<10;level++){
            values.put(COLUMN_NAME, userData.getMyUserName());
            values.put(COLUMN_PASSWORD, userData.getMyPassword());
            values.put(COLUMN_LEVEL, level+1);
            values.put(COLUMN_SCORE, 0);
            db.insert(TABLE_USERS, null, values);
        }
        Log.v(TAG,"added user to database: "+userData.getMyUserName());

    }
    public String printall(){
        SQLiteDatabase db = this.getWritableDatabase();
        String x = "";
        Cursor allRows  = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        allRows.moveToFirst();
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    x += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                x += "\n";

            } while (allRows.moveToNext());
        }
        allRows.close();
        return x;
    }

    public UserData findUser(String username)
    {
        /* HINT:
            This finds the user that is specified and returns the data information if it is found.
            If not found, it will return a null.
            Log.v(TAG, FILENAME +": Find user form database: " + query);

            The following should be used in getting the query data.
            you may modify the code to suit your design.

            if(cursor.moveToFirst()){
                do{
                    ...
                    .....
                    ...
                }while(cursor.moveToNext());
                Log.v(TAG, FILENAME + ": QueryData: " + queryData.getLevels().toString() + queryData.getScores().toString());
            }
            else{
                Log.v(TAG, FILENAME+ ": No data found!");
            }
         */
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v(TAG, FILENAME +": Find user form database: " + username);
        UserData user = new UserData();
        String name;
        String password;
        ArrayList<Integer> levellist = new ArrayList<Integer>(){};
        ArrayList<Integer> scorelist = new ArrayList<Integer>(){};
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = "+"'"+username+"'" +" ORDER BY username,level",null);
        cursor.moveToFirst();
        try{
            do {
                name = cursor.getString(0);
                password = cursor.getString(1);
                int level = Integer.parseInt(cursor.getString(2));
                int score = Integer.parseInt(cursor.getString(3));
                Log.v(TAG,name+"-"+password+"-"+Integer.toString(level)+"-"+Integer.toString(score));
                levellist.add(level-1);
                scorelist.add(score);
            } while (cursor.moveToNext());
            user.setMyUserName(name);
            user.setMyPassword(password);
            user.setLevels(levellist);
            user.setScores(scorelist);
            Log.v(TAG,Integer.toString(user.getLevels().size()) );
            Log.v(TAG,Integer.toString(user.getScores().size()) );
            Log.v(TAG,"user found "+user.getMyUserName());
            }
        catch (Exception e){Log.v(TAG,"user not found "+user.getMyUserName());}
        cursor.close();
        return user;
    }
    public boolean deleteAccount(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        /* HINT:
            This finds and delete the user data in the database.
            This is not reversible.
            Log.v(TAG, FILENAME + ": Database delete user: " + query);
         */
        boolean result = false;
        try{
            db.delete(TABLE_USERS, COLUMN_NAME + " = ?",
                    new String[] { String.valueOf(username) });
            result = true;
            db.close();
            return result;
        }
        catch(Exception e){
            return result;
        }
    }

    public Boolean updateuserScore(String username, int score, int difficulty, Integer bestscore) {
        try{
            if (bestscore<score){
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("UPDATE "+TABLE_USERS+" SET score = "+Integer.toString(score) + " WHERE username = "+"'"+username+"'"+" AND level = "+Integer.toString(difficulty+1));
                db.close();
                return true;
            }
            else {
                return true;
            }
        }
        catch (Exception e) {
            return false;
        }
    }
}
