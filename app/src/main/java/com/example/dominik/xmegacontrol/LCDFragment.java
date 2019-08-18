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


public class LCDFragment extends Fragment {

    private Button SendLcdTextButton, ClearLcdButton, CleanLcdTextButton;
    private TextView LcdStatus;
    private EditText LcdText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lcd, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            SendLcdTextButton = (Button) view.findViewById(R.id.SendLcdText);
            ClearLcdButton = (Button) view.findViewById(R.id.ClearLcd);
            CleanLcdTextButton = (Button) view.findViewById(R.id.CleanLcdText);
            LcdStatus = (TextView) view.findViewById(R.id.LcdStatus);
            LcdText = (EditText) view.findViewById(R.id.LcdText);

            SendLcdTextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String CurrentMessage = LcdText.getText().toString();
                    if(CurrentMessage.isEmpty())
                        setLcdStatus("Nie wpisano tekstu");
                    else
                        ((MainActivity) getActivity()).commandSend("TEXT " + CurrentMessage, 3);


                    try{
                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (NullPointerException  e){
                        return;
                    }
                }
            });

            ClearLcdButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).commandSend("CLEARLCD", 3);
                }
            });
            CleanLcdTextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LcdText.setText("");
                    setLcdStatus("Pole wyczyszczone");

                }
            });

        }
    }
    public void setLcdStatus(String text){
        LcdStatus.setText(text);
    }

}
