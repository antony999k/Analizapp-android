package com.example.lv999k.analizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lv999k.analizapp.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    TextView user_profile_name, user_profile_mail;
    ProgressBar user_profile_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        user_profile_mail = (TextView) findViewById(R.id.user_profile_mail);
        user_profile_loading = (ProgressBar)findViewById(R.id.user_profile_loading);

        SharedPreferences pref = getSharedPreferences("user",Context.MODE_PRIVATE);
        if(pref.contains("nombre") && pref.contains("correo") && pref.contains("apellido")){
            final String nombre = pref.getString("nombre", "") + " " + pref.getString("apellido", "");
            final String correo = pref.getString("correo", "");
            //Revisa si el token no esta vacio o es diferente de nulo
            if((!nombre.isEmpty() || nombre != null) && (!correo.isEmpty() || correo != null)){
                user_profile_name.setText(nombre);
                user_profile_mail.setText(correo);
            }else{
                profileQuery();
            }
        }else{
            profileQuery();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profile.this, Principal.class));
        finish();
    }

    public void profileQuery(){
        user_profile_loading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.USERS_ME;

        SharedPreferences pref = getSharedPreferences("auth",Context.MODE_PRIVATE);
        final String token = pref.getString("token", "");

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // response
                Log.d("response", String.valueOf(response));
                try {
                    user_profile_loading.setVisibility(View.GONE);
                    String nombreCompleto = response.getString("nombre") + " " + response.getString("apellido");
                    user_profile_name.setText(nombreCompleto);
                    user_profile_mail.setText(response.getString("correo"));
                    saveUserData(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getBaseContext(), "Seión caducada", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Profile.this, Login.class));
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
        SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString("nombre", resp.getString("nombre"));
            editor.putString("apellido", resp.getString("apellido"));
            editor.putString("correo", resp.getString("correo"));
            editor.putString("img", resp.getString("img"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/image.png");

        try {
            FileOutputStream fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
}
