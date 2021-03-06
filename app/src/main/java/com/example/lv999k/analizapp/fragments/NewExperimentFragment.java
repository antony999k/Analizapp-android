package com.example.lv999k.analizapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
        if (!validateForm()) {
            onNotValidateForm();
            return;
        }

        new_experiment_btn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AnalizapTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando Experimento...");
        progressDialog.show();

        Call<ResponseBody> call = apiService.newExperiment(exp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity().getBaseContext(), "El experimento se creo con exito", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                }
                else{
                    Toast.makeText(getActivity().getBaseContext(), "Error al crear el experimento", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Error al conectarse con el servidor", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }


    public boolean validateForm() {
        boolean valid = true;

        String expNameStr = experiment_name.getText().toString();
        String expDescriptionStr = experiment_description.getText().toString();

        if (expNameStr.isEmpty()) {
            experiment_name.setError("Ingresa el nombre del experimento");
            valid = false;
        } else {
            experiment_name.setError(null);
        }

        if (expDescriptionStr.isEmpty()) {
            experiment_description.setError("Ingresa la descripción del experimento");
            valid = false;
        } else {
            experiment_description.setError(null);
        }

        return valid;
    }

    public void onNotValidateForm(){
        Toast.makeText(getActivity().getBaseContext(), "Ingresa todos los campos", Toast.LENGTH_LONG).show();
        new_experiment_btn.setEnabled(true);
    }

}
