package com.example.uer.trabajogradofittness.Bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.example.uer.trabajogradofittness.RegistroEntreno.Inicio;

import java.util.List;
import java.util.UUID;

/**
 * This thread to the connection with the bluetooth device
 * @author Marco
 *
 */
@SuppressLint("NewApi")
public class H7ConnectThread  extends Thread{

    Inicio ac;
    private BluetoothGatt gat; //gat server
    private final String HRUUID = "0000180D-0000-1000-8000-00805F9B34FB";
    static BluetoothGattDescriptor descriptor;
    static BluetoothGattCharacteristic cc;

    public H7ConnectThread(BluetoothDevice device, Inicio ac) {
        Log.i("H7ConnectThread", "Iniciando lectura de datos!");
        this.ac=ac;
        gat = device.connectGatt(ac.getContext(), false, btleGattCallback); // Connect to the device and store the server (gatt)
    }


    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        gat.setCharacteristicNotification(cc,false);
        descriptor.setValue( BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        gat.writeDescriptor(descriptor);

        gat.disconnect();
        gat.close();
        Log.i("H7ConnectThread", "Closing HRsensor");
    }


    //Callback from the bluetooth
    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        //Called everytime sensor send data
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            int bmp = data[1] & 0xFF; // To unsign the value
            DataHandler.getInstance().cleanInput(bmp);
            Log.v("H7ConnectThread", "Pulsaciones por minuto: "+bmp);
        }

        //called on the successful connection
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            if (newState ==  BluetoothGatt.STATE_DISCONNECTED)
            {
                Log.e("H7ConnectThread", "Dispositivo desconectado");
                ac.connectionError();
            }
            else{
                gatt.discoverServices();
                Log.d("H7ConnectThread", "Connected and discovering services");
            }
        }

        //Called when services are discovered.
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            BluetoothGattService service = gatt.getService(UUID.fromString(HRUUID)); // Return the HR service
            //BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB"));
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics(); //Get the hart rate value
            for (BluetoothGattCharacteristic cc : characteristics)
            {
                for (BluetoothGattDescriptor descriptor : cc.getDescriptors()) {
                    //find descriptor UUID that matches Client Characteristic Configuration (0x2902)
                    // and then call setValue on that descriptor

                    //Those two line set the value for the disconnection
                    H7ConnectThread.descriptor=descriptor;
                    H7ConnectThread.cc=cc;

                    gatt.setCharacteristicNotification(cc,true);//Register to updates
                    descriptor.setValue( BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(descriptor);
                    Log.d("H7ConnectThread", "Connected and regisering to info");
                }
            }
        }
    };
}
