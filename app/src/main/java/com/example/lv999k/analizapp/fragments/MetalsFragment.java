package com.example.lv999k.analizapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.Profile;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.bo.Metal;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.utils.CustomResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MetalsFragment extends Fragment {
    ApiService apiService;

    ListView metalList;
    FloatingActionButton add_metal_btn;
    ProgressBar metals_fragment_loading;



    public MetalsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        activity = (Principal) this.getActivity();
        apiService = ((Principal) this.getActivity()).apiService;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metals, container, false);

        metals_fragment_loading = (ProgressBar)view.findViewById(R.id.metals_fragment_loading);
        metalList = view.findViewById(R.id.metal_list);

        loadMetals();

        add_metal_btn = (FloatingActionButton) view.findViewById(R.id.add_metal_btn);
        add_metal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NewMetalFragment();
                ((Principal) getActivity()).setFragment(fragment);
            }
        });

        return view;

    }

    public void loadMetals(){
        metals_fragment_loading.setVisibility(View.VISIBLE);
        Call<CustomResponse<Metal>> call = apiService.allMetals();
        call.enqueue(new Callback<CustomResponse<Metal>>() {
            @Override
            public void onResponse(Call<CustomResponse<Metal>> call, Response<CustomResponse<Metal>> response) {
                if(response.isSuccessful()){
                    metals_fragment_loading.setVisibility(View.GONE);
                    setAdapter(response.body().getResults());
                }
                else{
                    metals_fragment_loading.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getBaseContext(), "Error al obtener los metales", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CustomResponse<Metal>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    public void setAdapter(List<Metal> list){
        ArrayList<String> titles = new ArrayList<String>();

        for(Metal metal: list){
            titles.add(metal.getNombre());
        }

        metalList.setAdapter(new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, titles));

    }



}
