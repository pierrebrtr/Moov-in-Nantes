package activity;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.AppController;
import helper.SpacesItemDecoration;


public class Tab3 extends Fragment {

    MaterialListView mListView;
    ImageView viewimage;
    private SwipeRefreshLayout swipeLayout;
    int state = 1;
    View coordinatorLayoutView;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab_3,container,false);
        coordinatorLayoutView = v.findViewById(R.id.snackbarPosition);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshome2);
        mListView  = (MaterialListView) v.findViewById(R.id.material_listviewtab3);
        mListView.addItemDecoration(new SpacesItemDecoration(dpToPx(20)));
        viewimage = (ImageView) v.findViewById(R.id.erreur1);
        mListView.setClipToPadding(false);
        TanDirect();
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_tab3, menu);
        if (state == 1) {
            menu.findItem(R.id.actus).setIcon(R.drawable.ic_newspaper_white_48dp);
        } else if (state == 2) {


            menu.findItem(R.id.actus).setIcon(R.drawable.ic_clock_white_48dp);

        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
setHasOptionsMenu(true);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);

        swipeLayout.setColorSchemeColors(typedValue.data);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListView.getAdapter().clearAll();
                if (state == 1) {

                    TanDirect();
                } else if (state == 2) {

                    Tanactus();
                }

            }
        });





    }

    private int dpToPx(final int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public void Tanactus() {
        viewimage.setVisibility(View.GONE);
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    "http://data.nantes.fr/api/getInfoTraficTANTempsReel/1.0/39W9VSNCSASEOGV/?output=json", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {
                        // Parsing json object response
                        // response will be a json object

                        JSONArray object = response.getJSONObject("opendata").getJSONObject("answer").getJSONObject("data").getJSONObject("ROOT").getJSONObject("LISTE_INFOTRAFICS").getJSONArray("INFOTRAFIC");



                        for (int i = 0; i < object.length(); i++) {
                            try {

                                JSONObject obj = null;
                                try {
                                    obj = object.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                                String heurefin = obj.getString("HEURE_FIN");
                                String datefin = obj.getString("DATE_FIN");
                                String twice = " : ";



                                String text = stripAccents(obj.getString("RESUME"));

                                if (heurefin.equals("") || datefin.equals("") || heurefin.equals(" ") || datefin.equals(" ")) {


                                    twice = "";

                                    datefin = "Indeterminé";
                                }


                                Card card = new Card.Builder(getActivity())
                                        .withProvider(new CardProvider())
                                        .setLayout(R.layout.material_small_image_card)
                                        .setTitle(obj.getString("INTITULE"))
                                        .setSubtitle(obj.getString("DATE_DEBUT") + " : " + obj.getString("HEURE_DEBUT") + " → " + datefin + twice + heurefin)
                                        .setSubtitleColor(getResources().getColor(R.color.cardview_dark_background))
                                        .setDescription(text)
                                        .setDrawable("http://pbs.twimg.com/profile_images/666908099853295617/lRhMgYLE_normal.png")
                                        .endConfig()
                                        .build();

                                mListView.getAdapter().add(mListView.getAdapter().getItemCount(),card, false);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            } 
                        }







                        try {

                        } catch (NullPointerException e) {
                            // do something other
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());


                }
            });

            mListView.getAdapter().clearAll();
            AppController.getInstance().addToRequestQueue(jsonObjReq);

        } else {

            viewimage.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutView, "Pas de connexion", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Rafraichir", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mListView.getAdapter().clearAll();
                           viewimage.setVisibility(View.GONE);
                            Tanactus();
                        }
                    });
            snackbar.show();
        }

        if (swipeLayout.isRefreshing()){

            swipeLayout.setRefreshing(false);

        }
    }

    public void TanDirect() {
        viewimage.setVisibility(View.GONE);
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Create an object for subclass of AsyncTask

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://pierre.hellophoto.fr/tweets/tweet_json?screen_name=infotrafic_tan",
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




                                    String datestr = obj.getString("created_at");

                                    Date date = new SimpleDateFormat("E MMM dd hh:mm:ss Z yyyy", Locale.ENGLISH).parse(datestr);
                                    String str = new SimpleDateFormat("dd MMMM 'à' HH:mm ").format(date);

                                    Card card = new Card.Builder(getActivity())
                                            .withProvider(new CardProvider())
                                            .setLayout(R.layout.material_small_image_card)
                                            .setTitle("Trafic info Tan")
                                            .setSubtitle(str)
                                            .setSubtitleColor(getResources().getColor(R.color.cardview_dark_background))
                                            .setDescription(obj.getString("text"))
                                            .setDrawable(obj.getJSONObject("user").getString("profile_image_url"))
                                            .endConfig()
                                            .build();

                                    mListView.getAdapter().add(mListView.getAdapter().getItemCount(),card, false);




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());





                }


            });

            mListView.getAdapter().clearAll();
            AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        }else {

            viewimage.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutView, "Pas de connexion", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Rafraichir", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mListView.getAdapter().clearAll();
                            viewimage.setVisibility(View.GONE);
                           TanDirect();
                        }
                    });
            snackbar.show();
        }

        if (swipeLayout.isRefreshing()){

            swipeLayout.setRefreshing(false);

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.actus) {


            if (state == 1){

                state++;
                Tanactus();
                item.setIcon(R.drawable.ic_clock_white_48dp);


            } else if (state == 2) {

                state--;
                TanDirect();
                item.setIcon(R.drawable.ic_newspaper_white_48dp);

            }



            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onCreate (
            final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);





    }





}