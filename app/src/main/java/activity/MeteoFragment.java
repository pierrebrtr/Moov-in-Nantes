package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dexafree.materialList.cards.BasicButtonsCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.pandf.moovin.R;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;
import helper.ConnectionDetector;
import util.SpLite;


public class MeteoFragment extends Fragment  {
    SpLite sharedlite;
    SwipeRefreshLayout swipeLayout;
    ConnectionDetector cd;
    String condition = " ";
    String tmp = " ";
    String icon = " ";
    String condition2 = " ";
    String tmp2 = " ";
    String icon2 = " ";
    String condition3 = " ";
    String tmp3 = " ";
    String icon3 = " ";
    String condition4 = " ";
    String tmp4 = " ";
    String icon4 = " ";
    String condition5 = " ";
    String tmp5 = " ";
    String icon5 = " ";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.containermeteo);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final MaterialListView mListView = (MaterialListView) getActivity().findViewById(R.id.material_listviewmeteo);
                mListView.clear();
               doaskjson();
                swipeLayout.setRefreshing(false);
            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



    }



    public void doaskjson(){



        final MaterialListView mListView = (MaterialListView) getActivity().findViewById(R.id.material_listviewmeteo);




        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            // Create an object for subclass of AsyncTask

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    "http://www.prevision-meteo.ch/services/json/nantes", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("DEBUG", response.toString());

                    try {
                        // Parsing json object response
                        // response will be a json object

                        JSONObject object = response.getJSONObject("current_condition");


                        tmp = object.getString("tmp");

                        condition = object.getString("condition");
                        icon = object.getString("icon");
                        final MaterialListView mListView = (MaterialListView) getActivity().findViewById(R.id.material_listviewmeteo);

                        SmallImageCard cardmeteo = new SmallImageCard(getActivity());
                        cardmeteo.setTitle("En direct du ciel :");
                        cardmeteo.setDescription(condition + " (" + tmp + "°)");

                        sharedlite = new SpLite();

                        String foo = null;

                        Log.d("LOG", sharedlite.getFavorites(getActivity()).toString());
                        if (sharedlite.getFavorites(getActivity()).contains("false")){

                            cardmeteo.setDrawable(icon);
                        }

                        mListView.add(cardmeteo);



                        JSONObject objecttomorow = response.getJSONObject("fcst_day_1");
                        tmp2 = objecttomorow.getString("tmin") + " à " + objecttomorow.getString("tmax");
                        condition2 = objecttomorow.getString("condition");
                        icon2 = objecttomorow.getString("icon");
                        SmallImageCard cardmeteotomorow = new SmallImageCard(getActivity());
                        cardmeteotomorow.setTitle("Demain dans le ciel :");
                        cardmeteotomorow.setDescription(condition2 + " (" + tmp2 + "°)");
                        sharedlite = new SpLite();
                        Log.d("LOG", sharedlite.getFavorites(getActivity()).toString());
                        if (sharedlite.getFavorites(getActivity()).contains("false")){
                            cardmeteotomorow.setDrawable(icon2);
                        }
                        mListView.add(cardmeteotomorow);
                        JSONObject objectjour2 = response.getJSONObject("fcst_day_2");
                        tmp3 = objectjour2.getString("tmin") + " à " + objectjour2.getString("tmax");
                        condition3 = objectjour2.getString("condition");
                        icon3 = objectjour2.getString("icon");
                        SmallImageCard cardmeteo2 = new SmallImageCard(getActivity());
                        cardmeteo2.setTitle(objectjour2.getString("day_long"));
                        cardmeteo2.setDescription(condition3 + " (" + tmp3 + "°)");

                        sharedlite = new SpLite();
                        Log.d("LOG", sharedlite.getFavorites(getActivity()).toString());
                        if (sharedlite.getFavorites(getActivity()).contains("false")){
                            cardmeteo2.setDrawable(icon3);
                        }
                        mListView.add(cardmeteo2);
                        JSONObject objectjour3 = response.getJSONObject("fcst_day_3");
                        tmp4 = objectjour3.getString("tmin") + " à " + objectjour3.getString("tmax");
                        condition4 = objectjour3.getString("condition");
                        icon4 = objectjour3.getString("icon");
                        SmallImageCard cardmeteo3 = new SmallImageCard(getActivity());
                        cardmeteo3.setTitle(objectjour3.getString("day_long"));
                        cardmeteo3.setDescription(condition4 + " (" + tmp4 + "°)");
                        sharedlite = new SpLite();
                        Log.d("LOG", sharedlite.getFavorites(getActivity()).toString());
                        if (sharedlite.getFavorites(getActivity()).contains("false")){
                            cardmeteo3.setDrawable(icon4);
                        }
                        mListView.add(cardmeteo3);
                        JSONObject objectjour4 = response.getJSONObject("fcst_day_4");
                        tmp5 = objectjour4.getString("tmin") + " à " + objectjour4.getString("tmax");
                        condition5 = objectjour4.getString("condition");
                        icon5 = objectjour4.getString("icon");
                        SmallImageCard cardmeteo4 = new SmallImageCard(getActivity());
                        cardmeteo4.setTitle(objectjour4.getString("day_long"));
                        cardmeteo4.setDescription(condition5 + " (" + tmp5 + "°)");
                        sharedlite = new SpLite();
                        Log.d("LOG", sharedlite.getFavorites(getActivity()).toString());
                        if (sharedlite.getFavorites(getActivity()).contains("false")){
                            cardmeteo4.setDrawable(icon5);
                        }
                        mListView.add(cardmeteo4);




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



                }
            });



            AppController.getInstance().addToRequestQueue(jsonObjReq);


        } else {



            Toast.makeText(getActivity().getApplicationContext(),
                    "Pas de connexion internet",
                    Toast.LENGTH_LONG).show();





            BasicButtonsCard card = new BasicButtonsCard(getActivity());
            card.setTitle("Pas de connexion");
            card.setDescription("Veuillez vérifier votre connexion internet");
            card.setLeftButtonTextColor(getResources().getColor(R.color.colorError));
            card.setLeftButtonText("Rafraichir");

            card.setOnLeftButtonPressedListener(new OnButtonPressListener() {
                @Override
                public void onButtonPressedListener(View view, Card card) {


                    Intent intent = getActivity().getIntent();


                    Intent i = new Intent(MeteoFragment.this.getActivity(), MainActivity.class);

                    startActivity(i);

                }
            });


            mListView.add(card);







        }

















    }








    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        setHasOptionsMenu(true);
        View searchContainer = getActivity().findViewById(R.id.search_container);
        final EditText toolbarSearchView = (EditText) getActivity().findViewById(R.id.search);
        ImageView searchClearButton = (ImageView) getActivity().findViewById(R.id.search_clear);
        searchClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSearchView.setText("");
            }
        });
        searchContainer.setVisibility(View.GONE);



        doaskjson();



    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {

        menu.clear();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meteo, container, false);

        // Inflate the layout for this fragment
        return rootView;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


}
