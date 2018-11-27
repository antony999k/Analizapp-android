package com.example.lv999k.analizapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.lv999k.analizapp.Principal;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MetalsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MetalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MetalsFragment extends Fragment {
    ApiService apiService;

    ListView metalList;


    public MetalsFragment() {
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

        View view = inflater.inflate(R.layout.fragment_metals, container, false);

        metalList = view.findViewById(R.id.metal_list);


        loadMetals();

        return view;

    }

    public void loadMetals(){
        Call<CustomResponse<Metal>> call = apiService.allMetals();
        call.enqueue(new Callback<CustomResponse<Metal>>() {
            @Override
            public void onResponse(Call<CustomResponse<Metal>> call, Response<CustomResponse<Metal>> response) {
                if(response.isSuccessful()){
                    setAdapter(response.body().getResults());
                }
                else{
                    Log.e("Response", "Error in response");
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
