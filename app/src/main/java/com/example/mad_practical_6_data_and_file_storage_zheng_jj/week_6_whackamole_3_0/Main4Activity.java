package com.example.mad_practical_6_data_and_file_storage_zheng_jj.week_6_whackamole_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main4Activity extends AppCompatActivity {
    CountDownTimer myCountDown;
    int Score =0;
    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;

    private void readyTimer(final Integer difficulty){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        myCountDown = new CountDownTimer(10000, 1000){
            Toast ToastDisplay;
            public void onTick(long millisUntilFinished){
                Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
                ToastDisplay.makeText(getApplicationContext(), "Get ready in "+Integer.toString((int) (millisUntilFinished/1000)) +" Seconds!", Toast.LENGTH_SHORT).show();

            }
            public void onFinish(){
                Log.v(TAG, "Ready CountDown Complete!");
                ToastDisplay.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT).show();
                placeMoleTimer(difficulty);
                myCountDown.cancel();
            }
        };
        myCountDown.start();
    }
    protected void onPause() {
        super.onPause();
        if(myCountDown != null) myCountDown.cancel();
    }
    public void placeMoleTimer(final Integer difficulty){
        Integer interval;
        interval = (11-difficulty)*1000;
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                pertick(difficulty);
            }
        }, 0, interval);
    }
    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.hole1,R.id.hole2,R.id.hole3,R.id.hole4,R.id.hole5,R.id.hole6,R.id.hole7,R.id.hole8,R.id.hole9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Score = 0;
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */
        Bundle extras = getIntent().getExtras();
        final int difficulty = extras.getInt("level chosen");
        final String username = extras.getString("name");
        
        final TextView scoretext = findViewById(R.id.scoretext);
        scoretext.setText("Advanced score: "+Integer.toString(Score));
        Button stop = findViewById(R.id.stop);
        TextView diff = findViewById(R.id.diff);
        diff.setText("Difficulty: "+Integer.toString(difficulty+1));
        
        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            final Button tempbtn = findViewById(id);
            tempbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Score = doCheck(tempbtn,Score);
                    scoretext.setText("Advanced score: "+Integer.toString(Score));
                }
            });
        }
        //background thread for the timer
        final Runnable r = new Runnable() {
            public void run() {
                pertick(difficulty);
            }
        };
        new Thread(r).start();
        
        //stop the game, updates user score and stops the background thread
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore(username,Score,difficulty);
                new Thread(r).interrupt();
            }
        });
        

        readyTimer(difficulty);
    }

    private void updateUserScore(String username, int score, int difficulty) {
        MyDBHandler db = new MyDBHandler(this, "WhackAMole.db", null, 6);
        UserData loggedinuser = db.findUser(username);
        Boolean success = db.updateuserScore(username,score,difficulty,loggedinuser.getScores().get(difficulty));
        db.close();
        if(success){
            Log.v(TAG,"New score = "+ db.findUser(username).getScores().get(difficulty));
            Intent intent = new Intent(this, Main3Activity.class);
            MyDBHandler db2 = new MyDBHandler(this, "WhackAMole.db", null, 6);
            loggedinuser = db2.findUser(username);
            db2.close();
            intent.putExtra("loggedinuser",loggedinuser);
            intent.putIntegerArrayListExtra("userlevels",loggedinuser.getLevels());
            intent.putIntegerArrayListExtra("userscores",loggedinuser.getScores());
            Log.v(TAG,"LOG IN SUCCESSFUL");
            this.startActivity(intent);
        }
        else{
            Log.v(TAG,"Score not updated");
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
    private Integer doCheck(Button checkButton,Integer score)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        if (checkButton.getText() == "*") {
            score++;
            //instead of setting a new mole, i cleared all the moles here to balance the game
            clearMole(checkButton);
            Log.v(TAG, "HIT! SCORE +1!");
        } else {
            score--;
            Log.v(TAG, "MISS! SCORE -1!");
        }
        return score;
    }
    public void clearMole(Button molebutton)
    {
        molebutton.setText("o");
    }
    public void clearAllMoles() {
        for(Integer buttonid : BUTTON_IDS){
            Button temp = findViewById(buttonid);
            temp.setText("o");
        }
    }

    public void setNewMole(Integer difficulty)
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        if (difficulty>=5) {
            List<Integer> newmoles = new ArrayList<>();
            Random ran = new Random();

            while(newmoles.size()!=2){
                int randomLocation = ran.nextInt(9);
                if(newmoles.contains(randomLocation)){
                    continue;
                }
                else{
                    newmoles.add(randomLocation);
                }
            }
            clearAllMoles();
            for(Integer newmole : newmoles){
                Button tobemole = findViewById(BUTTON_IDS[newmole]);
                tobemole.setText("*");
            }
        }
        else{
            Random ran = new Random();
            int randomLocation = ran.nextInt(9);
            clearAllMoles();
            Button newmole = findViewById(BUTTON_IDS[randomLocation]);
            newmole.setText("*");
        }
    }

    private void pertick(final Integer difficulty){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearAllMoles();
                setNewMole(difficulty);
            }
        });
    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        newMolePlaceTimer.cancel();
        readyTimer.cancel();
    }

}
