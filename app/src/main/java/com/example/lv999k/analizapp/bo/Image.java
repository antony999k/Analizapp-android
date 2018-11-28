package com.example.lv999k.analizapp.bo;

import android.graphics.Bitmap;

import com.example.lv999k.analizapp.utils.Constants;

/**
 * Created by javiercuriel on 11/27/18.
 */

public class Image {
    int id;
    String metal;
    String experimento;
    double tiempo;
    double grados;
    double area_picos;
    double area_abajo;
    String descripcion;
    String ruta_original;
    String ruta_analisis;
    String filename;

    public String getFilename() {
        return filename;
    }

    public Image(int id, String metal, String experimento, double tiempo, double grados, double area_picos, double area_abajo, String descripcion, String filename , String ruta_original, String ruta_analisis) {
        this.id = id;
        this.metal = metal;
        this.experimento = experimento;
        this.tiempo = tiempo;
        this.grados = grados;
        this.area_picos = area_picos;
        this.area_abajo = area_abajo;
        this.descripcion = descripcion;
        this.filename = filename;
        this.ruta_original = ruta_original;
        this.ruta_analisis = ruta_analisis;
    }

    public String getMetal() {
        return metal;
    }

    public String getExperimento() {
        return experimento;
    }

    public String getDescripcion() {
        return descripcion;
    }

}


//    create table Imagen (
//        id int not null AUTO_INCREMENT,
//        metal_id int not null,
//        usuario_id smallint(5) unsigned not null,
//        experimento_id int not null,
//        descripcion varchar(200) default '',
//        tiempo_minutos float,
//        grados float,
//        area_picos float,
//        area_abajo float,
//        ruta_original varchar(200) not null,
//        ruta_analisis varchar(200) not null,
//        primary key (id),
//        foreign key (metal_id) references Metal(id),
//        foreign key (usuario_id) references Usuarios(id),
//        foreign key (experimento_id) references Experimento(id)
//        );