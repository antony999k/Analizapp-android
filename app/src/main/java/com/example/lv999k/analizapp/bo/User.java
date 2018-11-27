package com.example.lv999k.analizapp.bo;

import java.util.Date;

public class User {
    Integer id;
    String nombre;
    String apellido;
    String correo;
    String contrasenia;
    String img;
    Date diaRegistro;
    String empresa;
    String puesto;
    String area;
    Integer imgSubidas;

    public User(String nombre, String apellido, String correo, String img){
        this.nombre = nombre;
        this.apellido  = apellido;
        this. correo = correo;
        this.img = img;
    }
}
/*
    CREATE TABLE `Usuarios` (
        `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
        `nombre` varchar(20) NOT NULL DEFAULT '',
        `apellido` varchar(20) NOT NULL DEFAULT '',
        `correo` varchar(50) NOT NULL DEFAULT '',
        `contrasenia` varchar(100) NOT NULL DEFAULT '',
        `img` varchar(300) DEFAULT NULL,
        `diaRegistro` date NOT NULL,
        `empresa` varchar(40) DEFAULT NULL,
        `puesto` varchar(50) DEFAULT NULL,
        `area` varchar(50) DEFAULT NULL,
        `privilegios` varchar(10) NOT NULL DEFAULT 'usuario',
        `verificacion` varchar(20) NOT NULL DEFAULT 'desactivada',
        `imgSubidas` int(11) NOT NULL DEFAULT '0',
        PRIMARY KEY (`id`),
        UNIQUE KEY `correo` (`correo`)
        ) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
        */