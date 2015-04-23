package model;

import java.util.ArrayList;

public class Arretsgeo {
    private String arret;
    public ArrayList<String> ligne;
    private String lieu;
    private String geo;

    public Arretsgeo() {
    }

    public Arretsgeo(String thumbnailUrl, ArrayList<String> ligne, String lieuurl, String geo) {

        this.arret = thumbnailUrl;

        this.ligne = ligne;

        this.lieu = lieuurl;

        this.geo = geo;
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


    public String getGeo() {return geo;}

    public void setGeo(String geo) { this.geo = geo; }

    @Override
    public String toString() {
        return "Arret [id=" + arret + ", name=" + ligne + ", picture=" + lieu + "]";
    }

}