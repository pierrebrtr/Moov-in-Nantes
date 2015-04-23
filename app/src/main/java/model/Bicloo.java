package model;

public class Bicloo {
    private String adresse;
    private String placedispo;
    private String velodispo;

    public Bicloo() {
    }

    public Bicloo(String adresse, String placedispo, String velodispo) {

        this.adresse = adresse;

        this.placedispo = placedispo;

        this.velodispo = velodispo;
    }


    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }


    public String getPlacedispo() {
        return placedispo;
    }

    public void setPlacedispo(String placedispo) {
        this.placedispo = placedispo;
    }

    public String getVelodispo() {
        return velodispo;
    }

    public void setVelodispo(String velodispo) {
        this.velodispo = velodispo;
    }




}