package com.example.lv999k.analizapp.bo;

import com.example.lv999k.analizapp.services.ApiService;

/**
 * Created by javiercuriel on 11/26/18.
 */

public class Metal {
    Integer id;
    String nombre;
    String descripcion;

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Metal(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}



//    create table Metal(
//        id int not null AUTO_INCREMENT,
//        nombre varchar(100) not null,
//        descripcion varchar(200) default '',
//        primary key (id)
//        );