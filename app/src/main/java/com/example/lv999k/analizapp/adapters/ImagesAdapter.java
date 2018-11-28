package com.example.lv999k.analizapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lv999k.analizapp.R;
import com.example.lv999k.analizapp.bo.Image;
import com.example.lv999k.analizapp.services.ApiService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by javiercuriel on 11/27/18.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    List<Image> images;
    ApiService apiService;

    private final OnItemClickListener OnClickListener;

    public interface OnItemClickListener {
        void onItemClick(Image image);
    }


    public ImagesAdapter(List<Image> images, ApiService apiService,final OnItemClickListener OnClickListener){
        this.apiService = apiService;
        this.images = images;
        this.OnClickListener = OnClickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public View view;

        public TextView textView;
        public String title;

        public ImageView imageView;

        ProgressBar progressBar;
        Image image;


        public ViewHolder(View v) {
            super(v);
            view = v;

            textView = view.findViewById(R.id.imageTitle);
            imageView = view.findViewById(R.id.image);
            progressBar = view.findViewById(R.id.progressBar);


        }

        public void setImage(Image image){
            this.image = image;
        }

        public void setListener(final OnItemClickListener listener){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(image);
                }
            });
        }


        public void setTitle(String title) {
            try{
                this.title = title;
                textView.setText(title);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        public void setImageView(ApiService apiService, String filename){
            Call<ResponseBody> call = apiService.loadAnalyzedImage(filename);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Bitmap bmp =  BitmapFactory.decodeStream(response.body().byteStream());
                        progressBar.setVisibility(View.GONE);
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


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Image image = images.get(i);
        viewHolder.setImage(image);
        viewHolder.setListener(OnClickListener);
        viewHolder.setTitle(image.getExperimento() + " - " + image.getMetal());
        viewHolder.setImageView(apiService, image.getFilename());

    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
