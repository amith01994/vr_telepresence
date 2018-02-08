package com.pixel.mas.ishara.telepresence;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ishara on 2/7/18.
 */

public class Accelometer extends Service implements SensorEventListener {
    Sensor acceSensor;
    SensorManager mSensorManager;
    Intent broadcast_intent;
    float ax,ay,az;
    String MY_ACTION_ACCL = "ACCEL_DATA";
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        ax =  sensorEvent.values[0] ;
        ay =  sensorEvent.values[1] ;
        az =  sensorEvent.values[2] ;
        broadcast_intent.setAction(MY_ACTION_ACCL);
        broadcast_intent.putExtra("DATAPASSEDAX", "" + ax);
        broadcast_intent.putExtra("DATAPASSEDAY", "" + ay);
        broadcast_intent.putExtra("DATAPASSEDAZ", "" + az);
        sendBroadcast(broadcast_intent);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acceSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, acceSensor, SensorManager.SENSOR_DELAY_NORMAL);
        broadcast_intent = new Intent();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
