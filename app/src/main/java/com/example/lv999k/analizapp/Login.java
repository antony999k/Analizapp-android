package com.example.lv999k.analizapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.lv999k.analizapp.Constants;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);

    }

    public void login(View view){
        //Toast.makeText(this, "entro", Toast.LENGTH_LONG).show();
        if (!validate()) {
            onNotValidate();
            return;
        }

        loginBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                R.style.AnalizapTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();

        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        JSONObject json = new JSONObject();
        try {
            json.put("correo", emailStr);
            json.put("contrasenia", passwordStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.USERS_LOGIN;

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, json,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("response", String.valueOf(response));
                        onLoginSuccess(response);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        onLoginFailed(error);
                        progressDialog.dismiss();
                    }
                });
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess(JSONObject resp) {
        loginBtn.setEnabled(true);
        try {
            String token = resp.getString("token");
            SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("token", token).commit();
            startActivity(new Intent(Login.this, Principal.class));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onLoginFailed(VolleyError error) {
        if(error instanceof TimeoutError || error instanceof NoConnectionError){
            Toast.makeText(getBaseContext(), "Error al conectarse con el servidor", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getBaseContext(), "El nombre de usuario o la contraseña no coincide con nuestros registros. Intenta de nuevo", Toast.LENGTH_LONG).show();
        }
        loginBtn.setEnabled(true);
    }

    public void onNotValidate(){
        Toast.makeText(getBaseContext(), "Ingresa todos los campos correctamente", Toast.LENGTH_LONG).show();
        loginBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        if (emailStr.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email.setError("ingresa un email válido");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwordStr.isEmpty() || passwordStr.length() < 4) {
            password.setError("Ingresa contraseña mayor a 6 caracteres");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    public void signupLink(View view){
        Intent intent = new Intent(getApplicationContext(), Signup.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }
}
