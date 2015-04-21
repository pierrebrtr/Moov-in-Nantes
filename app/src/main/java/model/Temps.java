package model;

public class Temps {
    private String direction;
    private String ligne;
    private String temps;
    private String sens;

    public Temps() {
    }

    public Temps(String direction, String ligne, String temps, String sens) {

        this.direction = direction;

        this.ligne = ligne;

        this.temps = temps;
        this.sens = sens;
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

    public void setSens(String sens) {
        this.sens = sens;
    }

    public String getSens() {return sens;}
}