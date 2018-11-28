package com.example.lv999k.analizapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.adapters.ImagesAdapter;
import com.example.lv999k.analizapp.bo.Image;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.utils.CustomResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javiercuriel on 11/28/18.
 */

public class ProfileFragment extends Fragment {

    RecyclerView recyclerView;
    ApiService apiService;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ((Principal) this.getActivity()).apiService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.test);
        loadImages();

        return view;

    }

    public void loadImages(){
        Call<CustomResponse<Image>> call = apiService.allImages();
        call.enqueue(new Callback<CustomResponse<Image>>() {
            @Override
            public void onResponse(Call<CustomResponse<Image>> call, Response<CustomResponse<Image>> response) {
                if(response.isSuccessful()){
                    ImagesAdapter imagesAdapter = new ImagesAdapter(response.body().getResults(), apiService);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter( imagesAdapter );
                }
            }
            @Override
            public void onFailure(Call<CustomResponse<Image>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}