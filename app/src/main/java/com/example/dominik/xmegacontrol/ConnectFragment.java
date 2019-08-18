package com.example.dominik.xmegacontrol;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ConnectFragment extends Fragment {

    private TextView BluetoothStatus, ReadBuffer;
    public static Button OnOffBluetoothButton, ShowPairedDevicesButton, DiscoverNewDevicesButton;
    private ListView DevicesListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            BluetoothStatus = (TextView)view.findViewById(R.id.BluetoothStatus);
            OnOffBluetoothButton = (Button)view.findViewById(R.id.OnOffBluetooth);
            ShowPairedDevicesButton = (Button)view.findViewById(R.id.ShowPairedDevices);
            DiscoverNewDevicesButton = (Button)view.findViewById(R.id.DiscoverNewDevices);

            if(((MainActivity)getActivity()).GetBTStatus())
                OnOffBluetoothButton.setText(R.string.OffBluetooth);
            else
                OnOffBluetoothButton.setText(R.string.OnBluetooth);

            DevicesListView = (ListView)view.findViewById(R.id.DevicesListView);
            DevicesListView.setAdapter(((MainActivity)getActivity()).mBTArrayAdapter); // assign model to view
            DevicesListView.setOnItemClickListener(DevicesListClickListener);

            OnOffBluetoothButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( ((MainActivity) getActivity()).GetBTStatus()) {
                        ((MainActivity) getActivity()).OffBluetooth();
                    }
                    else{
                        ((MainActivity)getActivity()).OnBluetooth();
                    }
                }
            });


            ShowPairedDevicesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).ShowPairedDevices();
                }
            });

            DiscoverNewDevicesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).DiscoverNewDevices();
                }
            });
        }
    }
    private AdapterView.OnItemClickListener DevicesListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            ((MainActivity)getActivity()).DevicesClickListener(av,v,arg2,arg3);
        }
    };
    public void setBluetoothStatus(String text){
        BluetoothStatus.setText(text);
    }
}
