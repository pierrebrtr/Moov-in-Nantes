package model;
import java.util.ArrayList;

public class Arrets {
    private String arret;
    private ArrayList<String> ligne;
    private String lieu;

    public Arrets() {
    }

    public Arrets(String thumbnailUrl, ArrayList<String> ligne, String lieu) {

        this.arret = thumbnailUrl;

        this.ligne = ligne;

        this.lieu = lieu;
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
    public void setLieu(String lieu2){
        this.lieu = lieu2;
    }

    public ArrayList<String> getLigne() {
        return ligne;
    }

    public void setLigne(ArrayList<String> ligne) {
        this.ligne = ligne;
    }

}