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

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */


    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */
        final Button create = findViewById(R.id.createuser);
        final Button backlogin = findViewById(R.id.backtologin);
        final EditText name = findViewById(R.id.usernameenter);
        final EditText password = findViewById(R.id.passwordenter);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast msg = null;
                String name2 = name.getText().toString();
                String pass2 = password.getText().toString();
                UserData createuser = new UserData(name2,pass2);
                MyDBHandler db = new MyDBHandler(name.getContext(), "WhackAMole.db", null, 6);
                if(db.findUser(name2).getMyUserName()==null) {
                    db.addUser(createuser);
                    msg.makeText(create.getContext(), "User created successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(create.getContext(),MainActivity.class);
                    db.close();
                    create.getContext().startActivity(intent);
                }
                else{
                    msg.makeText(create.getContext(), "User already exist!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(create.getContext(),MainActivity.class);
                create.getContext().startActivity(intent);
            }
        });
    }

    protected void onStop() {
        super.onStop();
        finish();
    }
}
