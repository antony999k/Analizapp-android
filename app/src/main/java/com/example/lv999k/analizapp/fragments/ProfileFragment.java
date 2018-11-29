package com.example.lv999k.analizapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lv999k.analizapp.Login;
import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.Profile;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.adapters.ImagesAdapter;
import com.example.lv999k.analizapp.bo.Image;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.utils.Constants;
import com.example.lv999k.analizapp.utils.CustomResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javiercuriel on 11/28/18.
 */

public class ProfileFragment extends Fragment {

    RecyclerView recyclerView;
    ApiService apiService;
    ProgressBar user_profile_loading, profile_fragment_loading;


    TextView user_profile_name, user_profile_mail, user_profile_linearLayout_images;

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
        profile_fragment_loading = (ProgressBar)view.findViewById(R.id.profile_fragment_loading);
        user_profile_loading = (ProgressBar) view.findViewById(R.id.user_profile_loading);
        recyclerView = view.findViewById(R.id.images);

        //Profile part
        user_profile_name = (TextView) view.findViewById(R.id.user_profile_name);
        user_profile_mail = (TextView) view.findViewById(R.id.user_profile_mail);
        user_profile_linearLayout_images = (TextView) view.findViewById(R.id.user_profile_linearLayout_images);

        SharedPreferences pref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        if(pref.contains("nombre") && pref.contains("correo") && pref.contains("apellido")){
            final String nombre = pref.getString("nombre", "") + " " + pref.getString("apellido", "");
            final String correo = pref.getString("correo", "");
            final String imgSubidas = pref.getString("imgSubidas","");
            //Revisa si el token no esta vacio o es diferente de nulo
            if((!nombre.isEmpty() || nombre != null) && (!correo.isEmpty() || correo != null)){
                user_profile_name.setText(nombre);
                user_profile_mail.setText(correo);
                if(!imgSubidas.isEmpty() || imgSubidas != null){
                    user_profile_linearLayout_images.setText(imgSubidas);
                }

            }else{
                profileQuery();
            }
        }else{
            profileQuery();
        }

        //Cargar imagenes
        loadImages();

        return view;
    }


    public void profileQuery(){
        user_profile_loading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Constants.USERS_ME;

        SharedPreferences pref = getActivity().getSharedPreferences("auth",Context.MODE_PRIVATE);
        final String token = pref.getString("token", "");

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // response
                Log.d("response", String.valueOf(response));
                try {
                    user_profile_loading.setVisibility(View.GONE);
                    String nombreCompleto = response.getString("nombre") + " " + response.getString("apellido");
                    user_profile_name.setText(nombreCompleto);
                    user_profile_mail.setText(response.getString("correo"));
                    user_profile_linearLayout_images.setText(response.getString("imgSubidas"));
                    saveUserData(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getActivity(), "Seión caducada", Toast.LENGTH_LONG).show();
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
            editor.putString("imgSubidas", resp.getString("imgSubidas"));
            editor.putString("img", resp.getString("img"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
