package com.example.dominik.xmegacontrol;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    public ArrayAdapter<String> mBTArrayAdapter;
    public  int fragment_number=7;

    private final String TAG = MainActivity.class.getSimpleName();
    private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

    TopFragment top_fragment = new TopFragment();
    ConnectFragment connect_fragment = new ConnectFragment();
    LEDFragment led_fragment = new LEDFragment();
    LCDFragment lcd_fragment = new LCDFragment();
    SEGFragment seg_fragment = new SEGFragment();
    DIPFragment dip_fragment = new DIPFragment();
    POTFragment pot_fragment = new POTFragment();
    BUTFragment but_fragment = new BUTFragment();

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // dołączamy adapter SectionsPagerAdapter do kontrolki ViewPager
        FragmentPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        // Ask for location permission if not already allowed
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //select fragment and send received message
                    SelectReceiver(readMessage);
                }
                if(msg.what == CONNECTING_STATUS){
                    if(msg.arg1 == 1) {
                        setFragmentNumberToCurrentFragment();
                        SelectReceiver("Połączone z : " + (String) (msg.obj));
                    }
                    else{
                        setFragmentNumberToCurrentFragment();
                        SelectReceiver("Połączenie nieudane");
                    }
                }
            }
        };

        if (mBTArrayAdapter == null) {
            setFragmentNumberToCurrentFragment();
            SelectReceiver("Bluetooth nie znalezione");
            Toast.makeText(getApplicationContext(),"Urządzenie Bluetooth nie znalezione!",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onRestart(){
        super.onRestart();
    };
    @Override
    protected void onResume(){
        super.onResume();
    };
    @Override
    protected void onPause(){
        super.onPause();
    };
    @Override
    protected void onStop(){
        super.onStop();
    };
    @Override
    protected void onDestroy(){
        super.onDestroy();
    };




    private void setFragmentNumberToCurrentFragment(){ fragment_number = 1; }       //ustawianie numeru fragmentu na ConnectFragment
    private void setFragmentNumberToDefaultReceiver(){ fragment_number = 7; }       //ustawianie numeru fragmentu na ButtonFragment

    public void commandSend(String command, int frag_number){
        fragment_number = frag_number;
        if(mConnectedThread != null) {//First check to make sure thread created
            command += '\n';
            mConnectedThread.write(command);
        }
        else {
            Toast.makeText(getApplicationContext(),"Nie połączono z urządzeniem!",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void SelectReceiver(String ReceivedMessage){
        switch (fragment_number) {
            case 1: connect_fragment.setBluetoothStatus(ReceivedMessage); setFragmentNumberToDefaultReceiver(); break;
            case 2: led_fragment.setLedStatus(ReceivedMessage); setFragmentNumberToDefaultReceiver(); break;
            case 3: lcd_fragment.setLcdStatus(ReceivedMessage); setFragmentNumberToDefaultReceiver(); break;
            case 4: seg_fragment.setSegmentStatus(ReceivedMessage); setFragmentNumberToDefaultReceiver(); break;
            case 5: dip_fragment.setReadDipSwitchStatus(ReceivedMessage); setFragmentNumberToDefaultReceiver(); break;
            case 6: pot_fragment.setPotentiometerStatus(ReceivedMessage); setFragmentNumberToDefaultReceiver(); break;
            case 7: but_fragment.setPressedButtonStatus(ReceivedMessage); setFragmentNumberToDefaultReceiver(); break;
        }
    }
    public boolean GetBTStatus() {
        if (mBTAdapter.isEnabled())
            return true;
        else
            return false;
    }

    // Enter here after user selects "yes" or "no" to enabling radio
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK){
                setFragmentNumberToCurrentFragment();
                SelectReceiver("Włączone");
            }
            else{
                setFragmentNumberToCurrentFragment();
                SelectReceiver("Wyłączone");
                ConnectFragment.OnOffBluetoothButton.setText(R.string.OnBluetooth);
            }
        }
    }

    public void OnBluetooth() {
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            setFragmentNumberToCurrentFragment();
            SelectReceiver("Bluetooth włączone");
            Toast.makeText(getApplicationContext(),"Bluetooth włączone",Toast.LENGTH_SHORT).show();
            ConnectFragment.OnOffBluetoothButton.setText(R.string.OffBluetooth);
        }
        else
            Toast.makeText(getApplicationContext(),"Bluetooth jest już włączone", Toast.LENGTH_SHORT).show();
    }

    public void OffBluetooth() {
        mBTAdapter.disable(); // turn off
        setFragmentNumberToCurrentFragment();
        SelectReceiver("Bluetooth wyłączone");
        Toast.makeText(getApplicationContext(),"Bluetooth wyłączone", Toast.LENGTH_SHORT).show();
        ConnectFragment.OnOffBluetoothButton.setText(R.string.OnBluetooth);
    }

    public void ShowPairedDevices() {
        mPairedDevices = mBTAdapter.getBondedDevices();
        if(mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            mBTArrayAdapter.clear(); // clear items
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            Toast.makeText(getApplicationContext(), "Pokaż sparowane urządzenia", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "Bluetooth nie jest włączone", Toast.LENGTH_SHORT).show();
    }

    public void DiscoverNewDevices() {
        // Check if the device is already discovering
        if(mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(),"Wyszukiwanie zatrzymane",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBTAdapter.isEnabled()) {
                mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery();
                Toast.makeText(getApplicationContext(), "Wyszukiwanie rozpoczęte", Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth nie jest włączone", Toast.LENGTH_SHORT).show();
            }
        }
    }
    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    //Funkcja wykonywana gdy kliniemy element (urzadzenie do sparowania) w List Listener w ConnectFragment
    public void DevicesClickListener(AdapterView<?> av, View v, int arg2, long arg3){

        if(!mBTAdapter.isEnabled()) {
            Toast.makeText(getBaseContext(), "Bluetooth nie jest włączone", Toast.LENGTH_SHORT).show();
            return;
        }
        setFragmentNumberToCurrentFragment();
        SelectReceiver("Łączenie...");
        // Get the device MAC address, which is the last 17 chars in the View
        String info = ((TextView) v).getText().toString();
        final String address = info.substring(info.length() - 17);
        final String name = info.substring(0,info.length() - 17);

        // Spawn a new thread to avoid blocking the GUI one
        new Thread()
        {
            public void run() {
                boolean fail = false;
                BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
                try {
                    mBTSocket = createBluetoothSocket(device);
                } catch (IOException e) {
                    fail = true;
                    Toast.makeText(getBaseContext(), "Nieudane utworzenie Socket", Toast.LENGTH_SHORT).show();
                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket.connect();
                } catch (IOException e) {
                    try {
                        fail = true;
                        mBTSocket.close();
                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                    } catch (IOException e2) {
                        //insert code to deal with this
                        Toast.makeText(getBaseContext(), "Nieudane utworzenie Socket\"", Toast.LENGTH_SHORT).show();
                    }
                }
                if(fail == false) {
                    mConnectedThread = new ConnectedThread(mBTSocket);
                    mConnectedThread.start();
                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name).sendToTarget();
                }
            }
        }.start();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception e) {
            Log.e(TAG, "Nie można stworzyć połączenia Insecure RFComm",e);
        }
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return top_fragment;
                case 1: return connect_fragment;
                case 2: return led_fragment;
                case 3: return lcd_fragment;
                case 4: return seg_fragment;
                case 5: return dip_fragment;
                case 6: return pot_fragment;
                case 7: return but_fragment;
            }
            return null;
        }
    }
}

