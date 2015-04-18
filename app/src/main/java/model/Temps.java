package model;

public class Temps {
    private String direction;
    private String ligne;
    private String temps;

    public Temps() {
    }

    public Temps(String direction, String ligne, String temps) {

        this.direction = direction;

        this.ligne = ligne;

        this.temps = temps;
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String thumbnailUrl) {
        this.direction = thumbnailUrl;
    }


    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public String getLigne() {
        return ligne;
    }

    public void setLigne(String ligne) {
        this.ligne = ligne;
    }

}