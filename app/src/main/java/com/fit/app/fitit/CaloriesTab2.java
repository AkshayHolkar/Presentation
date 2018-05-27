package com.fit.app.fitit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by MACCAS-PC on 26/05/2018.
 */

public class CaloriesTab2 extends Fragment implements AddCaloriesDateDialog.DialogListener{
    private static final String TAG = "CaloriesTab2";

    ItemCounter calorieList;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button buttonAdd;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calories_tab2, container, false);

        //initialize
        calorieList = new ItemCounter();

        //add variables
        //calorieList.addItem("Line 1", 3);
        //calorieList.addItem("Line 3", 3);
        //calorieList.addItem("Line 5", 3);

        buildRecyclerView(view);

        //assign IDs
        buttonAdd = view.findViewById(R.id.button_add);

        //set button listener
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        return view;
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

    public void addItem(String name, int calories) {
        //add item to list, sort by date and update adapter
        if(!checkDuplicates(name, calories)){ calorieList.addItem(name, calories); }
        Collections.sort(calorieList.getList(), new StringDateComparator());
        mAdapter.notifyDataSetChanged();
    }

    public boolean checkDuplicates(String name, int calories){
        for(int i = 0; i < calorieList.size(); i++){
            if(name.compareTo(calorieList.getItem(i).getName())== 0){
                calorieList.getItem(i).changeNum(calories);
                return true;
            }
        }
        return false;
    }

    public void removeItem(){
        //remove the end of the list (take note of size -1 because size changes when removing item)
        calorieList.removeItemAtPosition(calorieList.size()-1);
        mAdapter.notifyItemRemoved(calorieList.size());
    }

    public void removeItemAtPosition(int position){
        calorieList.removeItemAtPosition(position);
        mAdapter.notifyItemRemoved(position);
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
        AddCaloriesDateDialog fDialog = new AddCaloriesDateDialog();
        fDialog.setTargetFragment(CaloriesTab2.this, 123);
        fDialog.show(getFragmentManager(), "Dialog");
    }

    @Override
    public void applyTexts(String text1, String text2) {
        if(text1.matches("") || text2.matches("")){
            Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
        }
        else {
            //addItemToEnd(text1, Integer.parseInt(text2));
            addItem(text1, Integer.parseInt(text2));
        }
    }
}

class StringDateComparator implements Comparator<RecyclerAdapter.Item>{
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public int compare(RecyclerAdapter.Item lhs, RecyclerAdapter.Item rhs) {
        Date date1 = new Date();
        Date date2 = new Date();
        try{
            date1 = dateFormat.parse(lhs.getName());
            date2 = dateFormat.parse(rhs.getName());
        } catch (ParseException e){
            e.printStackTrace();
        }
        return date1.compareTo(date2);
    }
}
