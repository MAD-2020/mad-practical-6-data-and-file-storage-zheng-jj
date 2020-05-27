package com.example.mad_practical_6_data_and_file_storage_zheng_jj.week_6_whackamole_3_0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    UserData userData=null;
    List<Integer> Levels = new ArrayList<>();
    List<Integer> Score = new ArrayList<>();
    String username;

    public CustomScoreAdaptor(UserData userdata){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        this.Levels=userdata.getLevels();
        this.Score=userdata.getScores();
        this.username=userdata.getMyUserName();
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        /* Hint:
        This method dictates how the viewholder layuout is to be once the viewholder is created.
         */
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View level = inflater.inflate(R.layout.level_select, parent, false);
        CustomScoreViewHolder viewHolder = new CustomScoreViewHolder(level);
        return viewHolder;
    }

    public void onBindViewHolder(final CustomScoreViewHolder holder, final int position){

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */

        holder.Level.setText(Integer.toString(Levels.get(position)+1) );
        holder.Points.setText(Integer.toString(Score.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"LEVEL IN INDEX "+Integer.toString(Levels.get(position)));
                Integer levelchosen = Levels.get(holder.getAdapterPosition());
                //get context from the level textview
                Intent intent = new Intent(holder.Level.getContext(),Main4Activity.class);
                intent.putExtra("name",username);
                intent.putExtra("level chosen",levelchosen);
                holder.Level.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        /* Hint:
        This method returns the the size of the overall data.
         */
        return Levels.size();
    }
}