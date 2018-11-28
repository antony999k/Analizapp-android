package com.example.lv999k.analizapp.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.bo.Experiment;
import com.example.lv999k.analizapp.bo.Metal;
import com.example.lv999k.analizapp.services.ApiService;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewExperimentFragment extends Fragment {
    ApiService apiService;
    Experiment newExperiment;

    EditText experiment_name;
    EditText experiment_description;
    Button new_experiment_btn;

    public NewExperimentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ((Principal) this.getActivity()).apiService;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_experiment, container, false);

        experiment_name = (EditText)view.findViewById(R.id.experiment_name);
        experiment_description = (EditText)view.findViewById(R.id.experiment_description);
        new_experiment_btn = (Button)view.findViewById(R.id.new_experiment_btn);

        new_experiment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Date date = new Date();
                newExperiment = new Experiment(null, experiment_name.getText().toString(), experiment_description.getText().toString(), null,date);
                postExperiment(newExperiment);
            }
        });

        return view;
    }

    public void postExperiment(Experiment exp){
        Call<ResponseBody> call = apiService.newExperiment(exp);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AnalizapTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando Experimento...");
        progressDialog.show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity().getBaseContext(), "Paso", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(getActivity().getBaseContext(), "No paso", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Error al crear el experimento", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

}
