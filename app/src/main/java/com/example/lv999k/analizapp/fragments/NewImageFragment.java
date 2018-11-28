package com.example.lv999k.analizapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.bo.Metal;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.utils.CustomResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javiercuriel on 11/27/18.
 */

public class NewImageFragment extends Fragment {
    ApiService apiService;
    String img_path;

    Spinner metal_dropdown;
    List<Metal> metals;
    ArrayList<String> metal_names;


    public NewImageFragment() {
        // Required empty public constructor
    }

    public static NewImageFragment newInstance(String img_path){
        NewImageFragment fragment = new NewImageFragment();
        Bundle args = new Bundle();

        args.putString("img_path", img_path);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ((Principal) this.getActivity()).apiService;
        if (getArguments() != null) {
            img_path= getArguments().getString("img_path");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newimage, container, false);

        Button button = view.findViewById(R.id.add_image_btn);
        button.setText(img_path);

        metal_dropdown = view.findViewById(R.id.dropdown_metal);

        loadMetals();

        return view;

    }

    public void loadElements(){

    }


    public void setElements(Call call){
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if(response.isSuccessful()){
                    metals = response.body().getResults();
                    setMetalNames();
                }
            }
            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void loadMetals(){
        Call<CustomResponse<Metal>> call = apiService.allMetals();

        call.enqueue(new Callback<CustomResponse<Metal>>() {
            @Override
            public void onResponse(Call<CustomResponse<Metal>> call, Response<CustomResponse<Metal>> response) {
                if(response.isSuccessful()){
                    metals = response.body().getResults();
                    setMetalNames();
                }
            }

            @Override
            public void onFailure(Call<CustomResponse<Metal>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void setMetalNames(){
        metal_names = new ArrayList<>();
        for(Metal metal: metals){
            metal_names.add(metal.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, metal_names);
        metal_dropdown.setAdapter(adapter);
    }


}
