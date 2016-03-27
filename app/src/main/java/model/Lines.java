package model;

public class Lines {
    private String ligne;
    private String numero;
    private String id;
    private String color;

    public Lines() {
    }

    public Lines(String ligne, String numero, String id, String color) {

        this.ligne = ligne;

        this.numero = numero;

        this.color = color;

        this.id = id;
    }


    public String getLigne() {
        return ligne;
    }

    public void setLigne(String ligne) {
        this.ligne = ligne;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumero() {return numero;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {return color;}

    public void setColor(String color) {this.color = color;
    }





}