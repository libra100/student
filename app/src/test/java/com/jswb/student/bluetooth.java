package com.jswb.student;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
/**
 * @author Ken
 * @date 2016年4月20日 下午3:33:35
 */
public class BlueToothTestActivity extends Activity {
    //該UUID表示串列埠服務
    static final String SPP_UUID = "00000000-0000-1000-8000-00805F9B34FB";
    Button btnSearch, btnDis, btnExit,spilt;
    ToggleButton tbtnSwitch;
    ListView lvBTDevices;
    ArrayAdapter adtDevices;
    List lstDevices = new ArrayList ();
    BluetoothAdapter btAdapt;
    public static BluetoothSocket btSocket;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
// }
        setContentView(R.layout.activity_main);
// Button 設定
        btnSearch = (Button) this.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new ClickEvent());
        btnExit = (Button) this.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ClickEvent());
        btnDis = (Button) this.findViewById(R.id.btnDis);
        btnDis.setOnClickListener(new ClickEvent());
// // ToogleButton設定
        tbtnSwitch = (ToggleButton) this.findViewById(R.id.tbtnSwitch);
        tbtnSwitch.setOnClickListener(new ClickEvent());
        spilt = (Button)this.findViewById(R.id.spilt);
        spilt.setOnClickListener(new ClickEvent());
// // ListView及其資料來源 介面卡
        lvBTDevices = (ListView) this.findViewById(R.id.lvDevices);
        adtDevices = new ArrayAdapter (this,
                android.R.layout.simple_list_item_1, lstDevices);
        lvBTDevices.setAdapter(adtDevices);
        lvBTDevices.setOnItemClickListener(new ItemClickEvent());
        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本機藍芽功能
        if (btAdapt.isEnabled()) {
            tbtnSwitch.setChecked(false);
        } else {
            tbtnSwitch.setChecked(true);
        }
// ============================================================
// 註冊Receiver來獲取藍芽裝置相關的結果
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver來取得搜尋結果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(searchDevices, intent);
    }
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();
// 顯示所有收到的訊息及其細節
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e(keyName, String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device = null;
// 搜尋裝置時,取得裝置的MAC地址
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    String str = "未配對|" + device.getName() + "|"
                            + device.getAddress();
                    if (lstDevices.indexOf(str) == -1)// 防止重複新增
                        lstDevices.add(str); // 獲取裝置名稱和mac地址
                    adtDevices.notifyDataSetChanged();
                }
            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BlueToothTestActivity", "正在配對......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BlueToothTestActivity", "完成配對");
                        connect(device);//連線裝置
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BlueToothTestActivity", "取消配對");
                    default:
                        break;
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        this.unregisterReceiver(searchDevices);
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    class ItemClickEvent implements AdapterView.OnItemClickListener {
        @SuppressLint("NewApi") @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            if(btAdapt.isDiscovering())btAdapt.cancelDiscovery();
            String str = (String) lstDevices.get(arg2);
            String[] values = str.split("//|");
            String address = values[2];
            Log.e("BlueToothTestActivity", "address :"+values[2]);
            BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
            try {
                Boolean returnValue = false;
                if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                    btDev.createBond();
                }else if(btDev.getBondState() == BluetoothDevice.BOND_BONDED){
                    connect(btDev);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void connect(BluetoothDevice btDev) {
        UUID uuid = UUID.fromString(SPP_UUID);
        try {
// btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
            btSocket =(BluetoothSocket) btDev.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(btDev,1);
            Log.d("BlueToothTestActivity", "開始連線...");
            btSocket.connect();
        } catch (IOException e) {
// TODO Auto-generated catch block
            Log.e("BlueToothTestActivity", "開始連線......連結失敗");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnSearch)// 搜尋藍芽裝置,在BroadcastReceiver顯示結果
            {
                if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// 如果藍芽還沒開啟
                    Toast.makeText(BlueToothTestActivity.this, "請先開啟藍芽", 1000)
                            .show();
                    return;
                }
                if (btAdapt.isDiscovering())
                    btAdapt.cancelDiscovery();
                lstDevices.clear();
                Object[] lstDevice = btAdapt.getBondedDevices().toArray();
                for (int i = 0; i < lstDevice.length; i++) {
                    BluetoothDevice device = (BluetoothDevice) lstDevice[i];
                    String str = "已配對|" + device.getName() + "|"
                            + device.getAddress();
                    lstDevices.add(str); // 獲取裝置名稱和mac地址
                    adtDevices.notifyDataSetChanged();
                }
                setTitle("本機藍芽地址:" + btAdapt.getAddress());
                btAdapt.startDiscovery();
            } else if (v == tbtnSwitch) {// 本機藍芽啟動/關閉
                if (tbtnSwitch.isChecked() == false)
                    btAdapt.enable();
                else if (tbtnSwitch.isChecked() == true)
                    btAdapt.disable();
            } else if (v == btnDis)// 本機可以被搜尋
            {
                Intent discoverableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
            } else if (v == btnExit) {
                try {
                    if (btSocket != null)
                        btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BlueToothTestActivity.this.finish();
            }
        }
    }
    private void autoConnect(String address){
        if(btAdapt.isDiscovering())btAdapt.cancelDiscovery();
        BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
        try {
            if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                btDev.createBond();
            }else if(btDev.getBondState() == BluetoothDevice.BOND_BONDED){
                connect(btDev);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}