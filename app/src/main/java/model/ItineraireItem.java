package model;

import java.util.ArrayList;

/**
 * Created by dev on 30/07/2015.
 */

public class ItineraireItem {

    private String libelle;
    private Double lng;
    private Double lat;



    public ItineraireItem(){

    }

    public ItineraireItem(String libelle, Double lng, Double lat) {

       this.libelle = libelle;

        this.lng = lng;

        this.lat = lat;
    }


    public String getLibelle() {
        return libelle;
    }

    public void setArret(String libelle) {
        this.libelle = libelle;
    }



    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

}
