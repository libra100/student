package com.jswb.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Hand(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Auto(View view) {
        Intent intent = new Intent(this, AutoActivity.class);
        startActivity(intent);
    }

    public void Check(View view) {
        Intent intent = new Intent(this, CheckActivity.class);
        startActivity(intent);
    }
}
