package com.jswb.student;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Student[] students;
    private CheckBox[] group = new CheckBox[24];
    private TextView tv_time;
    private TextView tv_context;
    private ToggleButton test;


    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Record");

    Date curTime;
    SimpleDateFormat md = new SimpleDateFormat("MMdd");
    SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
    String date;
    String time;

    Calendar c = Calendar.getInstance();
    int week = c.get(Calendar.DAY_OF_WEEK);
    int minute;
    int latetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find();

        switch (week) {
            case 2:
            case 6:
                schoolDay();
                latetime = 30;
                break;
            case 3:
            case 4:
            case 5:
                schoolDay();
                latetime = 20;
                break;
            default:
                tv_time.setText("不用上課啦!");
                break;
        }

        curTime = new Date(System.currentTimeMillis());
        date = md.format(curTime);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("狀態")) {
                    int state = Integer.parseInt(String.valueOf(dataSnapshot.child("狀態").getValue()));
                    if (state == 1)
                        Toast("保存完成");
                    else if (state == 2)
                        Toast("清空資料");
                    database.child("狀態").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        tv_context.setText("還要做發送到Line群組告知遲到的人，加油!!");
    }

    private void schoolDay() {
        if (c.get(Calendar.HOUR_OF_DAY) < 8) {
            tv_time.setText(String.valueOf(latetime));
        } else if (c.get(Calendar.HOUR_OF_DAY) > 16) {
            tv_time.setText("自主練習");
        } else
            for (CheckBox i : group)
                i.setEnabled(false);
        tv_time.setText("上課");
    }

    private void find() {
        tv_time = findViewById(R.id.time);
        tv_context = findViewById(R.id.tv_context);
        test = findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!test.isSelected()) {

                }
            }
        });

//        Student one = new Student(1, findViewById(R.id.one));
        Student two = new Student(2, findViewById(R.id.two));
        Student three = new Student(3, findViewById(R.id.three));
        Student four = new Student(4, findViewById(R.id.four));
        Student five = new Student(5, findViewById(R.id.five));
        Student six = new Student(6, findViewById(R.id.six));
//        Student seven = new Student(7, findViewById(R.id.seven));
        Student eight = new Student(8, findViewById(R.id.eight));
        Student nine = new Student(9, findViewById(R.id.nine));
        Student ten = new Student(10, findViewById(R.id.ten));
        Student eleven = new Student(11, findViewById(R.id.eleven));
//        Student twelve = new Student(12, findViewById(R.id.twelve));
        Student thirteen = new Student(13, findViewById(R.id.thirteen));
        Student fourteen = new Student(14, findViewById(R.id.fourteen));
        Student fifteen = new Student(15, findViewById(R.id.fifteen));
        Student sixteen = new Student(16, findViewById(R.id.sixteen));
        Student seventeen = new Student(17, findViewById(R.id.seventeen));
        Student eighteen = new Student(18, findViewById(R.id.eighteen));
        Student nineteen = new Student(19, findViewById(R.id.nineteen));
        Student twenty = new Student(20, findViewById(R.id.twenty));
        Student twentyone = new Student(21, findViewById(R.id.twentyone));
        Student twentytwo = new Student(22, findViewById(R.id.twentytwo));
        Student twentythree = new Student(23, findViewById(R.id.twentythree));
        Student twentyfour = new Student(24, findViewById(R.id.twentyfour));
        Student twentyfive = new Student(25, findViewById(R.id.twentyfive));
//        Student twentysix = new Student(26, findViewById(R.id.twentysix));
        Student twentyseven = new Student(27, findViewById(R.id.twentyseven));
        Student twentyeight = new Student(28, findViewById(R.id.twentyeight));

        students = new Student[]{two, three, four, five, six, eight, nine, ten, eleven, thirteen,
                fourteen, fifteen, sixteen, seventeen, eighteen, nineteen, twenty, twentyone,
                twentytwo, twentythree, twentyfour, twentyfive, twentyseven, twentyeight};

        for (int i = 0; i < students.length; i++) {
            group[i] = students[i].checkBox;
        }
        setClickListener();
    }

    private void setClickListener() {
        Onclick onclick = new Onclick();
        for (CheckBox i : group) {
            i.setOnClickListener(onclick);
            i.setOnLongClickListener(onclick);
        }
    }

    public void change(View view) {
        TextView textView = (TextView) view;
        changeTime(textView);
    }

    public void changeTime(final TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.time, new DialogInterface.OnClickListener() {
            int pick;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        pick = 20;
                        break;
                    case 1:
                        pick = 25;
                        break;
                    case 2:
                        pick = 30;
                        break;
                    case 3:
                        pick = 35;
                        break;
                }
                textView.setText("" + pick);
                latetime = pick;
            }
        }).setCancelable(false).show();
    }

    public void check(View view) {
        Intent intent = new Intent(this, CheckActivity.class);
        startActivity(intent);
    }

    public void save(View view) {
        newDay(1);
    }

    private void newDay(int state) {
        database.child("狀態").setValue(state);
        for (Student i : students)
            database.child(String.valueOf(i.number)).child(date).setValue(i.time);
        database.child("遲到時間").child(date).setValue(latetime);
    }

    public void clear(View view) {
        boolean thereis = false;
        for (CheckBox i : group) {
            if (i.isSelected()) {
                thereis = true;
                ask();
                break;
            }
        }
        if (!thereis)
            Toast("無資料清除");
    }

    private void ask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("你認真?").setMessage("確定要清空所有同學的狀態嗎");
        builder.setPositiveButton("我認真", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Student i : students) {
                    i.setTime("null");
                    i.checkBox.setSelected(false);
                    i.checkBox.setBackgroundResource(R.drawable.btn);
                }
                newDay(2);
            }
        }).setNegativeButton("不要好了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class Onclick implements View.OnClickListener, View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            longclick((CheckBox) v);
            return true;
        }

        @Override
        public void onClick(View v) {
            click((CheckBox) v);
        }
    }

    private void click(CheckBox checkBox) {
        checkBox.setSelected(!checkBox.isSelected());
        if (checkBox.isSelected()) {
            setTime(checkBox, 1);
        } else
            checkBox.setBackgroundResource(R.drawable.btn);
    }

    private void longclick(final CheckBox checkBox) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.function, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        int number = Integer.parseInt((String) checkBox.getText());
                        for (Student i : students)
                            if (i.number == number) {
                                Toast(i.time);
                            }
                        break;
                    case 1:
                        if (checkBox.isSelected())
                            changeGet(checkBox);
                        else
                            Toast("此人還沒來，不能更改時間");
                        break;
                    case 2:
                        if (!checkBox.isSelected()) {
                            checkBox.setSelected(true);
                            setTime(checkBox, 0);
                            checkBox.setBackgroundResource(R.drawable.rest);
                        } else {
                            Toast("請先將狀態取消，再重新設定");
                        }
                        break;
                }
            }
        }).show();
    }

    private void changeGet(final CheckBox checkBox) {
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int number = Integer.parseInt((String) checkBox.getText());
                for (Student i : students)
                    if (i.number == number) {
                        time = hourOfDay + ":" + minute;
                        isLate(checkBox, minute);
                        Toast(time);
                        i.time = time;
                    }
            }
        }, hourOfDay, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    private void setTime(CheckBox checkBox, int state) {
        int number = Integer.parseInt((String) checkBox.getText());
        for (Student i : students)
            if (i.number == number) {
                if (state == 1) {
                    Calendar c = Calendar.getInstance();
                    minute = c.get(Calendar.MINUTE);
                    isLate(checkBox, minute);
                    getTime();
                    i.time = time;
                } else if (state == 0)
                    i.time = "請假";
                break;
            }
    }

    private void isLate(CheckBox checkBox, int minute) {
        if (minute > latetime)
            checkBox.setBackgroundResource(R.drawable.late);
        else checkBox.setBackgroundResource(R.drawable.ontime);
    }

    private void getTime() {
        curTime = new Date(System.currentTimeMillis());
        time = hm.format(curTime);
        Toast(time);
    }

    private void Toast(String context) {
        Toast.makeText(MainActivity.this, context, Toast.LENGTH_SHORT).show();
    }
}
