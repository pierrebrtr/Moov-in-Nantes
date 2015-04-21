package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Random;

import adapter.CustomListAdapterTemps;
import app.AppController;
import model.Temps;


public class TempsActivity extends ActionBarActivity {
    final Random rnd = new Random();
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
    private String sens;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_temps, container, false);

        View headerView = getLayoutInflater().inflate(
                R.layout.view_list_item_header, listView2, false);

        // Inflate the layout for this fragment
        return rootView;


    }

    public void onCreate (
            final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        Intent intents = getIntent();

        String id = intents.getStringExtra("libelle");
        setContentView(R.layout.activity_temps);
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

        View headerView = getLayoutInflater().inflate(
                R.layout.view_list_item_header, listView2, false);


        final ImageView img = (ImageView) headerView.findViewById(R.id.imageView);
        // I have 3 images named img_0 to img_2, so...
        final String str = "img_" + rnd.nextInt(3);
        img.setImageDrawable
                (
                        getResources().getDrawable(getResourceID(str, "drawable",
                                getApplicationContext()))
                );




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


        final Temps temps2 = new Temps();

        temps2.setDirection("Chargement...");

        temps2.setLigne("  ");

        temps2.setTemps(" ");

        directionList.add(temps2);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                Intent i = new Intent(getBaseContext(), HorairesActivity.class);

                i.putExtra("sens", sens);
                startActivity(i);


            }
        });


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


                                directionList.remove(temps2);

                                Temps temps = new Temps();

                                JSONObject jObject = obj.getJSONObject("ligne");

                                temps.setDirection(obj.getString("terminus"));

                                temps.setLigne(jObject.getString("numLigne"));



                                sens = obj.getString("sens");

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



    public void onActivityCreated(Bundle savedInstanceState) {

        Intent intent = getIntent();


        final String url = "https://open.tan.fr/ewp/tempsattente.json/" + intent.getExtras().getString("text") + " ";
        System.out.println(intent.getExtras().getString("text") + " Test Test ");


        // movieList is an empty array at this point.
        adapter = new CustomListAdapterTemps(getParent(), directionList);
        listView2.setAdapter(adapter);

        // Showing progress dialog before making http request





    }




    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }










}
