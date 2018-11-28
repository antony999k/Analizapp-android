package com.example.lv999k.analizapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.services.ApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewExperimentFragment extends Fragment {
    ApiService apiService;

    public NewExperimentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_experiment, container, false);

        return view;
    }

}
