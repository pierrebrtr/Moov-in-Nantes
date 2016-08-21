package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.pandf.moovin.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import adapter.CustomListAdapterGeo;
import app.AppController;
import model.Arretsgeo;

import static java.lang.String.valueOf;


public class GeoFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "basic-location-sample";
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    protected TextView latitude_Text;
    protected TextView longitude_Text;
    protected TextView url_Text;
    private List<Arretsgeo> geoList = new ArrayList<Arretsgeo>();
    private CustomListAdapterGeo adapter;
    private ListView listView;
    private String url;
    private SwipeRefreshLayout swipeLayout;






    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
super.onActivityCreated(savedInstanceState);


        setHasOptionsMenu(true);

        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.containergeo);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Rechargement...", Toast.LENGTH_SHORT).show();


                geoList.clear();
                adapter.notifyDataSetChanged();

                onConnected(savedInstanceState);



                swipeLayout.setRefreshing(false);


            }
        });

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);

        swipeLayout.setColorSchemeColors(typedValue.data);


        listView = (ListView) getActivity().findViewById(R.id.listgeo);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView textView = (TextView) view.findViewById(R.id.lieugeo);
                String text = textView.getText().toString();


                TextView textView2 = (TextView) view.findViewById(R.id.arretgeo);
                String libelle = textView2.getText().toString();

                TextView textView3 = (TextView) view.findViewById(R.id.lignegeo);
                String ligne = textView3.getText().toString();

                Intent i = new Intent(GeoFragment.this.getActivity(), TempsActivity.class);
                i.putExtra("text", text);
                i.putExtra("libelle", libelle);
                i.putExtra("ligne", ligne);
                startActivity(i);


            }
        });







    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_geo, container, false);









        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        buildGoogleApiClient();


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        TextView url_Text = (TextView) getView().findViewById((R.id.url_text));

        if (mLastLocation != null) {

            String lat = valueOf(mLastLocation.getLatitude());
            lat = lat.replace(".", ",");

            String lon = valueOf(mLastLocation.getLongitude());
            lon = lon.replace(".", ",");

            url = "http://open.tan.fr/ewp/arrets.json/" + lat + "/" + lon + " ";
            Log.d("Url", url);
            url_Text.setText(url);

            String love = url_Text.getText().toString();
        } else {

        }




        listView = (ListView) getActivity().findViewById(R.id.listgeo);

        // movieList is an empty array at this point.
        adapter = new CustomListAdapterGeo(getActivity(), geoList);
        listView.setAdapter(adapter);


        geoList.clear();
        adapter.notifyDataSetChanged();


        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Chargement");
        progress.setMessage("Veuillez patienter pendant le chargement des arrêts");
        progress.setCancelable(false);
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
                                Arretsgeo arret = new Arretsgeo();
                                String text = new String(obj.getString("libelle").getBytes("ISO-8859-1"), "UTF-8");

                                arret.setArret(text);
                                String lieu = obj.getString("codeLieu");

                                arret.setLieu(lieu);

                                arret.setGeo(obj.getString("distance"));


                                JSONArray genreArry = obj.getJSONArray("ligne");
                                ArrayList<String> genre = new ArrayList<String>();
                                int ligne = genreArry.length();


                                for (int v = 0; v < ligne; v++) {

                                    JSONObject nl = genreArry.getJSONObject(v);

                                    genre.add(nl.optString("numLigne").toString());


                                }


                                arret.setLigne(genre);
                                geoList.add(arret);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
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


                geoList.clear();
                progress.dismiss();
                new MaterialStyledDialog(getActivity())
                        .setTitle("Erreur")
                        .setDescription("Une erreur est survenue")
                        .setHeaderColor(R.color.colorError)
                        .setCancelable(false)

                        .setIcon(R.drawable.ic_alert_circle_outline_white_48dp)
                        .setPositive("Retour", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        })


                        .show();
                adapter.notifyDataSetChanged();


            }


        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);




    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


}
