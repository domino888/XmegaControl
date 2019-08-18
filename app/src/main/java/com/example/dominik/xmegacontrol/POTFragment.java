package com.example.dominik.xmegacontrol;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class POTFragment extends Fragment {

    private Button MeasurePotentiometer1Button, MeasurePotentiometer2Button;
    private TextView Potentiometer1StatusBin,Potentiometer1StatusHex ,Potentiometer2StatusBin, Potentiometer2StatusHex;
    private int currentPotentiometer;

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pot, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            MeasurePotentiometer1Button = (Button) view.findViewById(R.id.MeasurePotentiometer1);
            MeasurePotentiometer2Button = (Button) view.findViewById(R.id.MeasurePotentiometer2);
            Potentiometer1StatusBin = (TextView) view.findViewById(R.id.Potentiometer1StatusBin);
            Potentiometer2StatusBin = (TextView) view.findViewById(R.id.Potentiometer2StatusBin);
            Potentiometer1StatusHex = (TextView) view.findViewById(R.id.Potentiometer1StatusHex);
            Potentiometer2StatusHex = (TextView) view.findViewById(R.id.Potentiometer2StatusHex);

            MeasurePotentiometer1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).commandSend("POT1", 6);
                    currentPotentiometer=1;
                }
            });
            MeasurePotentiometer2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).commandSend("POT2", 6);
                    currentPotentiometer=2;
                }
            });
        }
    }
    public void setPotentiometerStatus(String text){
        String convertTextBin = convertToBin(text);
        String convertTextHex = convertToHex(text);
        switch(currentPotentiometer) {
            case 1: Potentiometer1StatusBin.setText("  " + convertTextBin);
                Potentiometer1StatusHex.setText("  0x"+ convertTextHex); break;
            case 2: Potentiometer2StatusBin.setText("  " + convertTextBin);
                Potentiometer2StatusHex.setText("  0x"+ convertTextHex); break;
        }
    }

    private static String convertToBin(String text){
        byte[] bytes = text.getBytes();
        StringBuilder binary = new StringBuilder();
        int val = bytes[0];
        for (int i = 0; i < 8; i++)
        {
            binary.append((val & 128) == 0 ? 0 : 1);
            val <<= 1;
        }
        binary.append(' ');
        return binary.toString();
    }

    public static String convertToHex(String text) {
        byte[] bytes = text.getBytes();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String textHex = new String(hexChars);
        return  textHex.substring(0,2);
    }
}
