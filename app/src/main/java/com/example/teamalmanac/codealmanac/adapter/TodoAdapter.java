package com.example.teamalmanac.codealmanac.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.teamalmanac.codealmanac.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by somin on 16. 11. 21.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<CheckBox> todos = new ArrayList<>();
    public TodoAdapter(List<CheckBox> todos) {
        this.todos = todos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_todo, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setCheckBox(todos.get(position));
    }

    public void setTodos(List<CheckBox> items) {
        todos.clear();
        todos.addAll(items);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.todo_checkbox);
        }

        public void setCheckBox(CheckBox checkBox){
            this.checkBox = checkBox;
        }

        public CheckBox getCheckBox(){
            return checkBox;
        }
    }

}
