package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
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

import adapter.CustomListAdapterHoraires;
import app.AppController;
import model.Horaires;
import util.Utility;


public class HorairesActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    private ListView listView3;
    private List<Horaires> horairesList = new ArrayList<Horaires>();
    private CustomListAdapterHoraires adapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeLayout3;
    final Random rnd = new Random();
    private String configGrade;


    public final String URL =
            "http://pierre.hellophoto.fr/tan2/" + rnd.nextInt(3) +".png";
    ImageView imageView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_horaires, container, false);



        View headerView = getLayoutInflater().inflate(
                R.layout.view_list_item_header2, listView3, false);

        // Inflate the layout for this fragment
        return rootView;


    }




    @Override
    public void onCreate (
            final Bundle savedInstanceState) {


        Utility.themer(HorairesActivity.this);





super.onCreate(savedInstanceState);





        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Chargement");
        progress.setMessage("Veuillez patienter pendant le chargement des horaires");
        progress.show();



        Intent intents = getIntent();

        String id = intents.getStringExtra("libelle");
        setContentView(R.layout.activity_horaires);
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
                R.layout.view_list_item_header2, listView3, false);

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



        listView3 = (ListView) findViewById(R.id.list_horaires);
        listView3.addHeaderView(headerView, null, false);
        TextView t = (TextView) findViewById(R.id.headertext2);


        t.setText(intents.getStringExtra("arret") + "  ->   " + intents.getStringExtra("terminus"));

        adapter = new CustomListAdapterHoraires(this, horairesList);
        listView3.setAdapter(adapter);




        listView3.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                View headerView = view.findViewById(R.id.header2);

                imageView = (ImageView) headerView.findViewById(R.id.imageView);

                final float mTop = -headerView.getTop();
                float height = headerView.getHeight();
                if (mTop > height) {
                    // ignore
                    return;
                }
                View imgView = headerView.findViewById(R.id.header2);
                imgView.setTranslationY(mTop / 2f);

            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        final String url = "" + "https://open.tan.fr/ewp/horairesarret.json/" + intent.getExtras().getString("id") + "/" + intent.getExtras().getString("ligne") + "/" + intent.getExtras().getString("sens") + "";


        Log.d("Erreur", url);

        listView3 = (ListView) findViewById(R.id.list_horaires);

        // movieList is an empty array at this point.



        adapter.notifyDataSetChanged();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object












                    JSONArray configJsonArray = response.getJSONArray("horaires");
                    for(int configIterator = 0; configIterator < configJsonArray.length(); configIterator++){
                        ArrayList<String> listdata = new ArrayList<String>();
                        listdata.clear();


                        Horaires horaires = new Horaires();
                        JSONObject innerConfigObj = configJsonArray.getJSONObject(configIterator);
                         configGrade = innerConfigObj.getString("heure");
                        horaires.setHeure(configGrade);


                        JSONArray jr = innerConfigObj.getJSONArray("passages");






                        for(int v = 0; v < jr.length(); v++){



                            listdata.add(jr.get(v).toString());
                            horaires.setPassages(listdata);



                        }


                        horairesList.add(horaires);

                    }












                } catch (JSONException e) {
                    e.printStackTrace();

                }
                progress.dismiss();
                adapter.notifyDataSetChanged();

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());








                adapter.notifyDataSetChanged();

            }
        });





        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);



        swipeLayout3 = (SwipeRefreshLayout) findViewById(R.id.container3);
        swipeLayout3.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipeLayout3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            public void onRefresh() {
                Toast.makeText(getApplication(), "Rechargement...", Toast.LENGTH_SHORT).show();


                horairesList.clear();





                adapter.notifyDataSetChanged();


                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json object response
                            // response will be a json object












                            JSONArray configJsonArray = response.getJSONArray("horaires");
                            for(int configIterator = 0; configIterator < configJsonArray.length(); configIterator++){
                                ArrayList<String> listdata = new ArrayList<String>();
                                listdata.clear();


                                Horaires horaires = new Horaires();
                                JSONObject innerConfigObj = configJsonArray.getJSONObject(configIterator);
                                configGrade = innerConfigObj.getString("heure");
                                horaires.setHeure(configGrade);


                                JSONArray jr = innerConfigObj.getJSONArray("passages");






                                for(int v = 0; v < jr.length(); v++){



                                    listdata.add(jr.get(v).toString());
                                    horaires.setPassages(listdata);



                                }


                                horairesList.add(horaires);

                            }












                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();

                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                        // hide the progress dialog

                    }
                });

                AppController.getInstance().addToRequestQueue(jsonObjReq);

                swipeLayout3.setRefreshing(false);


            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
              super.onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
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


    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
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
            java.net.URL url = new URL(urlString);
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


}