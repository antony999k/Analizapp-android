package com.example.lv999k.analizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("auth",Context.MODE_PRIVATE);
        if(pref.contains("token")){
            final String token = pref.getString("token", "");
            //Revisa si el token no esta vacio o es diferente de nulo
            if(!token.isEmpty() || token != null){
                startActivity(new Intent(Launcher.this, Principal.class));
                finish();
            }else{
                startActivity(new Intent(Launcher.this, Login.class));
                finish();
            }
        }else{
            startActivity(new Intent(Launcher.this, Login.class));
            finish();
        }

    }
}
