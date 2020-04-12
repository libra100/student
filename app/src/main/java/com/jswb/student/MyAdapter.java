package com.jswb.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private String[] data;
    private String[] name = new String[]{"唐琇誠", "高聖凱", "郭銘芳", "黃裕閔", "雄庭垣", "蔡明昆", "蔡松恩", "王希平",
            "何姍蓉", "李坤儀", "李芷盈", "李紹綺", "李惠芬", "林玟欣", "林適薰", "徐維鈞", "沈邵淳", "郭旻楨", "曾品雯",
            "蔡沂倢", "蔡博妃", "鄭佳玲", "簡于函", "闕姿怡"};

    public MyAdapter(String[] students) {
        this.data = students;
    }

    public void setData(String[] data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(name[position]);
        holder.time.setText(data[position]);
        holder.stay.setText("0" + "分鐘");
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView time;
        public TextView stay;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            stay = itemView.findViewById(R.id.times);
        }
    }

}
