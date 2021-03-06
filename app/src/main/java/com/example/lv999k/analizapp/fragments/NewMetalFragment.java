package com.example.lv999k.analizapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lv999k.analizapp.Login;
import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.Profile;
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
        //getActivity().setTitle(R.string.NewMetal);

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
        if (!validateForm()) {
            onNotValidateForm();
            return;
        }

        new_metal_btn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AnalizapTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando Material...");
        progressDialog.show();

        Call<ResponseBody> call = apiService.newMetal(newMetal);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity().getBaseContext(), "El metal se creo con exito", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                }
                else{
                    Toast.makeText(getActivity().getBaseContext(), "Error al crear el metal", Toast.LENGTH_LONG).show();
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

        String metalNameStr = metal_name.getText().toString();
        String metalDescriptionStr = metal_description.getText().toString();

        if (metalNameStr.isEmpty()) {
            metal_name.setError("Ingresa el nombre del metal");
            valid = false;
        } else {
            metal_name.setError(null);
        }

        if (metalDescriptionStr.isEmpty()) {
            metal_description.setError("Ingresa la descripción del metal");
            valid = false;
        } else {
            metal_description.setError(null);
        }

        return valid;
    }

    public void onNotValidateForm(){
        Toast.makeText(getActivity().getBaseContext(), "Ingresa todos los campos", Toast.LENGTH_LONG).show();
        new_metal_btn.setEnabled(true);
    }


}
