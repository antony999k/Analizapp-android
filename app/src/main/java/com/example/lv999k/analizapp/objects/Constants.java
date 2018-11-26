package com.example.lv999k.analizapp.objects;

/**
 * Created by javiercuriel on 11/25/18.
 */

public class Constants {
    public static final String SERVER = "http://138.68.53.94";
    //public static final String SERVER = "http://192.168.15.21:3500";

    // User
    public static final String USERS_ALL = SERVER + "/users";
    public static final String USERS_GET = SERVER + "/user/";
    public static final String USERS_ME = SERVER + "/user/me";
    public static final String USERS_NEW = SERVER + "/user";
    public static final String USERS_LOGIN = SERVER + "/user/login";
    public static final String USERS_RECOVERY = SERVER + "/user/recovery";
    public static final String USERS_CHPASSWORD = SERVER + "/user/change-password";
    // Images
    public static final String IMAGES_GET = SERVER + "/images/get/";
    public static final String IMAGES_ME = SERVER + "/images/me";
    public static final String IMAGES_ANALYZE = SERVER + "/images/analyze";
    // Metals
    public static final String METAL_ALL = SERVER + "/metal/all";
    public static final String METAL_GET = SERVER + "/metal/get/";
    public static final String METAL_NEW = SERVER + "/metal/new";
    public static final String METAL_UPDATE = SERVER + "/metal/update/";
    public static final String METAL_DELETE = SERVER + "/metal/delete/";
    // Experiments
    public static final String EXPERIMENT_ALL = SERVER + "/experiment/all";
    public static final String EXPERIMENT_GET = SERVER + "/experiment/get/";
    public static final String EXPERIMENT_NEW = SERVER + "/experiment/new";
    public static final String EXPERIMENT_UPDATE = SERVER + "/experiment/update/";
    public static final String EXPERIMENT_DELETE = SERVER + "/experiment/delete/";



}
