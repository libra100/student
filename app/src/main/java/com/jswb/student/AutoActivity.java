package com.jswb.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.UUID;

public class AutoActivity extends AppCompatActivity {

    boolean check;
    private TextView tv_context;
    private EditText et_number;
    private Button btn_send;
    private WebView webView;
    private TextView tv_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);

        tv_context = findViewById(R.id.tv_context);
        et_number = findViewById(R.id.et_number);
        btn_send = findViewById(R.id.btn_send);
        webView = findViewById(R.id.web);
        tv_number = findViewById(R.id.tv_number);

        et_number.setVisibility(View.INVISIBLE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient()); //不調用系統瀏覽器
        webView.loadUrl("https://bin.webduino.io/dodax/1/edit?");


        et_number.setVisibility(View.INVISIBLE);
        btn_send.setVisibility(View.INVISIBLE);

        tv_context.setText("Ready");

        final Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myVibrator.vibrate(1000);
                final String card = dataSnapshot.getKey();
                if (!card.equals("Record")) {
                    tv_context.setText(card);
                    et_number.setVisibility(View.VISIBLE);
                    btn_send.setVisibility(View.VISIBLE);
                    btn_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            database.child(card).child("座號").setValue(et_number.getText().toString());

                            InputMethodManager inputManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);

                    assert inputManager != null;
                    inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    tv_context.setText("Ready");
                    et_number.setText("");
                    et_number.setVisibility(View.INVISIBLE);
                    btn_send.setVisibility(View.INVISIBLE);
                }
            });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myVibrator.vibrate(500);
                Toast.makeText(AutoActivity.this, dataSnapshot.child("座號").getValue().toString() + "到了", Toast.LENGTH_SHORT).show();
                if(!check) {
                    tv_number.setText(tv_number.getText().toString() + dataSnapshot.child("座號").getValue());
                    check = !check;
                }else
                    tv_number.setText(tv_number.getText().toString() + "," + dataSnapshot.child("座號").getValue());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
