package com.example.lv999k.analizapp.services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by javiercuriel on 11/26/18.
 */

public class Session {
    private static String PREF_NAME = "auth";
    private static String ID = "token";
    private static String PREF_NAME_USER = "user";

    public static boolean saveSessionID(String token, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(ID , token);
        return editor.commit();
    }
    public static String getSessionID(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return savedSession.getString(ID , null);
    }

    public static boolean deleteSessionID(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(ID);
        return  editor.commit();
    }

    public static boolean deleteSessionUser(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        return  editor.commit();
    }
}
