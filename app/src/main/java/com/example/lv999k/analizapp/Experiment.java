package com.example.lv999k.analizapp;

import java.util.Date;

/**
 * Created by javiercuriel on 11/26/18.
 */

public class Experiment {
    Integer id;
    String nombre;
    String descripcion;
    Integer usuario_id;
    Date fecha;

    public Experiment(Integer id, String nombre, String descripcion, Integer usuario_id, Date fecha) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.usuario_id = usuario_id;
        this.fecha = fecha;
    }
}


//    create table Experimento(
//        id int not null AUTO_INCREMENT,
//        nombre varchar(100) not null,
//        descripcion varchar(200) default '',
//        usuario_id smallint(5) unsigned not null,
//        fecha timestamp default current_timestamp,
//        primary key (id),
//        foreign key (usuario_id) references Usuarios(id)
//        );
