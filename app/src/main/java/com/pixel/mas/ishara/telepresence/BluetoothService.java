package com.pixel.mas.ishara.telepresence;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ishara on 1/22/18.
 */

public class BluetoothService extends Service {
    OutputStream outputStream;
    InputStream inputStream;
    public BluetoothService(OutputStream outputStream, InputStream inputStream){
        this.inputStream = inputStream;
        this.outputStream = outputStream;

    }
    public BluetoothService(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        try {
            write("10,30,60");
        }catch (Exception ex){
            Log.d("[-]Exception:",ex.toString());
        }

        return null;
    }

    public void write(String s) throws IOException {

            outputStream.write(s.getBytes());


    }
}
