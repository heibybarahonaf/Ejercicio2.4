package com.example.ejercicio24.Configuraciones;

public class Transacciones {

    public static final String NameDatabase="DBPM01E2.4";

    public static final String TablaSignatures="Signatures";

    public static final String id = "id";
    public static final String descripcion="descripcion";
    public static final String firma="firma";


    public static final String CreateTableSignatures ="CREATE TABLE "+ TablaSignatures + "(id INCREMENTE PRIMARY KEY, descripcion TEXT,"+"firma BLOB);";

    public static final String DROPTableSignatures ="DROP TABLE " + TablaSignatures;

}

