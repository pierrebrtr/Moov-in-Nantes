package model;

public class Meteo {
    private String temps;
    private String minetmax;
    private String jour;
    private String image;
    private String meteobg;


    public Meteo() {
    }

    public Meteo(String temps, String minetmax,String jour, String image, String meteobg) {

        this.temps = temps;
        this.jour = jour;
        this.minetmax = minetmax;
        this.image = image;
        this.meteobg = meteobg;
    }


    public String getTemps() {
        return temps;
    }

    public void setTemps(String Temps) {
        this.temps = Temps;
    }


    public String getMinetmax() {
        return minetmax;
    }

    public void setMinetmax(String lieuurl) {
        this.minetmax = lieuurl;
    }


    public String getJour() { return  jour;}

    public void setJour(String jour) {this.jour = jour;}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMeteobg() { return  meteobg;}

    public void setMeteobg(String meteobg) {this.meteobg = meteobg;}

}