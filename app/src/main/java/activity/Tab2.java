package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        final View c = inflater.inflate(R.layout.row_card,container,false);



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "http://www.prevision-meteo.ch/services/json/nantes", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object










                    JSONObject object = response.getJSONObject("fcst_day_0");

                    condition = object.getString("condition");

                    icon = object.getString("icon");





                    Card card = new Card(getActivity(), R.layout.row_card);






// Create a CardHeader
                    CardHeader header = new CardHeader(getActivity());
                    header.setTitle("Météo du jour :");

                    card.setTitle(condition);


                    CardThumbnail thumb = new CardThumbnail(getActivity());
                    thumb.setDrawableResource(R.drawable.ic_heart_red);

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