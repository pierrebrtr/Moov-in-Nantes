package model;

/**
 * Created by dev on 25/04/15.
 */
public class Trafic {
    private String resume;
    private String date_debut;
    private String date_fin;
    private String heure_fin;
    private String heure_debut;

    public Trafic () {
    }

    public Trafic(String resume, String date_debut, String date_fin, String heure_fin, String heure_debut) {

        this.resume = resume;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.heure_debut = heure_debut;
        this.heure_fin = heure_fin;

    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getdate_debut() {
        return date_debut;
    }

    public void setdate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getHeure_debut() {
        return heure_debut;
    }

    public void setHeure_debut(String heure_debut) {
        this.heure_debut = heure_debut;
    }


    public String getHeure_fin() {
        return heure_fin;
    }

    public void setHeure_fin(String heure_fin) {
        this.heure_fin = heure_fin;
    }

}
