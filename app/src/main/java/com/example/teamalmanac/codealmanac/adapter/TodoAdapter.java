package com.example.teamalmanac.codealmanac.adapter;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.teamalmanac.codealmanac.R;
import com.example.teamalmanac.codealmanac.TodoDataType;
import com.example.teamalmanac.codealmanac.database.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by somin on 16. 11. 21.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private DataManager mDb;

    private List<TodoDataType> todos = new ArrayList<>();
    public TodoAdapter(List<TodoDataType> todos) {
        this.todos = todos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_todo, parent, false);
        mDb = DataManager.getSingletonInstance();
        CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.todo_checkbox);
        Typeface typeface = Typeface.createFromAsset(parent.getContext().getAssets(), "FRAMDCN.TTF");
        checkBox.setTypeface(typeface);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.checkBox.setText(todos.get(position).getTodo());
        holder.deleteButton_visibility = Integer.parseInt(todos.get(position).getButton_visibility());
        holder.deleteButton.setVisibility(holder.deleteButton_visibility);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show X button
                if(todos.get(holder.getAdapterPosition()).getButton_visibility().equals(String.valueOf(View.INVISIBLE))) {
                    todos.get(holder.getAdapterPosition()).setButton_visibility(String.valueOf(View.VISIBLE));
                    view.getRootView().findViewById(R.id.todo_delete_button).setVisibility(View.VISIBLE);
                    ((CheckBox) view).setPaintFlags(((CheckBox) view).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                //cancel showing X button
                else {
                    todos.get(holder.getAdapterPosition()).setButton_visibility(String.valueOf(View.INVISIBLE));
                    view.getRootView().findViewById(R.id.todo_delete_button).setVisibility(View.INVISIBLE);
                    if((((CheckBox) view).getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0)
                        ((CheckBox) view).setPaintFlags(((CheckBox) view).getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                }
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todos.get(holder.getAdapterPosition()).setButton_visibility(String.valueOf(View.VISIBLE));
                view.findViewById(R.id.todo_delete_button).setVisibility(View.VISIBLE);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.todo_checkbox);
                checkBox.setChecked(true);
                checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                notifyDataSetChanged();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("", "onClick: !!!!!!!!!!!!!!!!!!!!!!");
                todos.get(holder.getAdapterPosition()).setButton_visibility(String.valueOf(View.INVISIBLE));
                view.setVisibility(View.INVISIBLE);
                CheckBox checkBox = (CheckBox) holder.itemView.findViewById(R.id.todo_checkbox);
                Log.i("!!!!!!!!!!!!!!!1", "onClick: "+checkBox.getText());
                if ((checkBox.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0)
                    checkBox.setPaintFlags(checkBox.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                checkBox.setChecked(false);
                mDb.updateTodoShowing(todos.get(holder.getAdapterPosition()).getDate(), false);
                todos.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        Button deleteButton;
        int deleteButton_visibility;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.todo_checkbox);
            deleteButton = (Button) itemView.findViewById(R.id.todo_delete_button);
            deleteButton.setVisibility(deleteButton_visibility);
        }
    }
}
