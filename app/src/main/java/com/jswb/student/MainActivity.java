package com.jswb.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
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


    FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    DatabaseReference database = firebase.getReference();

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
        setLatetime();

        curTime = new Date(System.currentTimeMillis());
        date = md.format(curTime);

        class Post {
            public String card;
            public String time;

            public Post(String card, String time) {
                this.card = card;
                this.time = time;
            }
        }


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Post post = dataSnapshot.getValue(Post.class);
//                System.out.println(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        tv_context.setText("還要做發送到Line群組告知遲到的人，加油!!");
    }

    private void setLatetime() {
        switch (week) {
            case 2:
            case 6:
                latetime = 30;
                break;
            case 3:
            case 4:
            case 5:
                latetime = 20;
                break;
            default:
                latetime = -1;
                break;
        }
        if (latetime == -1) {
            tv_time.setText("不用上課啦!");
            for (CheckBox i : group)
                i.setEnabled(false);
        } else {
            tv_time.setText(String.valueOf(latetime));
            for (CheckBox i : group)
                i.setEnabled(true);
        }
    }

    private void find() {
        tv_time = findViewById(R.id.time);
        tv_context = findViewById(R.id.tv_context);
        test = findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CheckBox i : group) {
                    i.setBackgroundResource(R.drawable.btn);
                    i.setEnabled(!i.isEnabled());
                }
                if (test.isChecked()) {
                    changeTime(tv_time);
                    for (CheckBox i : group)
                        i.setSelected(false);
                } else
                    setLatetime();
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
        builder.setItems(R.array.time_array, new DialogInterface.OnClickListener() {
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
        Toast.makeText(this, "還在開發", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CheckActivity.class);
//        startActivity(intent);
    }

    public void save(View view) {
        boolean thereis = false;
        for (Student i : students) {
            if (!i.time.contentEquals(i.checkBox.getText())) {
                thereis = true;
                break;
            }
        }
        if (thereis)
            newDay();
        else
            Toast.makeText(this, "不用保存", Toast.LENGTH_SHORT).show();
    }

    private void newDay() {//上傳資料
        database.child(date).child("狀態").setValue("上傳");
        for (Student i : students)
            database.child(date).child(String.valueOf(i.number)).setValue(i.time);
        database.child(date).child("遲到時間").setValue(latetime);
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
            Toast.makeText(this, "無資料清除", Toast.LENGTH_SHORT).show();
    }

    private void ask() {//確認清除資料
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
                newDay();
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
            setTime(checkBox);
            if (minute > latetime)//判斷遲到
                checkBox.setBackgroundResource(R.drawable.late);
            else checkBox.setBackgroundResource(R.drawable.ontime);
        } else
            checkBox.setBackgroundResource(R.drawable.btn);
    }

    private void longclick(CheckBox checkBox) {
        checkBox.setSelected(!checkBox.isSelected());
        if (checkBox.isSelected()) {
            int number = Integer.parseInt((String) checkBox.getText());
            for (Student i : students)
                if (i.number == number) {
                    i.time = "請假";
                    break;
                }
            checkBox.setBackgroundResource(R.drawable.rest);
        } else
            checkBox.setBackgroundResource(R.drawable.btn);
    }

    private void setTime(CheckBox checkBox) {
        getTime();
        int number = Integer.parseInt((String) checkBox.getText());
        for (Student i : students)
            if (i.number == number) {
                i.time = time;
                break;
            }
    }

    private void getTime() {
        curTime = new Date(System.currentTimeMillis()); // 獲取當前時間
        time = hm.format(curTime);
        Toast.makeText(this, time, Toast.LENGTH_SHORT).show();
        c = Calendar.getInstance();
        minute = c.get(Calendar.MINUTE);
    }
}
