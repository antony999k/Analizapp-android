package com.example.lv999k.analizapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lv999k.analizapp.Login;
import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.Profile;
import com.example.lv999k.analizapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView principal_greeting_name;
    ProgressBar home_fragment_loading;
    RelativeLayout layout_header_text;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Se declaran componentes de la vista
        principal_greeting_name = (TextView)view.findViewById(R.id.principal_greeting_name);
        home_fragment_loading = (ProgressBar)view.findViewById(R.id.home_fragment_loading);
        layout_header_text = (RelativeLayout) view.findViewById(R.id.layout_header_text);

        SharedPreferences pref = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(pref.contains("nombre") && pref.contains("correo") && pref.contains("apellido")){
            String nombre = pref.getString("nombre", "");
            String correo = pref.getString("correo", "");
            //Revisa si el token no esta vacio o es diferente de nulo
            if((!nombre.isEmpty() || nombre != null) && (!correo.isEmpty() || correo != null)){
                principal_greeting_name.setText(nombre);
            }else{
                //Hacer invisible la cabezera de texto
                layout_header_text.setVisibility(View.GONE);
                profileQuery();
            }
        }else{
            //Hacer invisible la cabezera de texto
            layout_header_text.setVisibility(View.GONE);
            profileQuery();
        }
        return view;
    }

    public void profileQuery(){
        home_fragment_loading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //TODO: Cambiar por ruta de producción
        String url = "http://138.68.53.94/user/me";

        SharedPreferences pref = getActivity().getSharedPreferences("auth",Context.MODE_PRIVATE);
        final String token = pref.getString("token", "");

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // response
                Log.d("response", String.valueOf(response));
                try {
                    home_fragment_loading.setVisibility(View.GONE);
                    layout_header_text.setVisibility(View.VISIBLE);
                    String nombreCompleto = response.getString("nombre");
                    principal_greeting_name.setText(nombreCompleto);
                    saveUserData(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getActivity().getBaseContext(), "Seión caducada", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), Login.class));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("authorization", token);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(postRequest);
    }

    public void saveUserData(JSONObject resp){
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString("nombre", resp.getString("nombre"));
            editor.putString("apellido", resp.getString("apellido"));
            editor.putString("correo", resp.getString("correo"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
