package wtf.mshea.remotecontrol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import android.os.Build;
import android.util.Log;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothConnection {
    private static final String LOG_TAG = BluetoothConnection.class.getName();

    protected static final UUID SERIAL_PROFILE_UUID = UUID.fromString(
            "00001101-0000-1000-8000-00805F9B34FB"
    );

    protected static final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    protected final BluetoothDevice bluetoothDevice;
    protected final String bluetoothAddress;

    protected BluetoothSocket bluetoothSocket = null;
    protected OutputStream outputStream = null;

    public static BluetoothDevice getDevice(String deviceAddress) {
        return bluetoothAdapter.getRemoteDevice(deviceAddress);
    }

    public static String getName(String deviceAddress) {
        return getDevice(deviceAddress).getName();
    }

    public BluetoothConnection(String deviceAddress) {
        bluetoothDevice = getDevice(deviceAddress);
        bluetoothAddress = bluetoothDevice.getAddress();
    }

    public void close() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException exception) {
            }

            bluetoothSocket = null;
        }
    }

    public boolean open() {
        try {
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(SERIAL_PROFILE_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException openException) {

            if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                }

                bluetoothSocket = null;
                outputStream = null;
            }

            return false;
        }

        return true;
    }

    public void write (String msg) {
        try {
            outputStream.write(msg.getBytes());
            outputStream.flush();
        } catch (IOException exception) {
        }
    }
}