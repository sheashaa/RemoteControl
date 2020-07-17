package wtf.mshea.remotecontrol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button turnOnButton;
    Button turnOffButton;
    EditText macAddrEditText;

    BluetoothConnection bluetoothConnection;
    boolean isConnected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turnOnButton = findViewById(R.id.turn_on_btn);
        turnOffButton = findViewById(R.id.turn_off_btn);
        macAddrEditText = findViewById(R.id.mac_edit_txt);

        turnOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBluetooth();

                if (isConnected) {
                    bluetoothConnection.write("0");
                    Toast.makeText(MainActivity.this, "Turning OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        turnOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBluetooth();

                if (isConnected) {
                    bluetoothConnection.write("1");
                    Toast.makeText(MainActivity.this, "Turning OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    void initBluetooth() {
        String deviceAddress = macAddrEditText.getText().toString().trim();
        Log.d("TEST", deviceAddress);
        if (deviceAddress != null) {
            bluetoothConnection = new BluetoothConnection(deviceAddress);
            isConnected = bluetoothConnection.open();
            if (!isConnected)
                Toast.makeText(this, "Not Connected. Try Again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something wrong with MAC address", Toast.LENGTH_SHORT).show();
        }
    }
}