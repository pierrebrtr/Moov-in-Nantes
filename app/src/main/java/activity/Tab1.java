package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.pandf.moovin.MapsActivity;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CustomListAdapter;
import app.AppController;
import helper.ConnectionDetector;
import model.Arrets;
import util.Spfav;

public class Tab1 extends Fragment {
    ListView favoriteList;
    Spfav sharedPreference;
    List<Arrets> favorites;

    Activity activity;

    CustomListAdapter productListAdapter;

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";



    File mfile =new File("/sdcard/favoris.xml");

    public Tab1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activity = getActivity();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_1, container, false);
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


        sharedPreference = new Spfav();
        favorites = sharedPreference.getFavorites(activity);

        if (favorites == null) {



        } else {

            if (favorites.size() == 0) {


            }


            favoriteList = (ListView) view.findViewById(R.id.list);
            if (favorites != null) {
                productListAdapter = new CustomListAdapter(activity, favorites, true);
                favoriteList.setAdapter(productListAdapter);




            }


            favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    TextView textView = (TextView) view.findViewById(R.id.lieu);
                    String text = textView.getText().toString();


                    TextView textView2 = (TextView) view.findViewById(R.id.arret);
                    String libelle = textView2.getText().toString();

                    Intent i = new Intent(getActivity(), TempsActivity.class);
                    i.putExtra("text", text);
                    i.putExtra("libelle", libelle);
                    startActivity(i);


                }
            });


            favoriteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(
                        AdapterView<?> parent, View view,
                        int position, long id) {







                    ImageView button = (ImageView) view
                            .findViewById(R.id.imgbtn_favorite);

                    String tag = button.getTag().toString();

                    sharedPreference.removeFavorite(activity,
                            favorites.get(position));
                    button.setTag("grey");
                    button.setImageResource(R.drawable.ic_heart_white);
                    favorites.remove(favorites.get(position));
                    Toast.makeText(activity, "Supprimé des favoris", Toast.LENGTH_SHORT).show();
                    sharedPreference.saveFavorites(activity, favorites);

                    productListAdapter.notifyDataSetChanged();



                    return true;
                }

            });

            // Inflate the layout for this fragment

        }

        return view;
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public void onDetach() {

        super.onDetach();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_fav, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.imp){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Importer/Exporter")
                    .setMessage("Cette option va vous permettre de sauvegarder et de restaurer à partir de la mémoire interne de votre téléphone les favoris de l'application")
                    .setPositiveButton("Importer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            try {

                                favorites.clear();
                                loadSharedPreferencesFromFile(mfile);
                                productListAdapter.notifyDataSetChanged();

                                Intent i = new Intent(getActivity(), MainActivity.class);

                                startActivity(i);

                            } catch(NullPointerException e) {

                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Erreur")
                                        .setMessage("Vous n'avez pas de favoris à importer")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();

                                            }
                                        })

                                        .setCancelable(true)
                                        .setIcon(R.drawable.alert9)
                                        .show();
                                // do something other
                            }


                        }
                    })
                    .setNegativeButton("Exporter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            saveSharedPreferencesToFile(mfile);
                            Toast.makeText(getActivity(),"Export des favoris réussi !", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setCancelable(true)
                    .setIcon(R.drawable.alert9)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private boolean saveSharedPreferencesToFile(File dst) {
        boolean res = false;
        ObjectOutputStream output = null;
        try {


            output = new ObjectOutputStream(new FileOutputStream(dst));
            SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);
            output.writeObject(pref.getAll());

            res = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }



    @SuppressWarnings({ "unchecked" })
    private boolean loadSharedPreferencesFromFile(File src) {
        boolean res = false;
        ObjectInputStream input;
        input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            SharedPreferences.Editor prefEdit = getActivity().getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE).edit();
            prefEdit.clear();
            Map<String, ?> entries = (Map<String, ?>) input.readObject();
            for (Map.Entry<String, ?> entry : entries.entrySet()) {
                Object v = entry.getValue();
                String key = entry.getKey();

                if (v instanceof Boolean)
                    prefEdit.putBoolean(key, ((Boolean) v).booleanValue());
                else if (v instanceof Float)
                    prefEdit.putFloat(key, ((Float) v).floatValue());
                else if (v instanceof Integer)
                    prefEdit.putInt(key, ((Integer) v).intValue());
                else if (v instanceof Long)
                    prefEdit.putLong(key, ((Long) v).longValue());
                else if (v instanceof String)
                    prefEdit.putString(key, ((String) v));
            }
            prefEdit.commit();
            res = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }



}