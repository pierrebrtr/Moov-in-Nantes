package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;
import com.pandf.moovin.MapsActivity;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomListAdapterBicloo;
import app.AppController;
import helper.ConnectionDetector;
import model.Bicloo;


public class BiclooFragment extends Fragment  {

    private ListView listView;
    private CustomListAdapterBicloo adapter;
    private SwipeRefreshLayout swipeLayout;
    ConnectionDetector cd;

    private List<Bicloo> listbicloo = new ArrayList<Bicloo>();


    ArrayList<LatLng> locations;


    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private String url = "https://api.jcdecaux.com/vls/v1/stations?contract=Nantes&apiKey=e7eaf7d32891eb367bdc40e3318478bb618e1487";





    public BiclooFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bicloo, container, false);

        // Inflate the layout for this fragment
        return rootView;


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        locations = new ArrayList();


        listView = (ListView) getActivity().findViewById(R.id.listbicloo);

        // movieList is an empty array at this point.
        adapter = new CustomListAdapterBicloo(getActivity(), listbicloo);
        listView.setAdapter(adapter);






        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Rechargement...", Toast.LENGTH_SHORT).show();


                listbicloo.clear();
                locations.clear();





                // Creating volley request obj
                JsonArrayRequest movieReq = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());


                                // Parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = null;
                                        try {
                                            obj = response.getJSONObject(i);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }












                                        JSONObject genreArry = obj.getJSONObject("position");




                                        Double latitude;

                                       Double longitude;





                                        latitude = genreArry.getDouble("lat");

                                        longitude = genreArry.getDouble("lng");


                                        Log.d("lat", latitude.toString());




                                        locations.add(new LatLng(latitude, longitude));








                                        Bicloo bicloo = new Bicloo();
                                        bicloo.setAdresse(obj.getString("address"));

                                        bicloo.setIcon(String.valueOf(obj.getBoolean("banking")));

                                        bicloo.setPlacedispo(obj.getString("available_bike_stands"));

                                        bicloo.setVelodispo(obj.getString("available_bikes"));




                                        listbicloo.add(bicloo);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        listbicloo.clear();

                        Bicloo bicloo = new Bicloo();
                        bicloo.setAdresse("Pas de données disponible");

                        bicloo.setPlacedispo(" ");



                        bicloo.setVelodispo(" ");
                        listbicloo.add(bicloo);


                        adapter.notifyDataSetChanged();


                    }


                });


                AppController.getInstance().addToRequestQueue(movieReq);

                swipeLayout.setRefreshing(false);


            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);





    }


        @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

            setHasOptionsMenu(true);
            View searchContainer = getActivity().findViewById(R.id.search_container);
            final EditText toolbarSearchView = (EditText) getActivity().findViewById(R.id.search);
            ImageView searchClearButton = (ImageView) getActivity().findViewById(R.id.search_clear);
            searchClearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toolbarSearchView.setText("");
                }
            });
            searchContainer.setVisibility(View.GONE);


            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setTitle("Chargement");
            progress.setMessage("Veuillez patienter pendant le chargement des arrêts");
            progress.show();

            // Creating volley request obj
            JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());


                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = null;
                                    try {
                                        obj = response.getJSONObject(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }





                                    JSONObject genreArry = obj.getJSONObject("position");




                                    Double latitude;

                                    Double longitude;





                                    latitude = genreArry.getDouble("lat");

                                    longitude = genreArry.getDouble("lng");


                                    Log.d("lat", latitude.toString());


                                    Log.d("lng", longitude.toString());




                                    locations.add(new LatLng(latitude, longitude));




                                    Bicloo bicloo = new Bicloo();
                                   bicloo.setAdresse(obj.getString("address"));
                                    bicloo.setIcon(String.valueOf(obj.getBoolean("banking")));

                                    bicloo.setPlacedispo(obj.getString("available_bike_stands"));

                                    bicloo.setVelodispo(obj.getString("available_bikes"));




                                    listbicloo.add(bicloo);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            progress.dismiss();
                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());


                    progress.dismiss();
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Erreur")
                            .setMessage("Pas de connexion internet")
                            .setPositiveButton("Retour", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setCancelable(false)
                            .setIcon(R.drawable.alert9)
                            .show();
                    listbicloo.clear();

                    adapter.notifyDataSetChanged();

                }


            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);



    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_bicloo, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
    if (id == R.id.action_search){
        Intent intent = new Intent();
        intent.setClass(getActivity(), MapsActivity.class);
        intent.putParcelableArrayListExtra("locations", locations);
        startActivity(intent);
        return true;
    }

    return super.onOptionsItemSelected(item);
}



}
