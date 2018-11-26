package com.example.lv999k.analizapp;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;


import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * Created by javiercuriel on 11/25/18.
 */

public interface ApiService {
    @Multipart
    @POST("images/analyze")
    Call<ResponseBody> analyzeImage(@Header("Authorization") String token, @Part MultipartBody.Part image);

    // Metals
    @GET("metal/all")
    Call<ResponseBody> allMetals(@Header("Authorization") String token);
    @GET("metal/get/{id}")
    Call<ResponseBody> getMetal(@Header("Authorization") String token, @Path("id") Integer id);
    @POST("metal/new")
    Call<ResponseBody> newMetal(@Header("Authorization") String token, @Body Metal metal);
    @PUT("metal/update/{id}")
    Call<ResponseBody> updateMetal(@Header("Authorization") String token, @Path("id") Integer id ,@Body Metal metal);
    @DELETE("metal/delete/{id}")
    Call<ResponseBody> deleteMetal(@Header("Authorization") String token, @Path("id") Integer id);

    // Experiments
    @GET("experiment/all")
    Call<ResponseBody> allExperiments(@Header("Authorization") String token);
    @GET("experiment/get/{id}")
    Call<ResponseBody> getExperiment(@Header("Authorization") String token, @Path("id") Integer id);
    @POST("experiment/new")
    Call<ResponseBody> newExperiment(@Header("Authorization") String token, @Body Experiment experiment);
    @PUT("experiment/update/{id}")
    Call<ResponseBody> updateExperiment(@Header("Authorization") String token, @Path("id") Integer id ,@Body Experiment experiment);
    @DELETE("experiment/delete/{id}")
    Call<ResponseBody> deleteExperiment(@Header("Authorization") String token, @Path("id") Integer id);

}


