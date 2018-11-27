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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lv999k.analizapp.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class Signup extends AppCompatActivity {

    EditText name, lastName, email, password;
    Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText) findViewById(R.id.name);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signupBtn = (Button) findViewById(R.id.signupBtn);
    }

    public void signup(View view){
        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Signup.this,
                R.style.AnalizapTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();

        String nameStr = name.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        JSONObject json = new JSONObject();
        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("nombre", nameStr);
            jsonUser.put("apellido", lastNameStr);
            jsonUser.put("correo", emailStr);
            jsonUser.put("contrasenia", passwordStr);
            json.put("usuario", jsonUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.USERS_NEW;

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, json,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // response
                Log.d("response", String.valueOf(response));
                onSignupSuccess(response);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        onSignupFailed();
                        progressDialog.dismiss();
                    }
                });

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(postRequest);
    }

    public void onSignupSuccess(JSONObject resp) {
        signupBtn.setEnabled(true);
        try {
            String token = resp.getString("token");
            SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("token", token).commit();
            Toast.makeText(getBaseContext(), "Se registro el usuario con exito", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Signup.this, Principal.class));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Error al registrar el usuario", Toast.LENGTH_LONG).show();
        signupBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String nameStr = name.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        if (nameStr.isEmpty() || nameStr.length() < 3) {
            name.setError("Nombre debe ser mayor a 3 caracteres");
            valid = false;
        } else {
            name.setError(null);
        }

        if (lastNameStr.isEmpty() || lastNameStr.length() < 3) {
            lastName.setError("Apellido debe ser mayor a 3 caracteres");
            valid = false;
        } else {
            lastName.setError(null);
        }

        if (emailStr.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwordStr.isEmpty() || passwordStr.length() < 6) {
            password.setError("Ingresa contraseña mayor a 6 caracteres");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    public void loginLink(View view){
        finish();
    }
}
