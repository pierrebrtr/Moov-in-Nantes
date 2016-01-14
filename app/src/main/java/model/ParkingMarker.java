package model;

public class ParkingMarker {
    private Double latitude;
    public Double longitude;
    private String lieuname;
    private String places;
    private String dispo;


    public ParkingMarker() {
    }

    public ParkingMarker(Double latitude, Double longitude, String lieuname, String places,String dispo) {

        this.latitude = latitude;

        this.longitude = longitude;

        this.lieuname = lieuname;

        this.places = places;

        this.dispo = dispo;


    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLieuname() {
        return lieuname;
    }

    public void setLieuname(String lieuname) {
        this.lieuname = lieuname;
    }



    public String  getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String  getDispo() {
        return dispo;
    }

    public void setDispo(String places) {
        this.dispo = dispo;
    }

}