package com.pixel.mas.ishara.telepresence;

import android.Manifest;
import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Set;



public class MainActivity extends AppCompatActivity {
    Button btn_discon;
    Button btn_connect;
    Button btn_stream;
    TextView tv_x;
    TextView tv_y;
    TextView tv_z;

    @Override
    protected void onResume() {
        super.onResume();
    }

    Intent mServiceIntent;
    Intent stream_intent;
    sensorReceive sensorreceive;
    ListView listView;
    EditText eturl;

    OutputStream outputStream;
    InputStream inputStream;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mServiceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
//sensor


    private BluetoothAdapter blueadapt;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothSocket socket;
    ArrayAdapter<String> adapter;
    ArrayList addrlist;

    @Override
    protected void onStart() {
        super.onStart();
        sensorreceive = new sensorReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.MY_ACTION);
        registerReceiver(sensorreceive, intentFilter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{

        }
        requestPermission();
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btn_connect = (Button) findViewById(R.id.btn_bluetooth);
        btn_stream = (Button)findViewById(R.id.btn_stream);
        btn_discon = (Button) findViewById(R.id.btn_discon);
        listView = (ListView) findViewById(R.id.blu_list);
        eturl = (EditText)findViewById(R.id.eturl);
        tv_x = (TextView) findViewById(R.id.x_view);
        tv_y = (TextView) findViewById(R.id.y_view);
        tv_z = (TextView) findViewById(R.id.z_view);

        btn_stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stream_intent = new Intent(MainActivity.this,Stream.class);
                stream_intent.putExtra("URL_PASS","http://" + eturl.getText().toString());
                startActivity(stream_intent);


            }
        });




        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBlue();



            }
        });

        btn_discon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    socket.close();
                    if(mServiceIntent != null){
                        stopService(mServiceIntent);
                    }
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "[Error]:Can't close the socket connection",Toast.LENGTH_SHORT).show();
                }


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    String address = (String)addrlist.get(i);

                    mServiceIntent = new Intent(MainActivity.this, BluetoothService.class);
                    mServiceIntent.putExtra("bluetooth_address", address);

                    //mServiceIntent.setData(Uri.parse(address));
                    startService(mServiceIntent);
//                    BluetoothDevice btDevice = blueadapt.getRemoteDevice(address);
//                    Toast.makeText(getApplicationContext(), "Connecting to Bluetooth address:"+address,Toast.LENGTH_SHORT).show();
//                    socket = btDevice.createRfcommSocketToServiceRecord((btDevice.getUuids()[0]).getUuid());
//                    socket.connect();
//                    outputStream = socket.getOutputStream();
//                    inputStream = socket.getInputStream();
                    //outputStream.write("10,20,30".getBytes());
                    //BluetoothService blue_background = new BluetoothService(outputStream, inputStream);
                }catch (Exception e){
                    Log.d("INTEND ERROR",e.toString());
                    Toast.makeText(getApplicationContext(), "[Error]"+e,Toast.LENGTH_SHORT).show();
                    Log.d("CRASH","[Error]"+e);

                }
                //BluetoothDevice btDevice = blueadapt.getRemoteDevice(address);

            }
        });


    }

    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.BLUETOOTH)) {

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH},
                        1);

            }
        }
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.BODY_SENSORS)) {

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.BODY_SENSORS},
                        1);

            }
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.INTERNET)) {

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET},
                        1);

            }
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.BLUETOOTH_ADMIN)) {

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        1);

            }
        }
    }

    private void initBlue(){
        blueadapt = BluetoothAdapter.getDefaultAdapter();
        Intent turnon = new Intent(blueadapt.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnon, 0);

        ArrayList list = new ArrayList();
        addrlist = new ArrayList();


        pairedDevices = blueadapt.getBondedDevices();
        if(pairedDevices.size() > 0){
            Toast.makeText(getApplicationContext(), "[+]Updating with device List!",Toast.LENGTH_SHORT).show();
            for(BluetoothDevice bt : pairedDevices){
                list.add(bt.getName());
                addrlist.add(bt.getAddress());

            }
            adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

            listView.setAdapter(adapter);

        }else{
            Toast.makeText(getApplicationContext(), "[-]No Paired devices Found!",Toast.LENGTH_SHORT).show();

        }

    }
    private class sensorReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            String datapassedx = arg1.getStringExtra("DATAPASSEDX");
            String datapassedy = arg1.getStringExtra("DATAPASSEDY");
            String datapassedz = arg1.getStringExtra("DATAPASSEDZ");


//            String datapassedx = "" + arg1.getIntExtra("DATAPASSEDX", 0);
//            String datapassedy = "" + arg1.getIntExtra("DATAPASSEDY", 0);
//            String datapassedz = "" + arg1.getIntExtra("DATAPASSEDZ", 0);
            Log.d("VALUEREC","X:" + datapassedx + ",Y:" + datapassedy+",Z:" + datapassedz);
            tv_x.setText("X:" + datapassedx);
            tv_y.setText("Y:" + datapassedy);
            tv_z.setText("Z:" + datapassedz);



        }

    }


}
