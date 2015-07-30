package activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
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
    private static final String TAG = MainActivity.class.getSimpleName();

    private List<ItineraireItem> itineraireList = new ArrayList<ItineraireItem>();

    Activity activity;

    private String url1 = "https://api.navitia.io/v1/coverage/fr-nw/places?q=moulinais%20nantes";

    final String basicAuth = "Basic " + Base64.encodeToString("a6ca7725-5504-474f-925b-6aa310d48cce:stream53".getBytes(), Base64.NO_WRAP);



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

                depart.showDropDown();


                itineraireList.clear();




                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        "https://api.navitia.io/v1/coverage/fr-nw/places?q=" + s + "%20nantes", null, new Response.Listener<JSONObject>() {


                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        // Parsing json object response
                        // response will be a json object

                        String test = "";
                        try {
                            test = response.getJSONArray("places").getJSONObject(1).getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ItineraireItem item = new ItineraireItem();

                        item.setArret(test);

                        itineraireList.add(item);

                        Log.d("TEST", test);


                        lAdapter.notifyDataSetChanged();


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

                depart.setText(text);

            }
        });


    }


}


