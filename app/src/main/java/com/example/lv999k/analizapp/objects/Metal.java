package com.example.lv999k.analizapp.objects;

/**
 * Created by javiercuriel on 11/26/18.
 */

public class Metal {
    Integer id;
    String nombre;
    String descripcion;

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