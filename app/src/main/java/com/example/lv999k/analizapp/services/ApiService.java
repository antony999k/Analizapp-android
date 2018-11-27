package com.example.lv999k.analizapp.services;

import com.example.lv999k.analizapp.bo.Experiment;
import com.example.lv999k.analizapp.bo.Metal;
import com.example.lv999k.analizapp.utils.CustomResponse;

import java.io.Serializable;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;


import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
    Call<ResponseBody> analyzeImage(@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseBody> userLogin(@Field("email") String email, @Field("password") String password);

    // Metals
    @GET("metal/all")
    Call<CustomResponse<Metal>> allMetals();
    @GET("metal/get/{id}")
    Call<ResponseBody> getMetal(@Path("id") Integer id);
    @POST("metal/new")
    Call<ResponseBody> newMetal(@Body Metal metal);
    @PUT("metal/update/{id}")
    Call<ResponseBody> updateMetal(@Path("id") Integer id ,@Body Metal metal);
    @DELETE("metal/delete/{id}")
    Call<ResponseBody> deleteMetal(@Path("id") Integer id);

    // Experiments
    @GET("experiment/all")
    Call<ResponseBody> allExperiments();
    @GET("experiment/get/{id}")
    Call<ResponseBody> getExperiment(@Path("id") Integer id);
    @POST("experiment/new")
    Call<ResponseBody> newExperiment(@Body Experiment experiment);
    @PUT("experiment/update/{id}")
    Call<ResponseBody> updateExperiment(@Path("id") Integer id ,@Body Experiment experiment);
    @DELETE("experiment/delete/{id}")
    Call<ResponseBody> deleteExperiment(@Path("id") Integer id);

}


