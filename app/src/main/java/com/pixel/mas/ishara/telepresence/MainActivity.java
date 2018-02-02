package com.pixel.mas.ishara.telepresence;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;



public class MainActivity extends AppCompatActivity {
    Button btn_discon;
    Button btn_connect;
    Button btn_sendmsg;
    ListView listView;
    EditText et_cmd;
    OutputStream outputStream;
    InputStream inputStream;

    //sensor


    private BluetoothAdapter blueadapt;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothSocket socket;
    ArrayAdapter<String> adapter;
    ArrayList addrlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_connect = (Button) findViewById(R.id.btn_bluetooth);
        btn_sendmsg = (Button) findViewById(R.id.btn_msg);
        btn_discon = (Button) findViewById(R.id.btn_discon);
        listView = (ListView) findViewById(R.id.blu_list);
        et_cmd = (EditText) findViewById(R.id.et_cmd);


        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initBlue();



            }
        });
        btn_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(outputStream != null){
                    try{



//                        outputStream.write(et_cmd.getText().toString().getBytes());
//                        outputStream.flush();

                    }catch (Exception ex){
                        Toast.makeText(getApplicationContext(), "[Error]"+ex,Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "[Error]:Bro you need to connect first",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_discon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    socket.close();
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "[Error]:Can't close the socket connection",Toast.LENGTH_SHORT).show();
                }


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String address = (String)addrlist.get(i);
                Intent mServiceIntent = new Intent(MainActivity.this, BluetoothService.class);
                mServiceIntent.putExtra("address", address);

                //mServiceIntent.setData(Uri.parse(address));
                startService(mServiceIntent);
                try{







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

                }
                //BluetoothDevice btDevice = blueadapt.getRemoteDevice(address);

            }
        });


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


}
