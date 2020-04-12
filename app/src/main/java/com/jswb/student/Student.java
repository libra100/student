package com.jswb.student;

import android.view.View;
import android.widget.CheckBox;

public class Student {
    public int number;
    public String time = "null";
    public CheckBox checkBox;

    public Student() {
    }

    public Student(int number, View view) {
        this.number = number;
        this.checkBox = (CheckBox) view;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
