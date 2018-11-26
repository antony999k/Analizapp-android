package com.example.lv999k.analizapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import com.example.lv999k.analizapp.fragments.AnalyticsFragment;
import com.example.lv999k.analizapp.fragments.HomeFragment;
import com.example.lv999k.analizapp.fragments.SettingsFragment;
import com.example.lv999k.analizapp.services.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Principal extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //Texto de nombre de usuario
    TextView principal_greeting_name;

    public static final String API_URL = "http://192.168.15.21:3500/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ApiService apiService;
    public String jwtToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiService = retrofit.create(ApiService.class);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView)findViewById(R.id.nv);
        setupNavigationDraweContent(navigationView);
        setFragment(0);

        //Start access data
        //principal_greeting_name = (TextView)findViewById(R.id.principal_greeting_name);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void setupNavigationDraweContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id) {
                    case R.id.item_home:
                        //Toast.makeText(Principal.this, "Home",Toast.LENGTH_SHORT).show();
                        item.setChecked(true);
                        setFragment(0);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.item_profile:
                        startActivity(new Intent(Principal.this, Profile.class));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.item_analytics:
                        item.setChecked(true);
                        setFragment(1);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.item_settings:
                        item.setChecked(true);
                        setFragment(2);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.item_about_us:
                        item.setChecked(true);
                        Toast.makeText(Principal.this, "About us",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_logout:
                        logOutAlertDialog();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void setFragment(int position){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position){
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.fragment, homeFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                AnalyticsFragment analyticsFragment = new AnalyticsFragment();
                fragmentTransaction.replace(R.id.fragment, analyticsFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.fragment, settingsFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    public void logOutAlertDialog(){
        AlertDialog.Builder alertaLogOut = new AlertDialog.Builder(this, R.style.AlertDialogLogout);
        alertaLogOut.setTitle(R.string.AlertTitleImportant);
        alertaLogOut.setMessage(R.string.AlertLogoutMessage);
        alertaLogOut.setCancelable(false);
        alertaLogOut.setPositiveButton(R.string.Logout, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogo1, int id) {
                logOut();
            }
        });
        alertaLogOut.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialogo1, int id) {}});
        alertaLogOut.show();
    }

    public void logOut(){
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("token").commit();
        SharedPreferences prefs2 = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = prefs2.edit();
        editor2.clear().commit();
        startActivity(new Intent(Principal.this, Login.class));
    }

}
