package model;

import java.util.ArrayList;

public class Arrets {
    private String arret;
    private ArrayList<String> ligne;
    private String lieu;

    public Arrets() {
    }

    public Arrets(String thumbnailUrl, ArrayList<String> ligne, String lieuurl) {

        this.arret = thumbnailUrl;

        this.ligne = ligne;

        this.lieu = lieuurl;
    }


    public String getArret() {
        return arret;
    }

    public void setArret(String thumbnailUrl) {
        this.arret = thumbnailUrl;
    }


    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieuurl) {
        this.lieu = lieuurl;
    }

    public ArrayList<String> getLigne() {
        return ligne;
    }

    public void setLigne(ArrayList<String> ligne) {
        this.ligne = ligne;
    }


    @Override
    public String toString() {
        return "Arret [id=" + arret + ", name=" + ligne + ", picture=" + lieu + "]";
    }

}