package activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;
import helper.SpacesItemDecoration;
import util.SpLite;


public class Tab2 extends Fragment {


    String condition = " ";
    String tmp = " ";
    String icon = " ";


    String condition2 = " ";
    String tmp2 = " ";
    String icon2 = " ";
    MaterialListView mListView;
    View coordinatorLayoutView;
    ImageView viewimage;
    private static final String TAG = MainActivity.class.getSimpleName();

    private SwipeRefreshLayout swipeLayout;
    SpLite sharedlite;


    private int dpToPx(final int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =inflater.inflate(R.layout.tab_2,container,false);
        coordinatorLayoutView = v.findViewById(R.id.snackbarPosition);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshome);
        mListView  = (MaterialListView) v.findViewById(R.id.material_listview);
   viewimage = (ImageView) v.findViewById(R.id.erreur1);
        mListView.addItemDecoration(new SpacesItemDecoration(dpToPx(20)));
        mListView.setClipToPadding(false);
        SetupView();

        return v;

    }


    public void SetupView() {
        viewimage.setVisibility(View.GONE);
        mListView.getAdapter().clearAll();

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();



        if (networkInfo != null && networkInfo.isConnected()) {
            // Create an object for subclass of AsyncTask

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    "http://www.prevision-meteo.ch/services/json/nantes", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {
                        // Parsing json object response
                        // response will be a json object

                        JSONObject object = response.getJSONObject("current_condition");



                        tmp = object.getString("tmp");

                        condition = object.getString("condition");


                        mListView.getAdapter().clear();

                        sharedlite = new SpLite();

                        String foo = null;

                        Log.d("LOG", sharedlite.getFavorites(getActivity()).toString());

                        icon = object.getString("icon");

                        Card cardmeteo = new Card.Builder(getActivity())
                                .withProvider(new CardProvider())
                                .setLayout(R.layout.material_small_image_card)
                                .setTitle("En direct du ciel :")
                                .setDescription(condition + " (" + tmp + "°)")
                                .setDrawable(icon)
                                .endConfig()
                                .build();



                        mListView.getAdapter().add(mListView.getAdapter().getItemCount(),cardmeteo, false);

                        Card card2 = new Card.Builder(getActivity())
                                .withProvider(new CardProvider())
                                .setLayout(R.layout.material_basic_buttons_card)
                                .setTitle("Nouveau !")
                                .setDescription("Les itinéraires sont enfin disponibles ! (Attention instable !)")
                                .addAction(R.id.left_text_button, new TextViewAction(getActivity())
                                        .setText("Entrer")
                                        .setTextColor(getResources().getColor(R.color.grey_title))

                                        .setListener(new OnActionClickListener() {
                                            @Override
                                            public void onActionClicked(View view, Card card) {
                                                Intent intent = getActivity().getIntent();


                                                Intent i = new Intent(Tab2.this.getActivity(), ItineraireActivity.class);

                                                startActivity(i);
                                            }



                                        }))
                                .endConfig()
                                .build();

                        mListView.getAdapter().add(mListView.getAdapter().getItemCount(),card2, false);


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


            AppController.getInstance().addToRequestQueue(jsonObjReq);

        } else {
            viewimage.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutView, "Pas de connexion", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Rafraichir", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewimage.setVisibility(View.GONE);
                            SetupView();
                        }
                    });
            snackbar.show();

        }

        JsonArrayRequest movieReq = new JsonArrayRequest("http://pierre.hellophoto.fr/moovinantes/json/cards.json",
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




                                if (obj.getString("type").equals("1")) {



                                    final String testurl = obj.getString("url");
                                    Card card2 = new Card.Builder(getActivity())
                                            .withProvider(new CardProvider())
                                            .setLayout(R.layout.material_basic_buttons_card)
                                            .setTitle(obj.getString("title"))
                                            .setDescription(obj.getString("description"))
                                            .addAction(R.id.left_text_button, new TextViewAction(getActivity())
                                                    .setText(obj.getString("bouton"))
                                                    .setTextColor(getResources().getColor(R.color.grey_title))

                                                    .setListener(new OnActionClickListener() {
                                                        @Override
                                                        public void onActionClicked(View view, Card card) {
                                                            String url = testurl;
                                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                                            i.setData(Uri.parse(url));
                                                            startActivity(i);
                                                        }



                                                    }))
                                            .endConfig()
                                            .build();


                                    mListView.getAdapter().add(mListView.getAdapter().getItemCount(),card2, false);
                                }  else if (obj.getString("type").equals("2")){


                                    Card card2 = new Card.Builder(getActivity())
                                            .withProvider(new CardProvider())
                                            .setLayout(R.layout.material_small_image_card)
                                            .setTitle(obj.getString("title"))
                                            .setDescription(obj.getString("description"))
                                            .endConfig()
                                            .build();



                                    mListView.getAdapter().add(mListView.getAdapter().getItemCount(),card2, false);

                                }


                                // adding movie to movies array


                            } catch (JSONException e) {
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


        AppController.getInstance().addToRequestQueue(movieReq);

        if (swipeLayout.isRefreshing()){
            swipeLayout.setRefreshing(false);
        }



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
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
              SetupView();
            }
        });





    }




    public void onCreate (
            final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);





    }





}