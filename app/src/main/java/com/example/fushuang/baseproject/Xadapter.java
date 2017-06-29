package com.example.fushuang.baseproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 2017/6/24.
 */

public class Xadapter extends RecyclerView.Adapter<Xadapter.XHolder> {

    public Context mContext;
    public List<String> mList;

    public Xadapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public XHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_x,parent,false);
        return new XHolder(view);
    }

    @Override
    public void onBindViewHolder(XHolder holder, int position) {
            holder.mTextView.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class XHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public XHolder(View itemView) {
            super(itemView);
            mTextView= (TextView) itemView.findViewById(R.id.text);
        }

    }


}
