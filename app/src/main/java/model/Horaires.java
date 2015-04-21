package model;

import java.util.ArrayList;

public class Horaires {
    private String arret;
    public ArrayList<String> passages;

    private String heure;

    public Horaires() {
    }

    public Horaires(String arret, ArrayList<String> passages, String lieuurl) {

        this.arret = arret;

        this.passages = passages;

        this.heure = lieuurl;
    }


    public String getArret() {
        return arret;
    }

    public void setArret(String thumbnailUrl) {
        this.arret = thumbnailUrl;
    }


    public String getHeure() {
        return heure;
    }

    public void setHeure(String lieuurl) {
        this.heure = lieuurl;
    }

    public ArrayList<String> getPassages() {
        return passages;
    }

    public void setPassages(ArrayList<String> passages) {
        this.passages = passages;
    }




}