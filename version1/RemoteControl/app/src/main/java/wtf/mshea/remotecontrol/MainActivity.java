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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int BLUETOOTH_CONNECT_ACTIVITY_REQUEST_CODE = 10;


    FloatingActionButton bluetoothFab;
    Button switchButton;
    boolean isSwitchOn = false;

    BluetoothConnection bluetoothConnection;
    boolean isConnected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothFab = findViewById(R.id.btFab);
        switchButton = findViewById(R.id.switchButton);


        bluetoothFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBluetoothDeviceActivity();
            }
        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (bluetoothConnection == null) {
                startBluetoothDeviceActivity();
            } else {
                if (isConnected) {
                    if (isSwitchOn) {
                        bluetoothConnection.write("0".getBytes());
                        switchButton.setBackgroundColor(getResources().getColor(R.color.colorRed));
                        switchButton.setTextColor(getResources().getColor(R.color.colorWhite));
                        switchButton.setText(getResources().getText(R.string.button_off));
                        isSwitchOn = !isSwitchOn;
                    } else {
                        bluetoothConnection.write("1".getBytes());
                        switchButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        switchButton.setTextColor(getResources().getColor(R.color.colorBlack));
                        switchButton.setText(getResources().getText(R.string.button_on));
                        isSwitchOn = !isSwitchOn;


                    }
                } else {
                    isConnected = bluetoothConnection.open(false);
                }
            }
            }
        });

    }

    void startBluetoothDeviceActivity() {
        Intent intent = new Intent(MainActivity.this, BluetoothDeviceActivity.class);
        startActivityForResult(intent, BLUETOOTH_CONNECT_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_CONNECT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                String deviceAddress = data.getStringExtra("DeviceAddress");
                bluetoothConnection = new BluetoothConnection(deviceAddress);
                isConnected = bluetoothConnection.open(false);
            }
        }

    }
}