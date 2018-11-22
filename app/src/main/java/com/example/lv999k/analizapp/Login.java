package com.example.lv999k.analizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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

    public void sendLogin(View view){
        //Toast.makeText(this, "entro", Toast.LENGTH_LONG).show();

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                R.style.AnalizapTheme);
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
        //TODO: Cambiar por ruta de producción
        String url = "http://138.68.53.94/user/login";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, json,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("response", String.valueOf(response));
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        onLoginFailed();
                        progressDialog.dismiss();
                    }
                });
        queue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginBtn.setEnabled(true);
        Toast.makeText(getBaseContext(), "Login exitoso", Toast.LENGTH_LONG).show();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "El nombre de usuario o la contraseña no coincide con nuestros registros. Intenta de nuevo", Toast.LENGTH_LONG).show();
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
            password.setError("Ingresa contraseña mayor a 4 caracteres");
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
