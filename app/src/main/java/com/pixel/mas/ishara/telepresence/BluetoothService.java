package com.pixel.mas.ishara.telepresence;

import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
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
    String address = null;

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private BluetoothAdapter blueadapt;

    private BluetoothSocket socket;
    final static String MY_ACTION = "VALUE_UPDATE";

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
                socket = btDevice.createRfcommSocketToServiceRecord((btDevice.getUuids()[0]).getUuid());
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();

            }else{
                Toast.makeText(getApplicationContext(), "[-]Address Value:"+address,Toast.LENGTH_SHORT).show();

            }

        }catch (IOException ex){
            Toast.makeText(getApplicationContext(), "[-]Error:"+address,Toast.LENGTH_SHORT).show();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "[-]Error:"+address,Toast.LENGTH_SHORT).show();
        }



        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        broadcase_intent = new Intent();



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("INTENDERROR","Starting Intentbinder");



        return null;
    }



    public void write(String s) throws IOException {

            outputStream.write(s.getBytes());
            outputStream.flush();


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){

        Float x =  sensorEvent.values[0] * 180;
        Float y =  sensorEvent.values[1] * 180;
        Float z =  sensorEvent.values[2] * 180;
        Log.d("INTENDERROR","Starting IntentSensorChange");
        Log.d("SENSOR0","" + x);
        Log.d("SENSOR1","" + y);
        Log.d("SENSOR2","" + z);


        broadcase_intent.setAction(MY_ACTION);
        broadcase_intent.putExtra("DATAPASSEDX", ""+x);
        broadcase_intent.putExtra("DATAPASSEDY", ""+y);
        broadcase_intent.putExtra("DATAPASSEDZ", ""+z);
        sendBroadcast(broadcase_intent);

        try{
            write(x + "," + y + "," + z);
        }catch (IOException ex){
            Toast.makeText(getApplicationContext(), "[-]Error:"+ex,Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "[-]Error"+ex,Toast.LENGTH_SHORT).show();
        }





    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d("INTENDERROR","Starting Accuracy");


    }
}
