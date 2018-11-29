package com.example.lv999k.analizapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lv999k.analizapp.bo.Metal;
import com.example.lv999k.analizapp.services.ApiService;


import com.example.lv999k.analizapp.utils.Constants;
import com.example.lv999k.analizapp.Login;
import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.example.lv999k.analizapp.fragments.NewImageFragment;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    TextView principal_greeting_name;
    ProgressBar home_fragment_loading;
    RelativeLayout layout_header_text;
    //Botones de Foto y Galria
    ImageButton home_btn_take_picture;
    ImageButton home_btn_gallery;

    ApiService apiService;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Se declaran componentes de la vista
        principal_greeting_name = (TextView)view.findViewById(R.id.principal_greeting_name);
        home_fragment_loading = (ProgressBar)view.findViewById(R.id.home_fragment_loading);
        layout_header_text = (RelativeLayout) view.findViewById(R.id.layout_header_text);
        //Declaración de botones
        home_btn_take_picture = (ImageButton) view.findViewById(R.id.home_btn_take_picture);
        home_btn_gallery = (ImageButton) view.findViewById(R.id.home_btn_gallery);

        apiService = ((Principal) this.getActivity()).apiService;


        SharedPreferences pref = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(pref.contains("nombre") && pref.contains("correo") && pref.contains("apellido")){
            String nombre = pref.getString("nombre", "");
            String correo = pref.getString("correo", "");
            //Revisa si el token no esta vacio o es diferente de nulo
            if((!nombre.isEmpty() || nombre != null) && (!correo.isEmpty() || correo != null)){
                principal_greeting_name.setText(nombre);
            }else{
                //Hacer invisible la cabezera de texto
                layout_header_text.setVisibility(View.GONE);
                profileQuery();
            }
        }else{
            //Hacer invisible la cabezera de texto
            layout_header_text.setVisibility(View.GONE);
            profileQuery();
        }

        home_btn_take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //home_btn_take_picture.setEnabled(false);
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                    return;
                }

                dispatchTakePictureIntent();
            }
        });

        home_btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //home_btn_take_picture.setEnabled(false);
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                    return;
                }

                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
//                photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), 1);
            }
        });

        return view;
    }


    public void profileQuery(){
        home_fragment_loading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = Constants.USERS_ME;

        SharedPreferences pref = getActivity().getSharedPreferences("auth",Context.MODE_PRIVATE);
        final String token = pref.getString("token", "");

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // response
                Log.d("response", String.valueOf(response));
                try {
                    home_fragment_loading.setVisibility(View.GONE);
                    layout_header_text.setVisibility(View.VISIBLE);
                    String nombreCompleto = response.getString("nombre");
                    principal_greeting_name.setText(nombreCompleto);
                    saveUserData(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getActivity().getBaseContext(), "Seión caducada", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), Login.class));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("authorization", token);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(postRequest);
    }

    public void saveUserData(JSONObject resp){
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString("nombre", resp.getString("nombre"));
            editor.putString("apellido", resp.getString("apellido"));
            editor.putString("correo", resp.getString("correo"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            try{
                Uri selectedImage = data.getData();
                String path = "";
                if(selectedImage == null){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    //create a file to write bitmap data
                    File f = new File(getActivity().getBaseContext().getCacheDir().getPath(), String.valueOf(System.currentTimeMillis())+".jpg");
                    f.createNewFile();

                    //Convert bitmap to byte array

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    //write the bytes in file
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    path = f.getPath();
                }
                else{
                    path = getRealPath(selectedImage);
                }
                NewImageFragment fragment = NewImageFragment.newInstance(path);
                ((Principal) this.getActivity()).setFragment(fragment);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);
        Toast.makeText(getActivity().getBaseContext(), imagePath, Toast.LENGTH_LONG).show();
        return cursor.getString(column_index);
    }

    public String getRealPath(Uri uri){
        // Will return "image:x*"
        String wholeID = DocumentsContract.getDocumentId(uri);
        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };
        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = getActivity().getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);
        String filePath = "";
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

}
