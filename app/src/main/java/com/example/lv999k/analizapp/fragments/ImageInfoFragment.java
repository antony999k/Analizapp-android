package com.example.lv999k.analizapp.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.bo.Image;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.utils.CustomResponse;

import java.io.Serializable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageInfoFragment extends Fragment {
    ApiService apiService;

    Image image;

    ImageView imageView;
    TextView titleView;
    TextView descriptionView;

    ProgressBar progressBar;


    public ImageInfoFragment() {
        // Required empty public constructor
    }

    public static ImageInfoFragment newInstance(Image image){
        ImageInfoFragment fragment = new ImageInfoFragment();
        Bundle args = new Bundle();

        args.putSerializable("image", image);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ((Principal) this.getActivity()).apiService;
        if (getArguments() != null) {
            image = (Image) getArguments().getSerializable("image");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_info, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        imageView = view.findViewById(R.id.imageView);
        titleView = view.findViewById(R.id.title);
        descriptionView = view.findViewById(R.id.description);

        setImageInfo();
        loadImage();

        return view;
    }

    public void setImageInfo(){
        titleView.setText(image.getExperimento() + " - " + image.getMetal());
        descriptionView.setText(image.getDescripcion());
    }

    public void loadImage(){
        Call<ResponseBody> call = apiService.loadAnalyzedImage(image.getFilename());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Bitmap bmp =  BitmapFactory.decodeStream(response.body().byteStream());
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bmp);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }




}
