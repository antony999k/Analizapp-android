package com.example.lv999k.analizapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.services.ApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewMetalFragment extends Fragment {
    ApiService apiService;
    EditText metal_name;
    EditText metal_description;

    public NewMetalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ((Principal) this.getActivity()).apiService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_metal, container, false);

        metal_name = (EditText)view.findViewById(R.id.metal_name);
        metal_description = (EditText)view.findViewById(R.id.metal_description);
        return view;
    }

    public void createMetal(View view){

    }

}
