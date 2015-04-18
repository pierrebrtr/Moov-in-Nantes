package activity;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pierre.tan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CustomListAdapter;
import app.AppController;
import helper.ConnectionDetector;
import model.Arrets;


public class ArretsFragment extends Fragment {


    ConnectionDetector cd;
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "https://open.tan.fr/ewp/arrets.json";
    private List<Arrets> arretsList = new ArrayList<Arrets>();
    private ListView listView;
    private CustomListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private Menu menu;
    private MenuInflater inflater;
    HashMap<String, String> lieumap = new HashMap<String, String>();

    public ArretsFragment() {

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.list);

        // movieList is an empty array at this point.
        adapter = new CustomListAdapter(getActivity(), arretsList);
        listView.setAdapter(adapter);
        // Showing progress dialog before making http request


        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Rechargement...", Toast.LENGTH_SHORT).show();


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

                                        Arrets arret = new Arrets();

                                        arret.setArret(obj.getString("libelle"));


                                        String lieu = obj.getString("codeLieu");

                                        arret.setLieu(lieu);


                                        JSONArray genreArry = obj.getJSONArray("ligne");
                                        ArrayList<String> genre = new ArrayList<String>();
                                        int ligne = genreArry.length();

                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                                Intent intent = new Intent(getActivity().getBaseContext(), TempsActivity.class);
                                                Bundle extras = new Bundle();
                                                extras.putString("status", "lieu");
                                                intent.putExtras(extras);
                                                startActivity(intent);


                                            }
                                        });


                                        for (int v = 0; v < ligne; v++) {

                                            JSONObject nl = genreArry.getJSONObject(v);


                                            genre.add(nl.optString("numLigne").toString());


                                        }


                                        arret.setLigne(genre);


                                        // adding movie to movies array
                                        arretsList.add(arret);

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

                        Toast.makeText(getActivity(), "No internet connection !", Toast.LENGTH_LONG).show();


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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView textView = (TextView) view.findViewById(R.id.lieu);
                String text = textView.getText().toString();

                Intent i = new Intent(ArretsFragment.this.getActivity(), TempsActivity.class);
                i.putExtra("text", text);
                startActivity(i);


            }
        });


    }


    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


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
                                Arrets arret = new Arrets();
                                arret.setArret(obj.getString("libelle"));


                                String lieu = obj.getString("codeLieu");

                                arret.setLieu(lieu);


                                JSONArray genreArry = obj.getJSONArray("ligne");
                                ArrayList<String> genre = new ArrayList<String>();
                                int ligne = genreArry.length();


                                for (int v = 0; v < ligne; v++) {

                                    JSONObject nl = genreArry.getJSONObject(v);


                                    genre.add(nl.optString("numLigne").toString());


                                }


                                arret.setLigne(genre);


                                arretsList.add(arret);

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

                Toast.makeText(getActivity(), "No internet connection !", Toast.LENGTH_LONG).show();


            }


        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView textView = (TextView) view.findViewById(R.id.lieu);
                String text = textView.getText().toString();

                Intent i = new Intent(ArretsFragment.this.getActivity(), TempsActivity.class);
                i.putExtra("text", text);
                startActivity(i);


            }
        });



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_arrets, container, false);


        // Inflate the layout for this fragment
        return rootView;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_search) {
            Toast.makeText(getActivity(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
