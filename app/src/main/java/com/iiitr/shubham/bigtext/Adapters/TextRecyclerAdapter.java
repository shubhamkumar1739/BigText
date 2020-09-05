package com.iiitr.shubham.bigtext.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iiitr.shubham.bigtext.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TextRecyclerAdapter extends RecyclerView.Adapter {
    ArrayList<String> mList;
    Context mContext;
    public TextRecyclerAdapter(ArrayList<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_container,null);
        return new TextHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TextHolder)holder).bind(mList.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TextHolder extends RecyclerView.ViewHolder {
        TextView text, textPosition;
        public TextHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            textPosition = itemView.findViewById(R.id.textNumber);
        }
        public void bind(String s, int position) {
            text.setText(s);
            textPosition.setText("" + position + "/" + getItemCount());
        }
    }
}
