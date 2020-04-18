package com.jswb.student;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;

import java.text.DateFormat;
import java.util.Calendar;

public class CheckActivity extends AppCompatActivity {

    private TextView tv_date;
    private Calendar c;
    private MyAdapter adapter;
    private String date;
    private String[] students = new String[25];
    private int[] number = new int[]{0, 2, 3, 4, 5, 6, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 27, 28};

    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Record");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        database.child("狀態").setValue(3);

        tv_date = findViewById(R.id.tv_date);
        students[0] = "打卡時間";

        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);//固定大小
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new MyAdapter(students);
        recyclerView.setAdapter(adapter);


        c = Calendar.getInstance();
        refresh(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(String.valueOf(number[1])).hasChild(date)) {
                    for (int i = 1; i < number.length; i++)
                        if (dataSnapshot.child(String.valueOf(number[i])).child(date).getValue().toString().equals("null"))
                            students[i] = "沒到";
                        else
                            students[i] = dataSnapshot.child(String.valueOf(number[i])).child(date).getValue().toString();
                } else {
                    Toast.makeText(CheckActivity.this, "沒有這天的資料", Toast.LENGTH_SHORT).show();
                    for (int i = 1; i < number.length; i++)
                        students[i] = "";
                }
                adapter.setData(students);
                adapter.notifyDataSetChanged();
                database.child("狀態").setValue(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void date(View view) {
        changeDate();
    }

    private void changeDate() {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                refresh(year, month, dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void refresh(int year, int month, int day) {
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String dateText = DateFormat.getDateInstance().format(c.getTime());
        String mon = String.valueOf(c.get(Calendar.MONTH) + 1);
        String da = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if (c.get(Calendar.MONTH) < 10)
            mon = "0" + mon;
        if (c.get(Calendar.DAY_OF_MONTH) < 10)
            da = "0" + da;
        date = mon + da;
        tv_date.setText(dateText);
        database.child("狀態").setValue(3);
    }
}
