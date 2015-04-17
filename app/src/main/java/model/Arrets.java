package model;
import java.util.ArrayList;

public class Arrets {
    private String arret;
    private ArrayList<String> ligne;

    public Arrets() {
    }

    public Arrets(String thumbnailUrl, ArrayList<String> ligne) {

        this.arret = thumbnailUrl;

        this.ligne = ligne;
    }



    public String getArret() {
        return arret;
    }

    public void setArret(String thumbnailUrl) {
        this.arret = thumbnailUrl;
    }




    public ArrayList<String> getLigne() {
        return ligne;
    }

    public void setLigne(ArrayList<String> ligne) {
        this.ligne = ligne;
    }

}