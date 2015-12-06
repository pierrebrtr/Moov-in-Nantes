package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pandf.moovin.MapsActivity;
import com.pandf.moovin.R;

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


public class LigneFragment extends Fragment  {


    ConnectionDetector cd;
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "https://open.tan.fr/ewp/arrets.json";
    private List<Arrets> arretsList = new ArrayList<Arrets>();
    private ListView listView;
    private CustomListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private Menu menu;
    private MenuInflater inflater;
    HashMap<String, String> lieumap = new HashMap<String, String>();
    EditText search;
    Spfav sharedPreference;






    public LigneFragment() {

    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        final boolean[] clickedonce = {false};
        final EditText editText = (EditText) getActivity().findViewById(R.id.searchligne);



        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedonce[0] != true) {
                    editText.setText("");
                    int maxLengthofEditText = 3;
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthofEditText)});
                }
            clickedonce[0] = true;

            }
        });


        Button imageButton = (Button) getActivity().findViewById(R.id.buttonchercher);

        imageButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.viewsearch);

                                               layout.setVisibility(View.GONE);



                                               String ligne = editText.getText().toString();

                                               dosearchligne(ligne);

                                           }
                                       });


        listView = (ListView) getActivity().findViewById(R.id.list);

        // movieList is an empty array at this point.
        adapter = new CustomListAdapter(getActivity(), arretsList, false);
        listView.setAdapter(adapter);






        search = (EditText) getActivity().findViewById(R.id.searchligne);
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



            }
        });

        // Showing progress dialog before making http request







        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


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



            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {


                    Arrets item = (Arrets) arg0.getItemAtPosition(position);

                    ImageView button = (ImageView) view.findViewById(R.id.imgbtn_favorite);
                    sharedPreference = new Spfav();


                    String tag = button.getTag().toString();
                    if (tag.equalsIgnoreCase("grey")) {
                        sharedPreference.addFavorite(getActivity(), item);
                        Toast.makeText(getActivity(), "Ajouté au favoris !", Toast.LENGTH_SHORT).show();

                        button.setTag("red");

                    } else if (!tag.equalsIgnoreCase("grey")) {


                        Toast.makeText(getActivity(), "Déjà ajouté aux favoris !", Toast.LENGTH_SHORT).show();
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
















    }



    ArrayList<JSONObject> array = new ArrayList<JSONObject>();
    public void dosearchligne(final String lignesearch){





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


                                boolean had = false;

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


                                    if (nl.optString("numLigne").toString().equals(lignesearch)){

                                    had = true;

                                    }

                                }


                                arret.setLigne(genre);



                                if (had){
                                    arretsList.add(arret);

                                    array.add(response.getJSONObject(i));

                                }




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


                new AlertDialog.Builder(getActivity())
                        .setTitle("Erreur")
                        .setMessage("Pas de connexion internet")
                        .setPositiveButton("Retour", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .setIcon(R.drawable.alert9)
                        .show();
                arretsList.clear();



                adapter.notifyDataSetChanged();


            }


        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);



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

        if (id == R.id.ligne){
            final EditText editText = (EditText) getActivity().findViewById(R.id.searchligne);
            LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.viewsearch);

            layout.setVisibility(View.GONE);



            String ligne = editText.getText().toString();

            dosearchligne(ligne);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
