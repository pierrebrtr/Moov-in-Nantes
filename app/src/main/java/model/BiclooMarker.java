package model;

public class BiclooMarker {
    private Double latitude;
    public Double longitude;
    private String lieuname;

    public BiclooMarker() {
    }

    public BiclooMarker(Double latitude, Double longitude, String lieuname) {

        this.latitude = latitude;

        this.longitude = longitude;

        this.lieuname = lieuname;
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



}