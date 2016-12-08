package com.example.teamalmanac.codealmanac.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
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
    private DataManager mDB;
    public LogAdapter (Context context, ArrayList<HashMap<String, Object>> logList){
        this.mContext = context;
        this.logList = logList;
        mDB = DataManager.getSingletonInstance(context);
    }

    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_log_parent, null);
        return (new ViewHolder(v));
    }

    @Override
    public void onBindViewHolder(LogAdapter.ViewHolder holder, int position) {
        HashMap<String, Object> item = logList.get(position);


    }

    @Override
    public int getItemCount() {
        return logList.size();
    }


    private Adapter getChildMainFocusListItem(String date){
        ArrayList<HashMap<String, Object>> adapterItem = new ArrayList<>();
        ArrayList<MainfocusDataType> mainfocusBeenList = mDB.selectionDateMainfocus(date);
        for (MainfocusDataType temp : mainfocusBeenList){
            HashMap <String, Object> tempHash = new HashMap<>();
            tempHash.put(SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, temp.getMainfocus());
            tempHash.put(SQLContract.MainFocusEntry.COLUMN_NAME_DATE, temp.getDate());
            adapterItem.add(tempHash);
        }
        SimpleAdapter mainfocusAdapter = new SimpleAdapter(mContext, adapterItem, R.layout.listview_log_child,
                new String[]{SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, SQLContract.MainFocusEntry.COLUMN_NAME_DATE},
                new int[]{R.id.child_text_content, R.id.child_text_date});
        return mainfocusAdapter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView tv_date;
        protected ListView lv_mainfocusList;
        protected ListView lv_todoList;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView)itemView.findViewById(R.id.parent_text_date);
            lv_mainfocusList = (ListView)itemView.findViewById(R.id.parent_listview_mainfocus);
            lv_todoList = (ListView)itemView.findViewById(R.id.parent_listview_todo);
        }
    }
}
