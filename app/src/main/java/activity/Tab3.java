package activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;
import helper.SpacesItemDecoration;


public class Tab3 extends Fragment {

    MaterialListView mListView;
    int state = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab_3,container,false);
        mListView  = (MaterialListView) v.findViewById(R.id.material_listviewtab3);
        mListView.addItemDecoration(new SpacesItemDecoration(dpToPx(20)));

        mListView.setClipToPadding(false);
        TanDirect();





        return v;






    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_tab3, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
setHasOptionsMenu(true);





    }

    private int dpToPx(final int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public void Tanactus() {



    }

    public void TanDirect() {


        mListView.getAdapter().clear();





        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Create an object for subclass of AsyncTask

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://pierre.hellophoto.fr/tweets/tweet_json?screen_name=infotrafic_tan",
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




                                    Card card = new Card.Builder(getActivity())
                                            .withProvider(new CardProvider())
                                            .setLayout(R.layout.material_small_image_card)
                                            .setTitle("Trafic info Tan")
                                            .setSubtitle(obj.getString("created_at"))
                                            .setSubtitleColor(getResources().getColor(R.color.cardview_dark_background))
                                            .setDescription(obj.getString("text"))
                                            .setDrawable(obj.getJSONObject("user").getString("profile_image_url"))
                                            .endConfig()
                                            .build();

                                    mListView.getAdapter().add(mListView.getAdapter().getItemCount(),card, false);




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

            AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.actus) {


            if (state == 1){

                state++;
                Tanactus();
                item.setIcon(R.drawable.ic_clock_white_48dp);


            } else if (state == 2) {

                state--;
                TanDirect();
                item.setIcon(R.drawable.ic_newspaper_white_48dp);

            }



            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onCreate (
            final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);





    }





}