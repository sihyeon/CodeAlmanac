package com.example.teamalmanac.codealmanac.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.teamalmanac.codealmanac.R;
import com.example.teamalmanac.codealmanac.bean.MainfocusDataType;
import com.example.teamalmanac.codealmanac.database.DataManager;
import com.example.teamalmanac.codealmanac.database.SQLContract;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Choi Jaeung on 2016-12-08.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> logList;
    private Context mContext;
    public LogAdapter (Context context, ArrayList<HashMap<String, Object>> logList){
        this.mContext = context;
        this.logList = logList;
    }

    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_log_parent, null);
        return (new ViewHolder(v));
    }

    @Override
    public void onBindViewHolder(LogAdapter.ViewHolder holder, int position) {
        HashMap<String, Object> item = logList.get(position);
        Log.d("todo_log_adapter", (String)item.get("date"));
        holder.tv_date.setText((String)item.get("date"));
        holder.lv_mainfocusList.setAdapter((SimpleAdapter)item.get("mainfocus_adapter"));
        ViewGroup.LayoutParams params = holder.lv_mainfocusList.getLayoutParams();
        params.height = ((SimpleAdapter) item.get("mainfocus_adapter")).getCount() * translatePxToDp(40f);
        holder.lv_mainfocusList.setLayoutParams(params);

        holder.lv_todoList.setAdapter((SimpleAdapter)item.get("todo_adapter"));
        params = holder.lv_todoList.getLayoutParams();
        params.height = ((SimpleAdapter) item.get("todo_adapter")).getCount() * translatePxToDp(40f);
        holder.lv_todoList.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    private int translatePxToDp(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView tv_date;
        protected ListView lv_mainfocusList;
        protected ListView lv_todoList;
        public ViewHolder(View itemView) {
            super(itemView);
            Typeface typefaceR = Typeface.createFromAsset(mContext.getAssets(), "NanumSquareR.ttf");
            Typeface typefaceB = Typeface.createFromAsset(mContext.getAssets(), "NanumSquareB.ttf");
            tv_date = (TextView)itemView.findViewById(R.id.parent_text_date);
            tv_date.setTypeface(typefaceR);
            ((TextView)itemView.findViewById(R.id.parent_text_mainfocus_title)).setTypeface(typefaceB);
            ((TextView)itemView.findViewById(R.id.parent_text_todo_title)).setTypeface(typefaceB);
            lv_mainfocusList = (ListView)itemView.findViewById(R.id.parent_listview_mainfocus);
            lv_todoList = (ListView)itemView.findViewById(R.id.parent_listview_todo);
        }
    }
}
