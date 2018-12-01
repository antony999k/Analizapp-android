package com.example.lv999k.analizapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.adapters.ImagesAdapter;
import com.example.lv999k.analizapp.bo.Experiment;
import com.example.lv999k.analizapp.bo.Image;
import com.example.lv999k.analizapp.bo.Metal;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.utils.CustomResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by javiercuriel on 11/27/18.
 */

public class NewImageFragment extends Fragment {
    ApiService apiService;
    Uri image;
    String img_path;

    Spinner metal_dropdown;
    List<Metal> metals;
    ArrayList<String> metal_names;

    Spinner experiment_dropdown;
    List<Experiment> experiments;
    ArrayList<String> experiment_names;

    Button save_button;

    EditText timeText;
    EditText degreesText;
    EditText descriptionText;

    Image imageA;

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
            img_path = getArguments().getString("img_path");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newimage, container, false);

        Button button = view.findViewById(R.id.add_image_btn);
        button.setText(img_path);

        metal_dropdown = view.findViewById(R.id.dropdown_metal);
        experiment_dropdown = view.findViewById(R.id.dropdown_experiment);
        save_button = view.findViewById(R.id.save_btn);

        timeText = view.findViewById(R.id.time);
        degreesText = view.findViewById(R.id.degrees);
        descriptionText = view.findViewById(R.id.description);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                save();
            }
        });

        loadMetals();
        loadExperiments();

        return view;
    }


    public void save(){
        save_button.setEnabled(false);

        if (!validateForm()) {
            onNotValidateForm();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AnalizapTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Analizando Imagen...");
        progressDialog.show();


        File file = new File(img_path);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        RequestBody descripcion = RequestBody.create(MediaType.parse("text/plain"), descriptionText.getText().toString());

        Call<ResponseBody> call = apiService.analyzeImage(
                body,
                metals.get(metal_dropdown.getSelectedItemPosition()).getId(),
                experiments.get(experiment_dropdown.getSelectedItemPosition()).getId(),
                descripcion,
                Double.parseDouble(timeText.getText().toString()),
                Double.parseDouble(degreesText.getText().toString())
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    save_button.setEnabled(true);
                    progressDialog.dismiss();

                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();


                    //goToImageInfo(19);
                }
                else{
                    Toast.makeText(getActivity().getBaseContext(), "Error al analizar la imagen", Toast.LENGTH_LONG).show();
                    save_button.setEnabled(true);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Error al conectarse con el servidor", Toast.LENGTH_LONG).show();
                save_button.setEnabled(true);
                progressDialog.dismiss();
            }
        });

    }

    List<Metal> resultsMetal;
    public void goToImageInfo(Integer id){
    }

    public void loadExperiments(){
        Call<CustomResponse<Experiment>> call = apiService.allExperiments();

        call.enqueue(new Callback<CustomResponse<Experiment>>() {
            @Override
            public void onResponse(Call<CustomResponse<Experiment>> call, Response<CustomResponse<Experiment>> response) {
                if(response.isSuccessful()){
                    experiments = response.body().getResults();
                    setExperimentNames();
                }
            }
            @Override
            public void onFailure(Call<CustomResponse<Experiment>> call, Throwable t) {
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

    public void setExperimentNames(){
        experiment_names = new ArrayList<>();
        for(Experiment experiment: experiments){
            experiment_names.add(experiment.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, experiment_names);
        experiment_dropdown.setAdapter(adapter);
    }

    public void setMetalNames(){
        metal_names = new ArrayList<>();
        for(Metal metal: metals){
            metal_names.add(metal.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, metal_names);
        metal_dropdown.setAdapter(adapter);
    }

    public boolean validateForm() {
        boolean valid = true;

        String timeTextStr = timeText.getText().toString();
        String degreesTextStr = degreesText.getText().toString();
        String descriptionTextStr = descriptionText.getText().toString();

        if (timeTextStr.isEmpty() ) {
            timeText.setError("ingresa un tiempo");
            valid = false;
        } else {
            timeText.setError(null);
        }

        if (degreesTextStr.isEmpty()) {
            degreesText.setError("Ingresa grados");
            valid = false;
        } else {
            degreesText.setError(null);
        }

        if (descriptionTextStr.isEmpty()) {
            descriptionText.setError("Ingresa una descripción");
            valid = false;
        } else {
            descriptionText.setError(null);
        }

        return valid;
    }

    public void onNotValidateForm(){
        Toast.makeText(getActivity().getBaseContext(), "Ingresa todos los campos", Toast.LENGTH_LONG).show();
        save_button.setEnabled(true);
    }

}
