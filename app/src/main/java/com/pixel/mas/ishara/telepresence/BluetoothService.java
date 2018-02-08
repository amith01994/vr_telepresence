package com.pixel.mas.ishara.telepresence;

import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by ishara on 1/22/18.
 */

public class BluetoothService extends Service implements SensorEventListener{
    OutputStream outputStream;
    InputStream inputStream;
    Intent intent;
    Intent broadcase_intent;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor acceSensor;
    String address = null;
    BluetoothService.sensorReceive sensorreceive;

    Float x = (float)0;
    Float y= (float)0;
    Float z= (float)0;
    Float zaccel= (float)0;



    private BluetoothAdapter blueadapt;

    private BluetoothSocket socket;
    final static String MY_ACTION = "VALUE_UPDATE";
    final static String MY_ACTION_ACCL = "ACCEL_DATA";

    public BluetoothService() {
        super();
        Log.d("INTENDERROR","Starting Intent1");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;



        Log.d("INTENDERROR0","Starting Intent");
        blueadapt = BluetoothAdapter.getDefaultAdapter();

        this.address = intent.getStringExtra("bluetooth_address");

        try{
            if(address != null){
                BluetoothDevice btDevice = blueadapt.getRemoteDevice(address);
                Toast.makeText(getApplicationContext(), "Connecting to Bluetooth address:"+address,Toast.LENGTH_SHORT).show();
                this.socket = btDevice.createRfcommSocketToServiceRecord((btDevice.getUuids()[0]).getUuid());
                this.outputStream = socket.getOutputStream();
                this.inputStream = socket.getInputStream();

            }else{
                Toast.makeText(getApplicationContext(), "[-]Address Value:"+address,Toast.LENGTH_SHORT).show();
                this.stopSelf();

            }
            if(outputStream == null){
                this.stopSelf();
                Toast.makeText(getApplicationContext(), "Bluetooth connection terminated",Toast.LENGTH_SHORT).show();
            }

        }catch (IOException ex){
            Toast.makeText(getApplicationContext(), "[-]Error:"+address,Toast.LENGTH_SHORT).show();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "[-]Error:"+address,Toast.LENGTH_SHORT).show();
        }


        sensorreceive = new BluetoothService.sensorReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.MY_ACTION_ACCL);
        registerReceiver(sensorreceive, intentFilter);




        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(this, acceSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Intent accelo_service = new Intent(getApplicationContext(), Accelometer.class);
        startService(accelo_service);

        return super.onStartCommand(intent, flags, startId);


    }

    public BluetoothService(String address) {
        super();
        Log.d("INTENDERROR","Starting Intent2");


        this.address = address;




    }


    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //acceSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        broadcase_intent = new Intent();



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("INTENDERROR","Starting Intentbinder");



        return null;
    }



    public void write(String s) throws IOException {
        if(this.outputStream != null && socket.isConnected()){
            this.outputStream.write(s.getBytes());
            this.outputStream.flush();
        }else {
            socket.connect();
        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        Sensor source = sensorEvent.sensor;
        if(source.getType() == Sensor.TYPE_ORIENTATION){
            x =  sensorEvent.values[0] ;
            y =  sensorEvent.values[1] ;
            z =  sensorEvent.values[2] ;
            broadcase_intent.setAction(MY_ACTION);
            broadcase_intent.putExtra("DATAPASSEDX", ""+x);
            broadcase_intent.putExtra("DATAPASSEDY", ""+y);
            broadcase_intent.putExtra("DATAPASSEDZ", ""+z);
            sendBroadcast(broadcase_intent);


        }
//        if(source.getType() == Sensor.TYPE_ACCELEROMETER){
//           zaccel =  sensorEvent.values[2] ;
//            broadcase_intent.putExtra("DATAPASSEDX", ""+x);
//            broadcase_intent.putExtra("DATAPASSEDY", ""+y);
//            broadcase_intent.putExtra("DATAPASSEDZ", ""+z);
//
//        }










        Log.d("INTENDERROR","Starting IntentSensorChange");
        Log.d("SENSOR0","" + x);
        Log.d("SENSOR1","" + y);
        Log.d("SENSOR2","" + z);
        Log.d("SENSORACCEL","" + zaccel);






        try{
            write(x + "z"+ z + "z" + zaccel);
            Log.d("BLUETOOTHOUT","" + x + "z"+ z + "z" + zaccel);

        }catch (IOException ex){
            Log.d("[-]OUTPUTX",ex.toString());
            //Toast.makeText(getApplicationContext(), "[-]Error:"+ex,Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Log.d("[-]OUTPUTX",ex.toString());
            //Toast.makeText(getApplicationContext(), "[-]Error"+ex,Toast.LENGTH_SHORT).show();
        }






    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d("INTENDERROR","Starting Accuracy");


    }
    private class sensorReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            String datapassedax = arg1.getStringExtra("DATAPASSEDAX");
            String datapasseday = arg1.getStringExtra("DATAPASSEDAY");
            String datapassedaz = arg1.getStringExtra("DATAPASSEDAZ");
            zaccel = Float.valueOf(datapassedaz);


//            String datapassedx = "" + arg1.getIntExtra("DATAPASSEDX", 0);
//            String datapassedy = "" + arg1.getIntExtra("DATAPASSEDY", 0);
//            String datapassedz = "" + arg1.getIntExtra("DATAPASSEDZ", 0);
            Log.d("VALUERECACCEL","X:" + datapassedax + ",Y:" + datapasseday+",Z:" + datapassedaz);




        }

    }
}
