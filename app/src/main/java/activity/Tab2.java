package activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.pandf.moovin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.AppController;
import it.gmariotti.cardslib.library.cards.actions.BaseSupplementalAction;
import it.gmariotti.cardslib.library.cards.actions.IconSupplementalAction;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;

public class Tab2 extends Fragment {


    String condition = " ";
    String tmp = " ";
    String icon = " ";

    private static final String TAG = MainActivity.class.getSimpleName();
    private AnimatedCircleLoadingView animatedCircleLoadingView;






    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =inflater.inflate(R.layout.tab_2,container,false);








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







                    animatedCircleLoadingView = (AnimatedCircleLoadingView) getActivity().findViewById(R.id.circle_loading_view);
                    animatedCircleLoadingView.startIndeterminate();

                    buildCardDiscover(getActivity());

                    CardView cardView2 = (CardView) v.findViewById(R.id.carddemo);
                    cardView2.setVisibility(View.VISIBLE);




                    JSONObject object = response.getJSONObject("current_condition");


                    tmp = object.getString("tmp");

                    condition = object.getString("condition");



                    icon = object.getString("icon");



                    Card card = new Card(getActivity(), R.layout.row_card);

// Create a CardHeader
                    CardHeader header = new CardHeader(getActivity());
                    header.setTitle("En direct du ciel :");

                    card.setTitle(condition +" ("+ tmp +"°)");



                    CardThumbnail thumb = new CardThumbnail(getActivity());
                    thumb.setUrlResource(icon);

                    card.addCardThumbnail(thumb);


// Add Header to card
                    card.addCardHeader(header);

// Set card in the cardView
                    CardView cardView = (CardView) v.findViewById(R.id.carddemo);
                    cardView.setCard(card);












animatedCircleLoadingView.stopOk();
                   





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


            Toast.makeText(getActivity().getApplicationContext(),
                    "Pas de connexion internet",
                    Toast.LENGTH_LONG).show();






            Card carddiscover = new Card(getActivity(), R.layout.row_card);
// Create a CardHeader
            CardHeader headerdiscover = new CardHeader(getActivity());
            headerdiscover.setTitle("Découvrir");

            carddiscover.setTitle("Venez découvrir quelques endroits magnifiques de Nantes");



            CardThumbnail thumbdiscover = new CardThumbnail(getActivity());

// Add Header to card
            carddiscover.addCardHeader(headerdiscover);

// Set card in the cardView
            CardView cardViewdiscover = (CardView) v.findViewById(R.id.carddiscover);
            cardViewdiscover.setCard(carddiscover);






            Card card = new Card(getActivity(), R.layout.row_card);


            CardHeader header = new CardHeader(getActivity());
            header.setTitle("Erreur");

            card.setTitle("Vérifiez votre connection Internet");



            CardThumbnail thumb = new CardThumbnail(getActivity());
            thumb.setDrawableResource(R.drawable.ic_cloud);

            card.addCardThumbnail(thumb);

// Add Header to card
            card.addCardHeader(header);

// Set card in the cardView
            CardView cardView = (CardView) v.findViewById(R.id.carddemo);
            cardView.setCard(card);





        }


        // Create a Card

        return v;



    }



    public void createCard(View c){






    }



    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);







    }




    public void onCreate (
            final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);




    }

    private void buildCardDiscover(Activity activicty) {


        Card carddiscover = new Card(activicty, R.layout.row_card);
// Create a CardHeader
        CardHeader headerdiscover = new CardHeader(activicty);
        headerdiscover.setTitle("Découvrir");

        carddiscover.setTitle("Venez découvrir quelques endroits magnifiques de Nantes");



        CardThumbnail thumbdiscover = new CardThumbnail(activicty);

// Add Header to card
        carddiscover.addCardHeader(headerdiscover);

// Set card in the cardView
        CardView cardViewdiscover = (CardView) getView().findViewById(R.id.carddiscover);
        cardViewdiscover.setCard(carddiscover);



    }




}