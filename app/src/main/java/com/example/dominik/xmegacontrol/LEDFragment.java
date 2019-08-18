package com.example.dominik.xmegacontrol;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LEDFragment extends Fragment {

    private Button LedOnButton, LedOffButton, SendLEDButton;
    private TextView LedStatus;
    private EditText CharToSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_led, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            CharToSend = (EditText)view.findViewById(R.id.CharToSend);
            LedStatus = (TextView)view.findViewById(R.id.LedStatus);
            LedOnButton = (Button) view.findViewById(R.id.LedON);
            LedOffButton = (Button) view.findViewById(R.id.LedOFF);
            SendLEDButton = (Button) view.findViewById(R.id.SendLED);

            LedOnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).commandSend("LEDON",2);
                }
            });

            LedOffButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).commandSend("LEDOFF",2);
                }
            });
            SendLEDButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentChar = CharToSend.getText().toString();
                    if(currentChar.isEmpty())
                        setLedStatus("Nie podano bajtu");
                    else
                    ((MainActivity) getActivity()).commandSend("LED " + currentChar,2);

                    try{
                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (NullPointerException  e){
                        return;
                    }
                }
            });
        }
    }
    public void setLedStatus(String text){
        LedStatus.setText(text);
    }
}
