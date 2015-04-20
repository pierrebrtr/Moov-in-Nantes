package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

import adapter.CustomListAdapterTemps;
import app.AppController;
import model.Temps;


public class TempsActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    HashMap<String, String> lieumap = new HashMap<String, String>();

    // Movies json url
    private Toolbar mToolbar;
    private List<Temps> directionList = new ArrayList<Temps>();
    private ListView listView2;
    private CustomListAdapterTemps adapter;
    private SwipeRefreshLayout swipeLayout2;
    private Menu menu;
    private MenuInflater inflater;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_temps, container, false);


        // Inflate the layout for this fragment
        return rootView;


    }


    public void onActivityCreated(Bundle savedInstanceState) {

        Intent intent = getIntent();


        final String url = "https://open.tan.fr/ewp/tempsattente.json/" + intent.getExtras().getString("text") + " ";
        System.out.println(intent.getExtras().getString("text") + " Test Test ");


        // movieList is an empty array at this point.
        adapter = new CustomListAdapterTemps(getParent(), directionList);
        listView2.setAdapter(adapter);

        // Showing progress dialog before making http request





    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


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
            String title = getString(R.string.app_name);
            Fragment fragment = null;
            fragment = new ArretsFragment();
            title = "Rechercher";

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Intent intents = getIntent();

        String id = intents.getStringExtra("libelle");
        setContentView(R.layout.activity_temps);


        View headerView = getLayoutInflater().inflate(
                R.layout.view_list_item_header, listView2, false);

        listView2 = (ListView) findViewById(R.id.list_temps);
        listView2.addHeaderView(headerView);
        TextView t = (TextView) findViewById(R.id.headertext);
        t.setText(id);
        listView2.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                View headerView = view.findViewById(R.id.header);

                final float mTop = -headerView.getTop();
                float height = headerView.getHeight();
                if (mTop > height) {
                    // ignore
                    return;
                }
                View imgView = headerView.findViewById(R.id.header);
                imgView.setTranslationY(mTop / 2f);

            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        final String url = "https://open.tan.fr/ewp/tempsattente.json/" + intent.getExtras().getString("text") + " ";

        Toast.makeText(getApplicationContext(), intent.getExtras().getString("text"), Toast.LENGTH_LONG).show();


        listView2 = (ListView) findViewById(R.id.list_temps);

        // movieList is an empty array at this point.
        adapter = new CustomListAdapterTemps(this, directionList);
        listView2.setAdapter(adapter);

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

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

                                Temps temps = new Temps();

                                JSONObject jObject = obj.getJSONObject("ligne");

                                temps.setDirection(obj.getString("terminus"));

                                temps.setLigne(jObject.getString("numLigne"));

                                String temp = obj.getString("temps").replace("Close", "En approche");

                                temps.setTemps(temp);
                                directionList.add(temps);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();


            }


        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);



        swipeLayout2 = (SwipeRefreshLayout) findViewById(R.id.container2);
        swipeLayout2.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipeLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            public void onRefresh() {
                Toast.makeText(getApplication(), "Rechargement...", Toast.LENGTH_SHORT).show();


                directionList.clear();

                final Temps temps2 = new Temps();

                temps2.setDirection("Chargement...");

                temps2.setLigne("  ");

                temps2.setTemps(" ");

                directionList.add(temps2);




                adapter.notifyDataSetChanged();

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

                                        JSONObject jObject = obj.getJSONObject("ligne");



                                        directionList.remove(temps2);



                                        Temps temps = new Temps();



                                        temps.setDirection(obj.getString("terminus"));

                                        temps.setLigne(jObject.getString("numLigne"));

                                        String temp = obj.getString("temps").replace("Close", "En approche");

                                        temps.setTemps(temp);

                                        directionList.add(temps);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();


                    }


                });


                AppController.getInstance().addToRequestQueue(movieReq);

                swipeLayout2.setRefreshing(false);


            }
        });


    }







}
