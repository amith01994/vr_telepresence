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
    private SensorManager mSensorManager;
    private Sensor mSensor;
    String address = "";

    private BluetoothAdapter blueadapt;

    private BluetoothSocket socket;

    public BluetoothService() {
        super();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);




    }
    public BluetoothService(String address) {
        super();

//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//        this.address = address;




    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("INTEND ERROR","Starting Intent");
        blueadapt = BluetoothAdapter.getDefaultAdapter();
        try{
            BluetoothDevice btDevice = blueadapt.getRemoteDevice(address);
            Toast.makeText(getApplicationContext(), "Connecting to Bluetooth address:"+address,Toast.LENGTH_SHORT).show();
            socket = btDevice.createRfcommSocketToServiceRecord((btDevice.getUuids()[0]).getUuid());
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        }catch (IOException ex){
            Toast.makeText(getApplicationContext(), "[-]Error:"+address,Toast.LENGTH_SHORT).show();

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {



        return null;
    }



    public void write(String s) throws IOException {

            outputStream.write(s.getBytes());
            outputStream.flush();


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        float azimuth_angle = sensorEvent.values[0];
        float pitch_angle = sensorEvent.values[1];
        float roll_angle = sensorEvent.values[2];
        try{
            write(Float.toString(azimuth_angle) + "," + Float.toString(pitch_angle) + "," + Float.toString(roll_angle));
        }catch (Exception ex){

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }
}
