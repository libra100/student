package com.jswb.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
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

        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            init();
        }
        final Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (int i = 0; i < devices.size(); i++) {
            BluetoothDevice device = devices.iterator().next();
            System.out.println(device.getName());
        }

        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 設定藍芽可見性，最多300秒
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String name;
                int connectState;
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // 獲取查詢到的藍芽裝置
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    System.out.println(device.getName());
                    // 如果查詢到的裝置符合要連線的裝置，處理
                    name = device.getName();
                    if (device.getName().equalsIgnoreCase(name)) {
                        // 搜尋藍芽裝置的過程佔用資源比較多，一旦找到需要連線的裝置後需要及時關閉搜尋
                        adapter.cancelDiscovery();
                        // 獲取藍芽裝置的連線狀態
                        connectState = device.getBondState();
                        switch (connectState) {
                            // 未配對
                            case BluetoothDevice.BOND_NONE:
                                // 配對
                                try {
                                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                                    createBondMethod.invoke(device);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            // 已配對
                            case BluetoothDevice.BOND_BONDED:
                                try {
                                    // 連線
                                    connect(device);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                    // 狀態改變的廣播
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    name = device.getName();
                    if (device.getName().equalsIgnoreCase(name)) {
                        connectState = device.getBondState();
                        switch (connectState) {
                            case BluetoothDevice.BOND_NONE:
                                break;
                            case BluetoothDevice.BOND_BONDING:
                                break;
                            case BluetoothDevice.BOND_BONDED:
                                try {
                                    // 連線
                                    connect(device);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, intentFilter);
        adapter.startDiscovery();
    }

    protected void init() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient()); //不調用系統瀏覽器
        webView.loadUrl("https://bin.webduino.io/coqam/1");


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
                if (!check) {
                    tv_number.setText(tv_number.getText().toString() + dataSnapshot.child("座號").getValue());
                    check = !check;
                } else
                    tv_number.setText(tv_number.getText().toString() + "," + dataSnapshot.child("座號").getValue());
                MediaPlayer mediaPlayer01;
                mediaPlayer01 = MediaPlayer.create(AutoActivity.this, R.raw.celtic_fantasy);
                mediaPlayer01.start();
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



    private void connect(BluetoothDevice device) throws IOException {
        // 固定的UUID
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
        socket.connect();
    }
}