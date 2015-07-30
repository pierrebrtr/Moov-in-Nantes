package activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CustomListAdapterItineraireItem;
import app.AppController;
import model.ItineraireItem;
import util.Utility;

/**
 * Created by dev on 29/07/2015.
 */
public class ItineraireActivity extends Activity {

    private Toolbar mToolbar;
    AutoCompleteTextView depart;
    AutoCompleteTextView arrive;
    private static final String TAG = MainActivity.class.getSimpleName();

    private List<ItineraireItem> itineraireList = new ArrayList<ItineraireItem>();

    Activity activity;

    private String url1 = "https://api.navitia.io/v1/coverage/fr-nw/places?q=moulinais%20nantes";

    final String basicAuth = "Basic " + Base64.encodeToString("a6ca7725-5504-474f-925b-6aa310d48cce:stream53".getBytes(), Base64.NO_WRAP);


    Double firstlat;
    Double firstlon;
    Double secondelat;
    Double secondelon;



    Map<String, String> createBasicAuthHeader(String username, String password) {
        Map<String, String> headerMap = new HashMap<String, String>();

        String credentials = username + ":" + password;
        String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Authorization", "Basic " + base64EncodedCredentials);

        return headerMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.themer(ItineraireActivity.this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_itineraire);
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


        final CustomListAdapterItineraireItem lAdapter = new CustomListAdapterItineraireItem(ItineraireActivity.this, getApplicationContext(), itineraireList);

        depart= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

        depart.setAdapter(lAdapter);


        depart.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                itineraireList.clear();
                lAdapter.notifyDataSetChanged();


                setResearchRequest(s, lAdapter);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                depart.enoughToFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {

                lAdapter.notifyDataSetChanged();

            }
        });


        depart.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {

                TextView textView = (TextView) view.findViewById(R.id.libelle);
                String text = textView.getText().toString();

                TextView textView2 = (TextView) view.findViewById(R.id.lat);
                String lat = textView2.getText().toString();

                TextView textView3 = (TextView) view.findViewById(R.id.lng);
                String lng = textView3.getText().toString();
                depart.setListSelection(pos);
                depart.setText(text);

                firstlat = Double.valueOf(lat);
                Log.d("LATITUDE", String.valueOf(Double.valueOf(lat)));

                firstlon = Double.valueOf(lng);
                Log.d("LONGITUDE", String.valueOf(Double.valueOf(lng)));


            }
        });




        arrive= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);

        arrive.setAdapter(lAdapter);


        arrive.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



                itineraireList.clear();
                lAdapter.notifyDataSetChanged();


                setResearchRequest(s, lAdapter);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                arrive.enoughToFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {

                lAdapter.notifyDataSetChanged();

            }
        });


        arrive.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {

                TextView textView = (TextView) view.findViewById(R.id.libelle);
                String text = textView.getText().toString();

                TextView textView2 = (TextView) view.findViewById(R.id.lat);
                String lat = textView2.getText().toString();

                TextView textView3 = (TextView) view.findViewById(R.id.lng);
                String lng = textView3.getText().toString();
                arrive.setListSelection(pos);
                arrive.setText(text);

                secondelat = Double.valueOf(lat);
                Log.d("SECLATITUDE", String.valueOf(Double.valueOf(lat)));

                secondelon = Double.valueOf(lng);
                Log.d("SECLONGITUDE", String.valueOf(Double.valueOf(lng)));


            }
        });





    }





    public void setResearchRequest(CharSequence s, final CustomListAdapterItineraireItem lAdapter) {



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "https://api.navitia.io/v1/coverage/fr-nw/places?q=" + s + "%20nantes", null, new Response.Listener<JSONObject>() {


            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                // Parsing json object response
                // response will be a json object

                String test = "";
                Double lat = 0.0;
                Double lng = 0.0;

                JSONArray array = null;

                try {
                    array = response.getJSONArray("places");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    for (int i = 0; i < array.length(); i++) {


                        try {

                            String type = array.getJSONObject(i).getString("embedded_type");



                            test = array.getJSONObject(i).getJSONObject(type).getString("name");


                            lat = array.getJSONObject(i).getJSONObject(type).getJSONObject("coord").getDouble("lat");

                            lng = array.getJSONObject(i).getJSONObject(type).getJSONObject("coord").getDouble("lon");





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ItineraireItem item = new ItineraireItem();

                        item.setArret(test);

                        item.setLng(lng);

                        item.setLat(lat);

                        itineraireList.add(item);




                        lAdapter.notifyDataSetChanged();


                    }

                } catch (NullPointerException e) {
                    // do something other
                }


            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createBasicAuthHeader("a6ca7725-5504-474f-925b-6aa310d48cce", "stream53");
            }
        };


        // Adding request to request queue
        try {
            jsonObjReq.getHeaders();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }


        AppController.getInstance().addToRequestQueue(jsonObjReq);


        lAdapter.notifyDataSetChanged();


    }



    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ItineraireActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}


