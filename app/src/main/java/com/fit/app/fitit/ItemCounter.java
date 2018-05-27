package com.fit.app.fitit;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by MACCAS-PC on 16/05/2018.
 */

public class ItemCounter {

    //variables
    public ArrayList<RecyclerAdapter.Item> mList = new ArrayList<>();
    int mTotal = 0;



    //getter
    public ArrayList<RecyclerAdapter.Item> getList(){return mList;}
    public int getTotal(){ return mTotal;}
    public RecyclerAdapter.Item getItem(int position){ return mList.get(position); }
    public int size() {return mList.size();}

    //setters
    public void setList(ArrayList<RecyclerAdapter.Item> list){
        mList = list;

        //reset total and count again
        mTotal = 0;
        if(mList != null){
            for(int i = 0; i < mList.size(); i++){
                mTotal += mList.get(i).getNum();
            }
        }
    }

    public void addItem(RecyclerAdapter.Item item){
        //add item end and update total
        mList.add(item);
        mTotal += item.getNum();
    }

    public void addItem(String name, int num){
        //add item end and update total
        mList.add(new RecyclerAdapter.Item(name, num));
        mTotal += num;
    }

    public void addItemAtPosition(int position, RecyclerAdapter.Item item)
    {
        //if position is smaller than size and greater than 0
        if(position <= mList.size() && position > -1) {
            mList.add(position, item);
            mTotal += item.getNum();
        }
        else {
            Log.d("insert item at position", "position not within range");
        }
    }

    public void removeItemAtPosition(int position)
    {
        //if position less than size and greater than 0
        if(position < mList.size() && position > -1)
        {
            //update total and remove
            mTotal -= mList.get(position).getNum();
            mList.remove(position);
        }
    }

    public void removeItem()
    {
        //remove item at the end of list
        mTotal -= mList.get(mList.size()-1).getNum();
        mList.remove(mList.size()-1);
    }
}
