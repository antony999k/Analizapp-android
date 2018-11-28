package com.example.lv999k.analizapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.bo.Metal;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.utils.CustomResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewMetalFragment extends Fragment {
    ApiService apiService;
    EditText metal_name;
    EditText metal_description;
    Button new_metal_btn;
    Metal newMetal;

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
        new_metal_btn = (Button)view.findViewById(R.id.new_metal_btn);

        new_metal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                newMetal = new Metal(null,metal_name.getText().toString(),metal_description.getText().toString());
                postMetal(newMetal);
            }
        });

        return view;
    }

    public void postMetal(Metal newMetal){
        Call<ResponseBody> call = apiService.newMetal(newMetal);
        call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getActivity().getBaseContext(), "Paso", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getActivity().getBaseContext(), "No paso", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity().getBaseContext(), "Falla", Toast.LENGTH_LONG).show();
                    }
                });

    }

}
