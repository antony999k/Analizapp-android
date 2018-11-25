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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.example.lv999k.analizapp.Constants;
import com.example.lv999k.analizapp.Login;
import com.example.lv999k.analizapp.Principal;
import com.example.lv999k.analizapp.Profile;
import com.example.lv999k.analizapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

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
                }

                dispatchTakePictureIntent();
            }
        });

        home_btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
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
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                //TODO mandar ruta/image (filePath) via http

                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                    //FINE
                } else {
                    //NOT IN REQUIRED FORMAT
                }
            }
        }
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

}
