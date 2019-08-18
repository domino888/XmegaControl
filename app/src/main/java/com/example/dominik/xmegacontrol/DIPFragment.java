package com.example.dominik.xmegacontrol;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DIPFragment extends Fragment {

    private Button ReadDipSwitchButton;
    private TextView DipSwitchStatusHex, DipSwitchStatusBin;

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dip, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            ReadDipSwitchButton = (Button) view.findViewById(R.id.ReadDipSwitch);
            DipSwitchStatusHex = (TextView) view.findViewById(R.id.DipSwitchStatusHex);
            DipSwitchStatusBin = (TextView) view.findViewById(R.id.DipSwitchStatusBin);

            ReadDipSwitchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).commandSend("DIP", 5);
                }
            });
        }
    }
    public void setReadDipSwitchStatus(String text){
        String convertTextBin = convertToBin(text);
        String convertTextHex = convertToHex(text);
        DipSwitchStatusBin.setText("  " + convertTextBin);
        DipSwitchStatusHex.setText("  0x" + convertTextHex);
    }

    private  String convertToBin(String text){
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
