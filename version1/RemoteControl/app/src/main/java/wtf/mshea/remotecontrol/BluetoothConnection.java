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
                Log.w(LOG_TAG, "Bluetooth close failed: " + bluetoothAddress, exception);
            }

            bluetoothSocket = null;
        }
    }

    public boolean open(boolean secure) {
        try {
            bluetoothSocket = secure? bluetoothDevice.createRfcommSocketToServiceRecord(SERIAL_PROFILE_UUID):
                    bluetoothDevice.createInsecureRfcommSocketToServiceRecord(SERIAL_PROFILE_UUID);
            bluetoothSocket.connect();

            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException openException) {
            Log.e(LOG_TAG, "Bluetooth connect failed: " + bluetoothAddress + ": " + openException.getMessage());

            if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                    Log.e(LOG_TAG, "Bluetooth socket close error: " + bluetoothAddress, closeException);
                }

                bluetoothSocket = null;
                outputStream = null;
            }

            return false;
        }

        return true;
    }

    public boolean write (byte[] bytes) {
        try {
            outputStream.write(bytes);
            outputStream.flush();
            return true;
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Bluetooth write failed: " + bluetoothAddress, exception);
        }

        return false;
    }
}