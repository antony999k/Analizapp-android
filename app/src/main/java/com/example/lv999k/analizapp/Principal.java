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
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lv999k.analizapp.fragments.AnalyticsFragment;
import com.example.lv999k.analizapp.fragments.HomeFragment;
import com.example.lv999k.analizapp.fragments.SettingsFragment;

public class Principal extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    //private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        //actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);

        //drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //actionBarDrawerToggle.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.nv);
        setupNavigationDraweContent(navigationView);
        setFragment(0);

    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
*/
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
