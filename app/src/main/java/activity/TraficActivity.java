package activity;


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
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomListAdapterBicloo;
import adapter.CustomListAdapterTrafic;
import app.AppController;
import helper.ConnectionDetector;
import model.Trafic;


/**
 * Created by dev on 25/04/15.
 */
public class TraficActivity {

    private ListView listView;
    private CustomListAdapterTrafic adapter;
    private SwipeRefreshLayout swipeLayout;
    ConnectionDetector cd;

    private List<Trafic> listtrafic = new ArrayList<Trafic>();

    private static final String TAG = MainActivity.class.getSimpleName();

    private String url = "http://data.nantes.fr/api/getInfoTraficTANTempsReel/1.0/MJV6JQWO3079AGJ/?output=json";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_trafic, container, false);

        return rootView;


    }


    public void onCreate(
            final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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


        listView = (ListView) getActivity().findViewById(R.id.listbicloo);

        // movieList is an empty array at this point.
        adapter = new CustomListAdapterTrafic(getActivity(), listtrafic);
        listView.setAdapter(adapter);

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

        Trafic trafic = new Trafic();


                                JSONArray configJsonArray = response.getJSONArray("INFOTRAFIC");
                                for(int configIterator = 0; configIterator < configJsonArray.length(); configIterator++){
                                    ArrayList<String> listdata = new ArrayList<String>();
                                    listdata.clear();



    }
}