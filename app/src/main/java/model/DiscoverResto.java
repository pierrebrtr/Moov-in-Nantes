package model;



/**
 * Created by pierre on 30/12/2015.
 */
public class DiscoverResto {


    private String nomresto;
    private String typecuisine;
    private String adresse;
    private String nbcouverts;

    public DiscoverResto(){};


    public DiscoverResto (String nomresto, String typecuisine, String adresse, String nbcouverts) {

        this.nomresto = nomresto;
        this.typecuisine = typecuisine;
        this.adresse = adresse;
        this.nbcouverts = nbcouverts;
    }

    public String getNomresto() {return nomresto;}

    public void setNomresto(String nomresto) { this.nomresto = nomresto;}

    public String getTypecuisine() { return typecuisine;}

    public void setTypecuisine(String typecuisine) {this.typecuisine = typecuisine;}

    public String getAdresse() {return adresse;}

    public void setAdresse (String adresse) {this.adresse = adresse;}

    public String getNbcouverts () {return nbcouverts;}

    public void setNbcouverts(String nbcouverts) {this.nbcouverts = nbcouverts;}



}
