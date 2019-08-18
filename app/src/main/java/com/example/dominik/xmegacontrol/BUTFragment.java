package com.example.dominik.xmegacontrol;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class BUTFragment extends Fragment {

    private TextView PressedButtonStatus;
    private Button ButtonSW0, ButtonSW1, ButtonSW2, ButtonSW3;
    boolean flag0 = true;
    boolean flag1 = true;
    boolean flag2 = true;
    boolean flag3 = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_but, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            PressedButtonStatus = (TextView) view.findViewById(R.id.PressedButtonStatus);
            ButtonSW0 = (Button) view.findViewById(R.id.ButtonSW0);
            ButtonSW1 = (Button) view.findViewById(R.id.ButtonSW1);
            ButtonSW2 = (Button) view.findViewById(R.id.ButtonSW2);
            ButtonSW3 = (Button) view.findViewById(R.id.ButtonSW3);

            ButtonSW0.setVisibility(View.INVISIBLE);
            ButtonSW1.setVisibility(View.INVISIBLE);
            ButtonSW2.setVisibility(View.INVISIBLE);
            ButtonSW3.setVisibility(View.INVISIBLE);
        }
    }
    public void setPressedButtonStatus(String text){
        int number = text.charAt(0);
        switch(number){
            case '0':
                if(flag0){
                    ButtonSW0.setVisibility(View.VISIBLE);
                    flag0 = false;
                }
                else{
                    ButtonSW0.setVisibility(View.INVISIBLE);
                    flag0 = true;
                }break;
            case '1':
                if(flag1){
                    ButtonSW1.setVisibility(View.VISIBLE);
                    flag1 = false;
                }
                else{
                    ButtonSW1.setVisibility(View.INVISIBLE);
                    flag1 = true;
                }break;
            case '2':
                if(flag2){
                    ButtonSW2.setVisibility(View.VISIBLE);
                    flag2 = false;
                }
                else{
                    ButtonSW2.setVisibility(View.INVISIBLE);
                    flag2 = true;
                }break;
            case '3':
                if(flag3){
                    ButtonSW3.setVisibility(View.VISIBLE);
                    flag3 = false;
                }
                else{
                    ButtonSW3.setVisibility(View.INVISIBLE);
                    flag3 = true;
                }break;
        }
        PressedButtonStatus.setText(text.substring(1));
    }
}
