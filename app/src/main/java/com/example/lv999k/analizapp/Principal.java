package com.example.lv999k.analizapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lv999k.analizapp.fragments.ExperimentsFragment;
import com.example.lv999k.analizapp.fragments.MetalsFragment;
import com.example.lv999k.analizapp.fragments.NewExperimentFragment;
import com.example.lv999k.analizapp.fragments.ProfileFragment;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.services.ApiServiceGenerator;
import com.example.lv999k.analizapp.fragments.HomeFragment;
import com.example.lv999k.analizapp.fragments.SettingsFragment;
import com.example.lv999k.analizapp.utils.Session;

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

        apiService = ApiServiceGenerator.createService(ApiService.class, Session.getSessionID(this));

//        apiService = retrofit.create(ApiService.class);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView)findViewById(R.id.nv);
        setupNavigationDraweContent(navigationView);
        setFragment(new HomeFragment());

        //Start access data
        //principal_greeting_name = (TextView)findViewById(R.id.principal_greeting_name);
    }


    public void setupNavigationDraweContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;
                if(id == R.id.item_logout){
                    logOutAlertDialog();
                    return true;
                }
                switch(id) {
                    case R.id.item_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.item_settings:
                        fragment = new SettingsFragment();
                        break;
                    case R.id.item_metals:
                        fragment = new MetalsFragment();
                        break;
                    case R.id.item_experiments:
                        fragment = new ExperimentsFragment();
                        break;
                    case R.id.item_profile:
                        fragment = new ProfileFragment();
                        break;
                    default:
                        drawerLayout.closeDrawers();
                        return true;
                }
                item.setChecked(true);
                setFragment(fragment);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.addToBackStack( "tag" );
        fragmentTransaction.commit();
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
