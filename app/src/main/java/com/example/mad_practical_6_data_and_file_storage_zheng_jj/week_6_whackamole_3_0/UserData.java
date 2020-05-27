package com.example.mad_practical_6_data_and_file_storage_zheng_jj.week_6_whackamole_3_0;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Parcelable {

    /* NOTE:
            THIS OBJECT DATA IS GIVEN.
            DO NOT CHANGE ANYTHING IN HERE.
            YOU ARE TO USE THIS UserData as it is in your answers.

        The UserData consist of the following:
        1. UserName
        2. Password
        3. A list of Scores related to a list of corresponding Levels
        4. A list of Levels related to a list of corresponding Scores

     */
    private String MyUserName;
    private String MyPassword;
    private ArrayList<Integer> Scores;
    private ArrayList<Integer> Levels;

    public UserData()
    {
    }

    public UserData(String myUserName, String myPassword, ArrayList<Integer> myLevels, ArrayList<Integer> myScores) {
        this.MyUserName = myUserName;
        this.MyPassword = myPassword;
        this.Levels = myLevels;
        this.Scores = myScores;
    }
    public UserData(String myUserName, String myPassword) {
        this.MyUserName = myUserName;
        this.MyPassword = myPassword;
        this.Levels = new ArrayList<Integer>();
        this.Scores = new ArrayList<Integer>();
        for(Integer i = 0; i<10;i++){
            this.Levels.add(i);
            this.Scores.add(0);
        }
    }

    protected UserData(Parcel in) {
        MyUserName = in.readString();
        MyPassword = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public ArrayList<Integer> getLevels() {
        return this.Levels;
    }

    public void setLevels(ArrayList<Integer> levels) {
        this.Levels = levels;
    }

    public ArrayList<Integer> getScores() {
        return this.Scores;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.Scores = scores;
    }

    public String getMyUserName() {
        return this.MyUserName;
    }

    public void setMyUserName(String myUserName) {
        this.MyUserName = myUserName;
    }

    public String getMyPassword() {
        return this.MyPassword;
    }

    public void setMyPassword(String myPassword) {
        this.MyPassword = myPassword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MyUserName);
        dest.writeString(MyPassword);

    }
}
