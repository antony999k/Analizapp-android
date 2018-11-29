package com.example.lv999k.analizapp.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lv999k.analizapp.Principal;
import com.github.mikephil.charting.data.Entry;
import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.adapters.ImagesAdapter;
import com.example.lv999k.analizapp.bo.Image;
import com.example.lv999k.analizapp.services.ApiService;
import com.example.lv999k.analizapp.utils.CustomResponse;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.io.Serializable;
import java.util.ArrayList;

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

    Bitmap bmp_original;
    Bitmap bmp_analyzed;

    boolean original_set = false;

    ImageView imageView;
    TextView titleView;
    TextView descriptionView;
    ProgressBar progressBar;
    PieChart area_chart;

    @SuppressWarnings("FieldCanBeLocal")
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
        area_chart = (PieChart) view.findViewById(R.id.area_chart);

        setImageInfo();
        loadImage();

//        addData();

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
                    bmp_analyzed =  BitmapFactory.decodeStream(response.body().byteStream());
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bmp_analyzed);
                    loadOriginal();


                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    public void loadOriginal(){
        Call<ResponseBody> call = apiService.loadOriginalImage(image.getFilename());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    bmp_original =  BitmapFactory.decodeStream(response.body().byteStream());
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            swapImage();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void swapImage(){
        if(original_set){
            imageView.setImageBitmap(bmp_analyzed);
            original_set = false;
        }
        else{
            imageView.setImageBitmap(bmp_original);
            original_set = true;
        }
    }

    private void addData() {
        //Set chart
        ArrayList NoOfEmp = new ArrayList();
        NoOfEmp.add(new Entry(945f, 0));
        NoOfEmp.add(new Entry(1040f, 1));
        NoOfEmp.add(new Entry(1133f, 2));
        NoOfEmp.add(new Entry(1240f, 3));
        NoOfEmp.add(new Entry(1369f, 4));
        NoOfEmp.add(new Entry(1487f, 5));
        NoOfEmp.add(new Entry(1501f, 6));
        NoOfEmp.add(new Entry(1645f, 7));
        NoOfEmp.add(new Entry(1578f, 8));
        NoOfEmp.add(new Entry(1695f, 9));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Employees");

        ArrayList year = new ArrayList();
        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");
        PieData data = new PieData(year, dataSet);
        area_chart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        area_chart.animateXY(5000, 5000);
    }

}
