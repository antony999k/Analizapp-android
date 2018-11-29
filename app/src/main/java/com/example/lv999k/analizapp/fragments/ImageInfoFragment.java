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
import com.facebook.shimmer.ShimmerFrameLayout;
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

public class ImageInfoFragment extends Fragment {
    ApiService apiService;

    Image image;

    Bitmap bmp_original;
    Bitmap bmp_analyzed;

    boolean original_set = false;

    ImageView imageView;
    TextView titleView;
    TextView descriptionView;
    PieChart area_chart;

    //Textview de imagen
    TextView infoImage_time_minutes;
    TextView infoImage_degree;

    //Extra (carga de pantalla)
    private ShimmerFrameLayout mShimmerViewContainer;

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

        imageView = view.findViewById(R.id.imageView);
        titleView = view.findViewById(R.id.title);
        descriptionView = view.findViewById(R.id.description);
        area_chart = (PieChart) view.findViewById(R.id.area_chart);
        infoImage_time_minutes = (TextView)view.findViewById(R.id.infoImage_time_minutes);
        infoImage_degree = (TextView)view.findViewById(R.id.infoImage_degree);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        setImageInfo();
        loadImage();

        addData();

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
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    bmp_analyzed =  BitmapFactory.decodeStream(response.body().byteStream());
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bmp_analyzed);
                    loadOriginal();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
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
        image.getArea_abajo();
        image.getArea_picos();

        ArrayList NoOfEmp = new ArrayList();
        NoOfEmp.add(new Entry((float) image.getArea_abajo(), 0));
        NoOfEmp.add(new Entry((float)image.getArea_picos(), 1));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Areas");

        ArrayList year = new ArrayList();
        year.add("Área de Abajo");
        year.add("Área de picos");
        PieData data = new PieData(year, dataSet);
        data.setValueTextSize(12f);
        area_chart.setDrawHoleEnabled(true);
        area_chart.setHoleColorTransparent(true);
        area_chart.setHoleRadius(20);
        area_chart.setTransparentCircleRadius(30);
        area_chart.setData(data);
        area_chart.setDescription("");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        area_chart.animateXY(1000, 1000);

        infoImage_time_minutes.setText(String.valueOf(image.getTiempo_minutos()));
        infoImage_degree.setText(String.valueOf(image.getGrados()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

}
