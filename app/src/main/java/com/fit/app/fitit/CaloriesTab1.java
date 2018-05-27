package com.fit.app.fitit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by MACCAS-PC on 26/05/2018.
 */

public class CaloriesTab1 extends Fragment implements AddFoodDialog.DialogListener{
    private static final String TAG = "CaloriesTab1";

    ItemCounter calorieList;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button buttonAdd;
    private TextView textTotal;
    private Button buttonSave;

    //inner class
    public static class AlertReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("alarm", "notification");
            //do stuff when the alarm triggers
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calories_tab1, container, false);

        //initialize
        calorieList = new ItemCounter();
        //loadData();

        //add variables
        //calorieList.addItem("Line 1", 3);
        //calorieList.addItem("Line 3", 3);
        //calorieList.addItem("Line 5", 3);

        buildRecyclerView(view);

        //assign IDs
        buttonAdd = view.findViewById(R.id.button_add);
        textTotal = view.findViewById(R.id.text_total);
        //buttonSave = view.findViewById(R.id.button_save);

        //set on click for button
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        /*buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });*/

        updateTotalCalories();

        //startAlarm();

        return view;
    }

    public void startAlarm(){
        //set time
        Calendar c = Calendar.getInstance();
        //c.set(Calendar.HOUR_OF_DAY, 23);
        //c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 0);

        Log.d("hourmin", c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));

        //create alarm manager
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);

        //if alarm not already created
        //DOES NOT exist return null
        if(PendingIntent.getBroadcast(getContext(), 1,new Intent(getContext(),AlertReceiver.class), PendingIntent.FLAG_NO_CREATE) != null){
            Log.d("start Alarm", "already created");
            alarmManager.cancel(pendingIntent); //cancel to reset
        }
        else{
            Log.d("start Alarm", "not created yet");
        }

        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        //long twentyfour = 10000;

        //repeat every 24 hours
        final long twentyfour = 24*60*60*1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), twentyfour, pendingIntent);
    }

    public void insertItem(int position, String name, int calories){
        calorieList.addItemAtPosition(position, new RecyclerAdapter.Item(name, calories));
        mAdapter.notifyItemInserted(position);
    }

    public void addItemToEnd(String name, int calories){
        //add items to the end
        calorieList.addItem(name, calories);
        mAdapter.notifyItemInserted(calorieList.size());
    }

    public void removeItem(){
        //remove the end of the list (take note of size -1 because size changes when removing item)
        calorieList.removeItemAtPosition(calorieList.size()-1);
        mAdapter.notifyItemRemoved(calorieList.size());

        updateTotalCalories();
    }

    public void removeItemAtPosition(int position){
        calorieList.removeItemAtPosition(position);
        mAdapter.notifyItemRemoved(position);

        updateTotalCalories();
    }

    public void changeItem(int position, String text, int calories){
        calorieList.getItem(position).changeName(text);
        calorieList.getItem(position).changeNum(calories);
        mAdapter.notifyItemChanged(position);
    }

    public void buildRecyclerView(View view){
        //set recycler id
        mRecyclerView = view.findViewById(R.id.recyclerView);

        //initialise layout manager and adapter(as recycler adapter) with data
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(calorieList.getList());

        //set manager and adapter to recyclerview
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //set on click listeners for recyclerview
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {}

            @Override
            public void onDeleteClick(int position) {
                removeItemAtPosition(position);
            }
        });
    }

    public void openDialog(){
        //creates and shows dialog
        AddFoodDialog fDialog = new AddFoodDialog();
        fDialog.setTargetFragment(CaloriesTab1.this, 123); //add this line for fragments
        fDialog.show(getFragmentManager(), "Dialog");
        //fDialog.show(getFragmentSupportManager, "Dialog"); //activity version
    }

    @Override
    public void applyTexts(String text1, String text2) {
        if(text1.matches("") || text2.matches("")){
            Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
        }
        else {
            addItemToEnd(text1, Integer.parseInt(text2));
            updateTotalCalories();
            //saveData();
        }
    }

    public void updateTotalCalories() {
        String total = "Total = " + String.valueOf(calorieList.getTotal()) + " Cal";
        textTotal.setText(total);
    }

    private void saveData(){
        //create sharedpreference from String key then create an editor
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CalToday", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //convert list to json/gson string
        Gson gson = new Gson();
        String json = gson.toJson(calorieList.getList());

        //add to another key set and apply
        editor.putString("CalList", json);
        editor.apply();
    }

    private void loadData() {
        //create shared preference from String key
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CalToday", Context.MODE_PRIVATE);

        //get json/gson string form key set
        Gson gson = new Gson();
        String json = sharedPreferences.getString("CalList", null);

        //convert back to list
        Type type = new TypeToken<ArrayList<RecyclerAdapter.Item>>() {}.getType();
        ArrayList<RecyclerAdapter.Item> list = gson.fromJson(json, type);

        //initialize list
        calorieList = new ItemCounter();

        //if list is not null set list
        if (list != null){
            calorieList.setList(list);
        }
    }
}
