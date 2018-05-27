package com.fit.app.fitit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by MACCAS-PC on 15/05/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //variables
    private ArrayList<Item> mList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ mListener = listener; }

    //inner class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);

            //clicked item in recyclerview
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            //clicked image within item
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    //inner class
    public static class Item {
        private String mName;
        private int mNum;

        //constructor
        public Item(String name, int num){
            mName = name;
            mNum = num;
        }

        //setter
        public void changeName(String text){ mName = text; }
        public void changeNum(int num) { mNum = num; }
        //getter
        public String getName(){ return mName; }
        public int getNum() {return mNum;}
    }

    //constructor
    public RecyclerAdapter(ArrayList<Item> aList){
        mList = aList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder rvh = new ViewHolder(v, mListener);

        return rvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item currentItem = mList.get(position);

        holder.mTextView1.setText(currentItem.getName());
        holder.mTextView2.setText(String.valueOf(currentItem.getNum()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
