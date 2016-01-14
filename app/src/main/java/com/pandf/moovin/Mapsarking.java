package com.pandf.moovin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.AppController;
import model.ParkingMarker;
import util.Utility;

public class Mapsarking extends ActionBarActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar mToolbar;
    ArrayList<ParkingMarker> locations;
    ArrayList<ParkingMarker> locationsph1;
    ArrayList<ParkingMarker> locationsph2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.themer(Mapsarking.this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapsarking);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Parkings");
        View searchContainer = findViewById(R.id.search_container);
        final EditText toolbarSearchView = (EditText) findViewById(R.id.search);
        ImageView searchClearButton = (ImageView) findViewById(R.id.search_clear);
        searchClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSearchView.setText("");
            }
        });
        searchContainer.setVisibility(View.GONE);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void getParking() {
        locations = new ArrayList();
        locationsph1 = new ArrayList();




        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00022/LOC_EQUIPUB_MOBILITE_NM_STBL/content/?format=json", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {


                    JSONArray genreArry = response.getJSONArray("data");

                    int parkings = genreArry.length();


                    for (int v = 0; v < parkings; v++) {

                        JSONObject nl = genreArry.getJSONObject(v);

                        if (nl.getInt("CATEGORIE") == 1001) {


                           String idobject =  String.valueOf(nl.getInt("_IDOBJ"));

                            String lieuname = nl.getJSONObject("geo").getString("name");
                            Double latitude = nl.getJSONArray("_l").getDouble(0);
                            Double longitude = nl.getJSONArray("_l").getDouble(1);





                            LatLng sydney = new LatLng(latitude, longitude);



                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .snippet("Temps réel bientot disponible")
                                    .title(lieuname));


                        }





                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);





    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
   
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        getParking();







    }


    

}
