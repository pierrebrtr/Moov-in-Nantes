package activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import adapter.CustomListAdapterTemps;
import app.AppController;
import model.Temps;


public class TempsActivity extends ActionBarActivity {
    final Random rnd = new Random();
    private static final String TAG = MainActivity.class.getSimpleName();


    // Movies json url
    private Toolbar mToolbar;
    private List<Temps> directionList = new ArrayList<Temps>();
    private ListView listView2;
    private CustomListAdapterTemps adapter;
    private SwipeRefreshLayout swipeLayout2;
    private Menu menu;
    private MenuInflater inflater;

   private String arret;

    public final String URL =
            "http://pierre.hellophoto.fr/tan2/" + rnd.nextInt(3) +".png";
    ImageView imageView;

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

         imageView = (ImageView) headerView.findViewById(R.id.imageView);

        listView2 = (ListView) findViewById(R.id.list_temps);
        listView2.addHeaderView(headerView, null, false);
        TextView t = (TextView) findViewById(R.id.headertext);
        t.setText(id);



        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Create an object for subclass of AsyncTask
            GetXMLTask task = new GetXMLTask();
            // Execute the task
            task.execute(new String[] { URL });
        } else {
            Toast.makeText(getApplicationContext(),
                    "Pas de connexion internet",
                    Toast.LENGTH_LONG).show();
        }




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


        final Intent intent = getIntent();

        final String url = "https://open.tan.fr/ewp/tempsattente.json/" + intent.getExtras().getString("text") + " ";



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


                TextView textView2 = (TextView) view.findViewById(R.id.sens);
                String libelle = textView2.getText().toString();

                TextView textView3 = (TextView) view.findViewById(R.id.terminus);
                String terminus = textView3.getText().toString();

                TextView textView4 = (TextView) view.findViewById(R.id.codearret);
                String arret2 = textView4.getText().toString();

                TextView textView5 = (TextView) view.findViewById(R.id.ligne);
                String ligne = textView5.getText().toString();




                TextView t = (TextView) findViewById(R.id.headertext);

                i.putExtra("sens", libelle);
                i.putExtra("id", arret2);
                i.putExtra("ligne", ligne);
                i.putExtra("arret", t.getText().toString());
                i.putExtra("terminus", terminus);
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
                                JSONObject jObject2 = obj.getJSONObject("arret");

                                temps.setDirection(obj.getString("terminus"));

                                temps.setLigne(jObject.getString("numLigne"));






                                temps.setSens(obj.getString("sens"));

                                temps.setTerminus(obj.getString("terminus"));

                                String temp = obj.getString("temps").replace("Close", "En approche");

                                temps.setTemps(temp);


                                temps.setArret(jObject2.getString("codeArret"));
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


                directionList.clear();

                final Temps temps2 = new Temps();

                temps2.setDirection("Pas de données disponible");

                temps2.setLigne("  ");

                temps2.setTemps(" ");

                directionList.add(temps2);

                adapter.notifyDataSetChanged();


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

                        directionList.clear();

                        final Temps temps2 = new Temps();

                        temps2.setDirection("Pas de données disponible");

                        temps2.setLigne("  ");

                        temps2.setTemps(" ");

                        directionList.add(temps2);

                        adapter.notifyDataSetChanged();


                    }


                });


                AppController.getInstance().addToRequestQueue(movieReq);

                swipeLayout2.setRefreshing(false);


            }
        });


    }

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
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




    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }





}
