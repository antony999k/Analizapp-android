package com.example.lv999k.analizapp.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.bo.Experiment;
import com.example.lv999k.analizapp.bo.Metal;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.utils.CustomResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExperimentsFragment extends Fragment {
    ApiService apiService;

    ListView experimentList;
    FloatingActionButton add_experiment_btn;
    ProgressBar experiments_fragment_loading;

    public ExperimentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ((Principal) this.getActivity()).apiService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experiments, container, false);

        experiments_fragment_loading = (ProgressBar)view.findViewById(R.id.experiments_fragment_loading);

        experimentList = view.findViewById(R.id.experiment_list);

        loadExperiments();

        add_experiment_btn = (FloatingActionButton) view.findViewById(R.id.add_experiment_btn);
        add_experiment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NewExperimentFragment();
                ((Principal) getActivity()).setFragment(fragment);
            }
        });

        return view;
    }

    public void loadExperiments(){
        experiments_fragment_loading.setVisibility(View.VISIBLE);
        Call<CustomResponse<Experiment>> call = apiService.allExperiments();
        call.enqueue(new Callback<CustomResponse<Experiment>>() {
            @Override
            public void onResponse(Call<CustomResponse<Experiment>> call, Response<CustomResponse<Experiment>> response) {
                if(response.isSuccessful()){
                    experiments_fragment_loading.setVisibility(View.GONE);
                    setAdapter(response.body().getResults());
                }
                else{
                    experiments_fragment_loading.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getBaseContext(), "Error al obtener los experimentos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CustomResponse<Experiment>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void setAdapter(List<Experiment> list){
        ArrayList<String> titles = new ArrayList<String>();

        for(Experiment experiment: list){
            titles.add(experiment.getNombre());
        }

        experimentList.setAdapter(new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, titles));

    }

}
