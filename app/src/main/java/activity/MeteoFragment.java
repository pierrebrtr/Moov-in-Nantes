package activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pandf.moovin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomListAdapterMeteo;
import app.AppController;
import helper.BitmapWorkerTask;
import helper.ConnectionDetector;
import model.Meteo;
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

    private ListView listView;
    private CustomListAdapterMeteo adapter;
    private List<Meteo> listmeteo = new ArrayList<Meteo>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getActivity().findViewById(R.id.listmeteo);




        // movieList is an empty array at this point.
        adapter = new CustomListAdapterMeteo(getActivity(), listmeteo);
        listView.setAdapter(adapter);

        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.containermeteo);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

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

        listmeteo.clear();




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

                        sharedlite = new SpLite();

                        String foo = null;



                        TypedValue typedValue = new TypedValue();
                        Resources.Theme theme = getActivity().getTheme();
                        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
                        int color = typedValue.data;

                        TypedValue typedValue2 = new TypedValue();

                        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                        int color2 = typedValue.data;






                        View headerView = ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_list_item_headermeteo, null, false);


                        listView.removeHeaderView(getActivity().findViewById(R.id.headermeteo));

                        TextView jour = (TextView) headerView.findViewById(R.id.jourheader);
                        TextView temps = (TextView) headerView.findViewById(R.id.tempsheader);
                        TextView temperature = (TextView) headerView.findViewById(R.id.temperatureheader);
                        ImageView image = (ImageView) headerView.findViewById(R.id.iconheader);

                        BitmapWorkerTask task = new BitmapWorkerTask(image);
                        task.execute(icon);

                        jour.setText("En direct du ciel");
                        temps.setText(condition);
                        temperature.setText(tmp + "°");
                        listView.addHeaderView(headerView);


                        JSONObject objecttomorow = response.getJSONObject("fcst_day_1");
                        tmp2 = objecttomorow.getString("tmin") + " à " + objecttomorow.getString("tmax");
                        condition2 = objecttomorow.getString("condition");
                        icon2 = objecttomorow.getString("icon");



                        Meteo meteo2 = new Meteo();


                        meteo2.setJour("Demain");
                        meteo2.setTemps(condition2);
                        meteo2.setMinetmax(tmp2 + "°");
                        meteo2.setImage(icon2);



                        meteo2.setMeteobg("#" + Integer.toHexString(color2));
                        listmeteo.add(meteo2);

                        JSONObject objectjour2 = response.getJSONObject("fcst_day_2");
                        tmp3 = objectjour2.getString("tmin") + " à " + objectjour2.getString("tmax");
                        condition3 = objectjour2.getString("condition");
                        icon3 = objectjour2.getString("icon");



                        Meteo meteo3 = new Meteo();


                        meteo3.setJour(objectjour2.getString("day_long"));
                        meteo3.setTemps(condition3);
                        meteo3.setMinetmax(tmp3 + "°");
                        meteo3.setImage(icon3);
                        meteo3.setMeteobg("#" + Integer.toHexString(color));
                        listmeteo.add(meteo3);


                        JSONObject objectjour3 = response.getJSONObject("fcst_day_3");
                        tmp4 = objectjour3.getString("tmin") + " à " + objectjour3.getString("tmax");
                        condition4 = objectjour3.getString("condition");
                        icon4 = objectjour3.getString("icon");



                        Meteo meteo4 = new Meteo();


                        meteo4.setJour(objectjour3.getString("day_long"));
                        meteo4.setTemps(condition4);
                        meteo4.setMinetmax(tmp4 + "°");
                        meteo4.setImage(icon4);
                        meteo4.setMeteobg("#" + Integer.toHexString(color2));
                        listmeteo.add(meteo4);



                        JSONObject objectjour4 = response.getJSONObject("fcst_day_4");
                        tmp5 = objectjour4.getString("tmin") + " à " + objectjour4.getString("tmax");
                        condition5 = objectjour4.getString("condition");
                        icon5 = objectjour4.getString("icon");



                        Meteo meteo5 = new Meteo();


                        meteo5.setJour(objectjour4.getString("day_long"));
                        meteo5.setTemps(condition5);
                        meteo5.setMinetmax(tmp5 + "°");
                        meteo5.setImage(icon5);
                        meteo5.setMeteobg("#" + Integer.toHexString(color));
                        listmeteo.add(meteo5);




                        try {




                        } catch (NullPointerException e) {
                            // do something other
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    adapter.notifyDataSetChanged();
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
