package com.example.lv999k.analizapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
    ProgressBar profile_fragment_loading;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ((Principal) this.getActivity()).apiService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = view.findViewById(R.id.images);
        profile_fragment_loading = (ProgressBar)view.findViewById(R.id.profile_fragment_loading);

        loadImages();

        return view;
    }

    public void loadImages(){
        profile_fragment_loading.setVisibility(View.VISIBLE);
        Call<CustomResponse<Image>> call = apiService.allImagesMe();
        call.enqueue(new Callback<CustomResponse<Image>>() {
            @Override
            public void onResponse(Call<CustomResponse<Image>> call, Response<CustomResponse<Image>> response) {
                if(response.isSuccessful()){
                    ImagesAdapter.OnItemClickListener listener = new ImagesAdapter.OnItemClickListener(){
                        @Override
                        public void onItemClick(Image image) {
                            Fragment fragment = ImageInfoFragment.newInstance(image);
                            ((Principal) getActivity()).setFragment(fragment);
                        }
                    };

                    profile_fragment_loading.setVisibility(View.GONE);
                    ImagesAdapter imagesAdapter = new ImagesAdapter(response.body().getResults(), apiService, listener );
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter( imagesAdapter );
                }
            }
            @Override
            public void onFailure(Call<CustomResponse<Image>> call, Throwable t) {
                profile_fragment_loading.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}
