package activity;

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
import com.example.pierre.tan.R;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;

public class Tab2 extends Fragment {


    String condition = " ";
    String icon = " ";

    private static final String TAG = MainActivity.class.getSimpleName();





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





                    CardView cardView2 = (CardView) v.findViewById(R.id.carddemo);
                    cardView2.setVisibility(View.VISIBLE);




                    JSONObject object = response.getJSONObject("fcst_day_0");

                    condition = object.getString("condition");

                    icon = object.getString("icon");



                    Card card = new Card(getActivity(), R.layout.row_card);






// Create a CardHeader
                    CardHeader header = new CardHeader(getActivity());
                    header.setTitle("Météo du jour :");

                    card.setTitle(condition);



                    CardThumbnail thumb = new CardThumbnail(getActivity());
                    thumb.setUrlResource(icon);

                    card.addCardThumbnail(thumb);

// Add Header to card
                    card.addCardHeader(header);

// Set card in the cardView
                    CardView cardView = (CardView) v.findViewById(R.id.carddemo);
                    cardView.setCard(card);










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




}