package com.example.mad_practical_6_data_and_file_storage_zheng_jj.week_6_whackamole_3_0;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */
        final UserData loggedin = new UserData();
        final Button login = findViewById(R.id.Login);
        final EditText name = findViewById(R.id.usernamelogin);
        final EditText password = findViewById(R.id.passwordlogin);
        final TextView Register = findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name2 = name.getText().toString();
                String pass2 = password.getText().toString();
                if(isValidUser(name2,pass2)){
                    MyDBHandler db = new MyDBHandler(login.getContext(), "WhackAMole.db", null, 6);
                    UserData loggedinuser = db.findUser(name2);
                    db.close();
                    Intent intent = new Intent(login.getContext(), Main3Activity.class);
                    intent.putExtra("loggedinuser",loggedinuser);
                    intent.putIntegerArrayListExtra("userlevels",loggedinuser.getLevels());
                    intent.putIntegerArrayListExtra("userscores",loggedinuser.getScores());
                    Log.v(TAG,"LOG IN SUCCESSFUL");
                    login.getContext().startActivity(intent);
                }
                else{
                    Toast msg = null;
                    msg.makeText(login.getContext(), "Invalid Login Credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.getContext(),Main2Activity.class);
                login.getContext().startActivity(intent);
            }
        });
    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        try {
            MyDBHandler db = new MyDBHandler(this, "WhackAMole.db", null, 6);
            String actualpassword = db.findUser(userName).getMyPassword();
            db.close();
            if (actualpassword .equals(password)) {
                Log.v(TAG,"successful login");
                return true;
            } else {
                Log.v(TAG,"Entered username "+userName+", Actual password: "+actualpassword+", Entered password: "+password);
                Toast msg = null;
                msg.makeText(this, "Invalid Login!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e){
            Log.v(TAG,"Error at login");
            return false;
        }
    }

}
