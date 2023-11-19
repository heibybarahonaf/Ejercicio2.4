package com.example.ejercicio24.Configuraciones;

public class Signatures {
    private int id;
    private String descripcion;
    private byte[]  image;

    public Signatures() {
    }

    public Signatures(int id, String descripcion, byte[] image) {
        this.id = id;
        this.descripcion = descripcion;
        this.image = image;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
