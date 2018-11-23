package com.example.lv999k.analizapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id) {
                    case R.id.item_profile:
                        startActivity(new Intent(Principal.this, Profile.class));
                        break;
                    case R.id.item_analytics:
                        Toast.makeText(Principal.this, "Analytics",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_settings:
                        Toast.makeText(Principal.this, "Settings",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_about_us:
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
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
