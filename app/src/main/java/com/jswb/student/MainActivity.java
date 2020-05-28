package com.jswb.student;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    int open;
    private TextView tv_word;
    private Button btn_save;

    Date curTime;
    SimpleDateFormat md = new SimpleDateFormat("MMdd");
    SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
    String date;
    String time;

    Calendar c = Calendar.getInstance();
    int week = c.get(Calendar.DAY_OF_WEEK);
    int minute;
    int latetime;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Record");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find();
        open = c.get(Calendar.HOUR_OF_DAY);

        tv_context.setText(String.valueOf(open));
        switch (week) {
            case 2:
            case 6:
                latetime = 30;
                schoolDay();
                break;
            case 3:
            case 4:
            case 5:
                latetime = 20;
                schoolDay();
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

        if (TestNetWork())
            btn_save.setVisibility(View.VISIBLE);
        else
            btn_save.setVisibility(View.INVISIBLE);
    }

    public boolean TestNetWork() {
        Context context = this;
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            Handler handler = new Handler();
            if (activeNetInfo == null) {
                handler.sendEmptyMessage(8);
                return false;
            }
            boolean netInfo = activeNetInfo.isAvailable();
            if (!netInfo) {
                handler.sendEmptyMessage(8);
                return false;
            }
        }
        return true;
    }

    private void schoolDay() {
        if (open < 8) {
            tv_time.setText(String.valueOf(latetime));
        } else if (open > 15) {
            tv_word.setVisibility(View.INVISIBLE);
            tv_time.setText("自主練習");
        } else {
            for (CheckBox i : group)
                i.setEnabled(false);
            tv_time.setText("上課時間");
        }
    }

    private void find() {
        tv_time = findViewById(R.id.time);
        tv_context = findViewById(R.id.tv_context);
        tv_word = findViewById(R.id.word);
        btn_save = findViewById(R.id.save);

//        Student one = new Student(1, "", findViewById(R.id.one));
        Student two = new Student(2, "", findViewById(R.id.two));
        Student three = new Student(3, "5529213F", findViewById(R.id.three));
        Student four = new Student(4, "2529093F", findViewById(R.id.four));
        Student five = new Student(5, "59306099", findViewById(R.id.five));
        Student six = new Student(6, "", findViewById(R.id.six));
//        Student seven = new Student(7, "", findViewById(R.id.seven));
        Student eight = new Student(8, "", findViewById(R.id.eight));
        Student nine = new Student(9, "05EA143F", findViewById(R.id.nine));
        Student ten = new Student(10, "F93E38A3", findViewById(R.id.ten));
        Student eleven = new Student(11, "E5B5813C", findViewById(R.id.eleven));
//        Student twelve = new Student(12, "",findViewById(R.id.twelve));
        Student thirteen = new Student(13, "35E2803C", findViewById(R.id.thirteen));
        Student fourteen = new Student(14, "55B0873C", findViewById(R.id.fourteen));
        Student fifteen = new Student(15, "A587833C", findViewById(R.id.fifteen));
        Student sixteen = new Student(16, "8509883C", findViewById(R.id.sixteen));
        Student seventeen = new Student(17, "95918F3D", findViewById(R.id.seventeen));
        Student eighteen = new Student(18, "", findViewById(R.id.eighteen));
        Student nineteen = new Student(19, "D5B6913D", findViewById(R.id.nineteen));
        Student twenty = new Student(20, "F5B31A3F", findViewById(R.id.twenty));
        Student twentyone = new Student(21, "25DA243F", findViewById(R.id.twentyone));
        Student twentytwo = new Student(22, "", findViewById(R.id.twentytwo));
        Student twentythree = new Student(23, "", findViewById(R.id.twentythree));
        Student twentyfour = new Student(24, "B5C00C3F", findViewById(R.id.twentyfour));
        Student twentyfive = new Student(25, "C5F5823C", findViewById(R.id.twentyfive));
//        Student twentysix = new Student(26, "",findViewById(R.id.twentysix));
        Student twentyseven = new Student(27, "15EE1A3F", findViewById(R.id.twentyseven));
        Student twentyeight = new Student(28, "", findViewById(R.id.twentyeight));

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
                for (CheckBox i : group)
                    if (i.isSelected()) {
                        int j = Integer.parseInt(i.getText().toString());
                        for (Student k : students)
                            if (k.number == j) {
                                String time = k.time.substring(3);
                                isLate(i, Integer.parseInt(time));
                            }
                        Toast("更改完成");
                    }
            }
        }).setCancelable(false).show();
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
        } else {
            setTime(checkBox, -1);
            checkBox.setBackgroundResource(R.drawable.btn);
        }
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
                                if (i.time.equals("null"))
                                    Toast("沒有時間");
                                else
                                    Toast(i.time);
                            }
                        break;
                    case 1:
                        if (checkBox.isSelected()) {
                            String s = null;
                            int j = Integer.parseInt(checkBox.getText().toString());
                            for (Student k : students)
                                if (k.number == j) {
                                    s = k.time;
                                }
                            if (!s.equals("請假"))
                                changeGet(checkBox, s);
                            else
                                Toast("請假無法更改時間");
                        } else
                            Toast("還沒按過");
                        break;
                    case 2:
                        if (!checkBox.isSelected()) {
                            checkBox.setSelected(true);
                            setTime(checkBox, 0);
                            Toast(checkBox.getText().toString() + "號請假");
                            checkBox.setBackgroundResource(R.drawable.rest);
                        } else {
                            Toast("請先將狀態取消，再重新設定");
                        }
                        break;
                }
            }
        }).show();
    }

    private void changeGet(final CheckBox checkBox, String s) {
        int hourOfDay = Integer.parseInt(s.substring(0, 2));
        int minute = Integer.parseInt(s.substring(3, 5));
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int number = Integer.parseInt((String) checkBox.getText());
                for (Student i : students)
                    if (i.number == number) {
                        String hour = String.valueOf(hourOfDay);
                        String min = String.valueOf(minute);
                        if (hourOfDay < 10)
                            hour = "0" + hourOfDay;
                        if (minute < 10)
                            min = "0" + minute;
                        time = hour + ":" + min;
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
                    curTime = new Date(System.currentTimeMillis());
                    time = hm.format(curTime);
                    Toast(time);
                    i.time = time;
                } else if (state == 0)
                    i.time = "請假";
                else if (state == -1)
                    i.time = "null";
                break;
            }
    }

    private void isLate(CheckBox checkBox, int minute) {
        if (minute > latetime)
            checkBox.setBackgroundResource(R.drawable.late);
        else checkBox.setBackgroundResource(R.drawable.ontime);
    }

    private void Toast(String context) {
        Toast.makeText(MainActivity.this, context, Toast.LENGTH_SHORT).show();
    }
}
