package com.example.dominik.xmegacontrol;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class SEGFragment extends Fragment {

    private String[] elementsToDisplay = {"Wyślij znak 0","Wyślij znak 1","Wyślij znak 2","Wyślij znak 3","Wyślij znak 4","Wyślij znak 5","Wyślij znak 6","" +
            "Wyślij znak 7","Wyślij znak 8","Wyślij znak 9","Wyślij znak A","Wyślij znak B","Wyślij znak C","Wyślij znak D","Wyślij znak E","Wyślij znak F"};
    private  Spinner SegmentsSpinner;
    private TextView SegmentStatus;
    private Button SendSegmentButton, ClearSegmentButton;
    private int elementtoDisplay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seg, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            SegmentStatus = (TextView) view.findViewById(R.id.SegmentStatus);
            SendSegmentButton =(Button) view.findViewById(R.id.SendSegment);
            ClearSegmentButton =(Button) view.findViewById(R.id.ClearSegment);

            SegmentsSpinner = (Spinner)view.findViewById(R.id.SegmentsSpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, elementsToDisplay);
            SegmentsSpinner.setAdapter(adapter);

            SegmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int id, long position) {
                    elementtoDisplay= (int)position;
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            SendSegmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(elementtoDisplay){
                        case 0: ((MainActivity) getActivity()).commandSend("7SEG0", 4); break;
                        case 1: ((MainActivity) getActivity()).commandSend("7SEG1", 4); break;
                        case 2: ((MainActivity) getActivity()).commandSend("7SEG2", 4); break;
                        case 3: ((MainActivity) getActivity()).commandSend("7SEG3", 4); break;
                        case 4: ((MainActivity) getActivity()).commandSend("7SEG4", 4); break;
                        case 5: ((MainActivity) getActivity()).commandSend("7SEG5", 4); break;
                        case 6: ((MainActivity) getActivity()).commandSend("7SEG6", 4); break;
                        case 7: ((MainActivity) getActivity()).commandSend("7SEG7", 4); break;
                        case 8: ((MainActivity) getActivity()).commandSend("7SEG8", 4); break;
                        case 9: ((MainActivity) getActivity()).commandSend("7SEG9", 4); break;
                        case 10: ((MainActivity) getActivity()).commandSend("7SEGA", 4); break;
                        case 11: ((MainActivity) getActivity()).commandSend("7SEGB", 4); break;
                        case 12: ((MainActivity) getActivity()).commandSend("7SEGC", 4); break;
                        case 13: ((MainActivity) getActivity()).commandSend("7SEGD", 4); break;
                        case 14: ((MainActivity) getActivity()).commandSend("7SEGE", 4); break;
                        case 15: ((MainActivity) getActivity()).commandSend("7SEGF", 4); break;
                    }
                }
            });

            ClearSegmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).commandSend("7SEGR", 4);
                }
            });
        }
    }
    public void setSegmentStatus(String text){
        SegmentStatus.setText(text);
    }
}
