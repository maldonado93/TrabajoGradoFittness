package com.example.uer.trabajogradofittness.Bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * This thread to the connection with the bluetooth device
 * @author Marco
 *
 */
public class ConnectThread extends Thread {

    BluetoothAdapter mBluetoothAdapter;
    private final BluetoothSocket mmSocket;
    Main2 ac;

    public ConnectThread(BluetoothDevice device, Main2 ac) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        Log.i("ConnectThread", "Starting connectThread");
        this.ac=ac;
        BluetoothSocket tmp = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException ignored) {
            Log.e("ConnectThread", "Error on getting the device");
        }
        mmSocket = tmp;

    }

    public void run() {

        Log.i("ConnectThread", "Starting the thread for connectThread");
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
        int ok =0;
        while(ok<2)//loop to try to connect
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                if(mmSocket.isConnected())
                    mmSocket.close();
                mmSocket.connect();
                ok=5;
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                if (ok==0)// try 2 times
                    ok++;
                else{
                    ac.connectionError();
                    Log.e("ConnectThread","Error with the BT stack " + connectException.toString());

                    try {
                        mmSocket.close();
                    } catch (IOException closeException) {
                        Log.e("ConnectThread", "Error on getting the stack");
                    }
                    return;
                }
            }

        // Do work to manage the connection (in a separate thread)
        while (true){ //reading loop
            try {
                DataHandler.getInstance().acqui(mmSocket.getInputStream().read()); //read value
            } catch (IOException e) {
                ac.connectionError();
                Log.e("ConnectThread","Error with the BT stack " + e.toString());

                try {
                    mmSocket.getInputStream().close();
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e("ConnectThread", "Error on getting the stack");
                }
                return;
            }
        }
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        Log.i("ConnectThread","Closing BT connection");
        try {
            if(mmSocket!=null && mmSocket.isConnected())
                mmSocket.close();
        } catch (IOException e) {
            Log.e("ConnectThread", "Error on closing bluetooth");
        }
    }
}