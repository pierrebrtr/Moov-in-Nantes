package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CustomListAdapter;
import adapter.CustomListAdapterLigne;
import adapter.LineAdapterQUICK;
import app.AppController;
import helper.ConnectionDetector;
import model.Arrets;
import model.Lines;
import util.Spfav;


public class LigneFragment extends Fragment  {


    ConnectionDetector cd;
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private String url1 = "https://api.navitia.io/v1/coverage/fr-nw/networks/network:tan/lines";
    final String basicAuth = "Basic " + Base64.encodeToString("a6ca7725-5504-474f-925b-6aa310d48cce:stream53".getBytes(), Base64.NO_WRAP);
    private List<Lines> linesList = new ArrayList<Lines>();
    private ListView listView;
    private CustomListAdapterLigne adapter;
    private SwipeRefreshLayout swipeLayout;
    private Menu menu;
    private MenuInflater inflater;
    HashMap<String, String> lieumap = new HashMap<String, String>();
    EditText search;
    Spfav sharedPreference;



    int page = 1;



    public LigneFragment() {

    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listView = (ListView) getActivity().findViewById(R.id.list);

        // movieList is an empty array at this point.
        adapter = new CustomListAdapterLigne(getActivity(), linesList, false);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lines ligne = linesList.get(position);
               String id2 = ligne.getId();
                dialogRoute(id2, ligne.getNumero());
            }
        });

        page = 1;
        dosearchligne("https://api.navitia.io/v1/coverage/fr-nw/networks/network:tan/lines");



    }









    public void dialogRoute(final String id, final String numero) {

        final ArrayList<Lines> lineslist2 = new ArrayList<Lines>();


        final ArrayAdapter<Lines> adapter2 = new ArrayAdapter<Lines>(getActivity(), android.R.layout.simple_list_item_1, lineslist2);
        final LineAdapterQUICK adapterline = new LineAdapterQUICK(getActivity(), lineslist2);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "https://api.navitia.io/v1/coverage/fr-nw/lines/" + id + "/routes", null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.d("URL", "https://api.navitia.io/v1/coverage/fr-nw/lines/" + id + "/routes");
                try {
                    JSONArray array = response.getJSONArray("routes");
                    for (int v = 0; v < array.length(); v++) {
                        JSONObject nl = array.getJSONObject(v);
                        String test2 = nl.getJSONObject("direction").getString("name");
                        Lines ligne = new Lines();
                        ligne.setLigne(" →" + test2);
                        ligne.setId(nl.getString("id"));
                        ligne.setNumero(numero);
                        lineslist2.add(ligne);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapterline.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                new MaterialStyledDialog(getActivity())
                        .setTitle("Erreur")
                        .setDescription("Une erreur est survenue")
                        .setHeaderColor(R.color.colorError)
                        .setCancelable(false)

                        .setIcon(R.drawable.ic_alert_circle_outline_white_48dp)
                        .setPositive("Retour", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        })

                        .show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createBasicAuthHeader("a6ca7725-5504-474f-925b-6aa310d48cce", "stream53");
            }
        };


        // Adding request to request queue
        try {
            jsonObjReq.getHeaders();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }


        AppController.getInstance().addToRequestQueue(jsonObjReq);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Votre direction ?");
        builderSingle.setAdapter(
                adapterline,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DialogPlus dialogplus = DialogPlus.newDialog(getActivity())
                                .setContentHolder(new ViewHolder(R.layout.dialog_linesarrets))
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setInAnimation(R.anim.abc_fade_in)
                                .setHeader(R.layout.header_dialogplus)
                                .setOutAnimation(R.anim.abc_fade_out)
                                .setExpanded(false)
                                .create();
                        dialogplus.show();

                        TextView textiti = (TextView) dialogplus.getHeaderView().findViewById(R.id.textheader);
                        textiti.setText("Ligne " + adapterline.getItem(which).getNumero());
                        TextView textiti2 = (TextView) dialogplus.getHeaderView().findViewById(R.id.textheadertemps);
                        textiti2.setText("");
                        ListView listview = (ListView) dialogplus.findViewById(R.id.listviewlignesarrets);
                        final ArrayList<Arrets> lineslist3 = new ArrayList<Arrets>();
                        final CustomListAdapter adapterline2 = new CustomListAdapter(getActivity(), lineslist3, false);



                        listview.setAdapter(adapterline2);

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                TextView textView = (TextView) view.findViewById(R.id.lieu);
                                String text = textView.getText().toString();


                                TextView textView2 = (TextView) view.findViewById(R.id.arret);
                                String libelle = textView2.getText().toString();

                                Intent i = new Intent(LigneFragment.this.getActivity(), TempsActivity.class);
                                i.putExtra("text", text);
                                i.putExtra("libelle", libelle);
                                startActivity(i);


                            }
                        });

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                "https://api.navitia.io/v1/coverage/fr-nw/routes/" + adapterline.getItem(which).getId() + "?depth=3", null, new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());

                                // Parsing json object response
                                // response will be a json object

                                try {
                                    JSONArray array = response.getJSONArray("routes");


                                    JSONArray array2 = array.getJSONObject(0).getJSONArray("stop_points");

                                    for (int v = 0; v < array2.length(); v++) {

                                        JSONObject nl = array2.getJSONObject(v);

                                        Arrets arret = new Arrets();

                                        arret.setArret(nl.getString(("name")));
                                        ArrayList<String> list = new ArrayList<String>();
                                        list.add(0, numero);
                                        arret.setLigne(list);
                                        String test1 = nl.getJSONObject("stop_area").getString("id");
                                        String test2 = test1.substring(test1.lastIndexOf(":") + 1);
                                        arret.setLieu(test2);
                                        Log.d("TEST", arret.getLieu());
                                        lineslist3.add(arret);


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                adapterline2.notifyDataSetChanged();

                            }
                        }, new Response.ErrorListener() {


                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());

                            }


                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                return createBasicAuthHeader("a6ca7725-5504-474f-925b-6aa310d48cce", "stream53");
                            }
                        };


                        // Adding request to request queue
                        try {
                            jsonObjReq.getHeaders();
                        } catch (AuthFailureError authFailureError) {
                            authFailureError.printStackTrace();
                        }


                        AppController.getInstance().addToRequestQueue(jsonObjReq);


                    }

                });
        builderSingle.show();


    }



    Map<String, String> createBasicAuthHeader(String username, String password) {
        Map<String, String> headerMap = new HashMap<String, String>();

        String credentials = username + ":" + password;
        String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Authorization", "Basic " + base64EncodedCredentials);

        return headerMap;
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);

    }



    ArrayList<JSONObject> array = new ArrayList<JSONObject>();
    public void dosearchligne(String url){


        switch (page){
            case 1:
                page= 2;
                break;
            case 2:
                page = 3;
                break;
            case 3:
                page=4;
                break;
            case 4:
            page = 5;
                break;

        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {


            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                // Parsing json object response
                // response will be a json object

                try {
                    JSONArray array = response.getJSONArray("lines");



                    for (int v = 0; v < array.length(); v++) {

                        JSONObject nl = array.getJSONObject(v);


                        Lines ligne = new Lines();



                        if (nl.getString("code").equals("Navibus")) {
                            ligne.setNumero("⛵");
                        }else if (nl.getString("code").equals("Aéroport")) {
                            ligne.setNumero("✈️");
                        } else {
                            ligne.setNumero(nl.getString("code"));

                        }

                        String direction = nl.getString("name");

                        if (direction.equals("-")){

                            ligne.setLigne(nl.getJSONArray("routes").getJSONObject(0).getString("name"));

                        }else {

                            ligne.setLigne(direction);
                        }


                        ligne.setColor(nl.getString("color"));
                        ligne.setId(nl.getString("id"));

                        linesList.add(ligne);



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
                sort();

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                new MaterialStyledDialog(getActivity())
                        .setTitle("Erreur")
                        .setDescription("Une erreur est survenue")
                        .setHeaderColor(R.color.colorError)
                        .setCancelable(false)

                        .setIcon(R.drawable.ic_alert_circle_outline_white_48dp)
                        .setPositive("Retour", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        })


                        .show();


                linesList.clear();
                adapter.notifyDataSetChanged();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createBasicAuthHeader("a6ca7725-5504-474f-925b-6aa310d48cce", "stream53");
            }
        };


        // Adding request to request queue
        try {
            jsonObjReq.getHeaders();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }


        AppController.getInstance().addToRequestQueue(jsonObjReq);


        switch (page){
            case 1:

                break;
            case 2:
               dosearchligne("https://api.navitia.io/v1/coverage/fr-nw/networks/network:tan/lines?start_page=1");
                break;
            case 3:
                dosearchligne("https://api.navitia.io/v1/coverage/fr-nw/networks/network:tan/lines?start_page=2");

                break;
            case 4:

                break;
            case 5:


                break;
        }






    }


 public void sort(){

     Comparator objComparator = new Comparator<Lines>() {


         public int compare(Lines o1, Lines o2) {
             boolean validated = false;

             if (o1.getNumero().replaceAll("[^\\d.]", "").length() == 0) {

                validated = true;
                 return 1;

             }

             if (o2.getNumero().replaceAll("[^\\d.]", "").length() == 0) {
                 validated = true;
                 return -1;

             }

             if (o1.getNumero().contains("C") || o1.getNumero().contains("E")){
                 validated = true;
                 return 1;
             }
             if (o2.getNumero().contains("C") || o2.getNumero().contains("E")) {
                 validated = true;
                 return -1;

             }

             int no1 = Integer.parseInt((String) o1.getNumero().replaceAll("[^\\d.]", ""));

             int no2 = Integer.parseInt((String) o2.getNumero().replaceAll("[^\\d.]", ""));



             if (validated == false) {
                 return no1 < no2 ? -1 : no1 == no2 ? 0 : 1;
             }


             return no1;
         }
     };
     Collections.sort(linesList, objComparator);
 }
    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_ligne, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ligne, container, false);

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
        View searchContainer = getActivity().findViewById(R.id.search_container);








        return super.onOptionsItemSelected(item);
    }




}
