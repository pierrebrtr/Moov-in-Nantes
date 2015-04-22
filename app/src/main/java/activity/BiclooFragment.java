package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import adapter.CustomListAdapter;
import app.AppController;
import helper.ConnectionDetector;
import model.Arrets;
import util.Spfav;


public class BiclooFragment extends Fragment  {


    ConnectionDetector cd;
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "https://api.jcdecaux.com/vls/v1/stations?contract=Nantes&apiKey=e7eaf7d32891eb367bdc40e3318478bb618e1487";
    private List<Arrets> arretsList = new ArrayList<Arrets>();
    private ListView listView;
    private CustomListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private Menu menu;
    private MenuInflater inflater;
    HashMap<String, String> lieumap = new HashMap<String, String>();
    EditText search;
    Spfav sharedPreference;

    ArrayList listArrets;




    public BiclooFragment() {

    }


    public void filtrer() {
        // retourner la chaine saisie par l'utilisateur
        String name = search.getText().toString();
        // créer une nouvelle liste qui va contenir la résultat à afficher
      ArrayList listFoodNew = new ArrayList();

        for (Arrets food : arretsList) {
            // si le nom du food commence par la chaine saisie , ajouter-le !
            if (food.getArret().toLowerCase().toString().startsWith(name)) {
                listFoodNew.add(food);
            }
        }
        //vider la liste
        listView.setAdapter(null);
        // ajouter la nouvelle liste
        listView.setAdapter(new CustomListAdapter(getActivity(), listFoodNew, false));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.list);

        // movieList is an empty array at this point.
        adapter = new CustomListAdapter(getActivity(), arretsList, false);
        listView.setAdapter(adapter);


        search = (EditText) getActivity().findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub



            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                filtrer();
                adapter.notifyDataSetChanged();

            }
        });

        // Showing progress dialog before making http request


        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Rechargement...", Toast.LENGTH_SHORT).show();


                arretsList.clear();

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

                                        arret.setArret(obj.getString("adress"));


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


                TextView textView2 = (TextView) view.findViewById(R.id.arret);
                String libelle = textView2.getText().toString();

                Intent i = new Intent(BiclooFragment.this.getActivity(), TempsActivity.class);
                i.putExtra("text", text);
                i.putExtra("libelle", libelle);
                startActivity(i);


            }
        });



            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {


                    Arrets item = (Arrets) arg0.getItemAtPosition(position);

                    ImageView button = (ImageView) view.findViewById(R.id.imgbtn_favorite);
                    sharedPreference = new Spfav();



                    String tag = button.getTag().toString();
                    if (tag.equalsIgnoreCase("grey")) {
                        sharedPreference.addFavorite(getActivity(), item);
                        Toast.makeText(getActivity(), "Ajouté au favoris !", Toast.LENGTH_LONG).show();

                        button.setTag("red");
                        button.setImageResource(R.drawable.ic_heart_red);
                    } else {
                        sharedPreference.removeFavorite(getActivity(), item);
                        button.setTag("grey");
                        button.setImageResource(R.drawable.ic_heart_white);
                        Toast.makeText(getActivity(), "Supprimé des favoris !", Toast.LENGTH_LONG).show();
                    }


                    return true;
                }
            });


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


        listArrets = new ArrayList();



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
                                arret.setArret(obj.getString("adress"));


                                String lieu = obj.getString("available_bikes");

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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);





    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bicloo, container, false);

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

        View searchContainer = getActivity().findViewById(R.id.search_container);

        if (id == R.id.action_search){

           if (searchContainer.getVisibility() == View.GONE){

              search.requestFocus();
               InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
               searchContainer.setVisibility(View.VISIBLE);

           } else {
               InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
               searchContainer.setVisibility(View.GONE);

           }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
