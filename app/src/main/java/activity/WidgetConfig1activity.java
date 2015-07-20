package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.CustomListAdapter;
import app.AppController;
import model.Arrets;

/**
 * Created by dev on 20/07/2015.
 */
public class WidgetConfig1activity extends Activity {


    public static final String ACTION_TEXT_CHANGED = "com.pandf.moovin.TEXT_CHANGED";
    private int mAppWidgetId = 0 ;
    private static final String url = "https://open.tan.fr/ewp/arrets.json";
    private List<Arrets> arretsList = new ArrayList<Arrets>();
    private ListView listView;
    private CustomListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private Menu menu;
    private MenuInflater inflater;
    HashMap<String, String> lieumap = new HashMap<String, String>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_widget1);

        // Gettng the reference to the "Set Color" button




        listView = (ListView) findViewById(R.id.listwidgetarret);

        // movieList is an empty array at this point.
        adapter = new CustomListAdapter(this, arretsList, false);
        listView.setAdapter(adapter);





        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }





        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());


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
                VolleyLog.d("TAG", "Error: " + error.getMessage());

                new AlertDialog.Builder(WidgetConfig1activity.this)
                        .setTitle("Erreur")
                        .setMessage("Pas de connexion internet")
                        .setPositiveButton("Retour", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(WidgetConfig1activity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                arretsList.clear();



                adapter.notifyDataSetChanged();


            }


        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);








        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView textView = (TextView) view.findViewById(R.id.lieu);
                String text = textView.getText().toString();


                TextView textView2 = (TextView) view.findViewById(R.id.arret);
                String libelle = textView2.getText().toString();




                Intent intent2 = new Intent(WidgetConfig1activity.ACTION_TEXT_CHANGED);
                intent2.putExtra("ID", text);
                intent2.putExtra("ID2", libelle);
                getApplicationContext().sendBroadcast(intent2);




                Log.d("fonction", "fonctionnel");









                Intent intent = new Intent(getApplicationContext(), TempsActivity.class);






                intent.putExtra("text", text);
                intent.putExtra("libelle", libelle);


                // Creating a pending intent, which will be invoked when the user
                // clicks on the widget
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);





                // Getting an instance of WidgetManager
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

                // Instantiating the class RemoteViews with widget_layout
                RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.widget1);


                views.setTextViewText(R.id.textViewwidget, text);

                views.setTextViewText(R.id.textViewwidget2, libelle);

                //  Attach an on-click listener to the clock
                views.setOnClickPendingIntent(R.id.layoutwidget, pendingIntent);

                // Tell the AppWidgetManager to perform an update on the app widget
                appWidgetManager.updateAppWidget(mAppWidgetId, views);

                // Return RESULT_OK from this activity
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();


            }


        });







    }






    public void onActivityCreated(Bundle savedInstanceState) {


















    }





    }
