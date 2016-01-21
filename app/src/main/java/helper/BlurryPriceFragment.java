package helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.google.android.gms.maps.MapView;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.PriceProvider;
import app.AppController;
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;

/**
 * Created by pierre on 19/01/2016.
 */
public class BlurryPriceFragment extends BlurDialogFragment  {


    MaterialListView mListView;
    private static View rootView;
    MapView mapView;


    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String BUNDLE_KEY_DOWN_SCALE_FACTOR = "bundle_key_down_scale_factor";

    /**
     * Bundle key used to start the blur dialog with a given blur radius (int).
     */
    private static final String BUNDLE_KEY_BLUR_RADIUS = "bundle_key_blur_radius";

    /**
     * Bundle key used to start the blur dialog with a given dimming effect policy.
     */
    private static final String BUNDLE_KEY_DIMMING = "bundle_key_dimming_effect";

    /**
     * Bundle key used to start the blur dialog with a given debug policy.
     */
    private static final String BUNDLE_KEY_DEBUG = "bundle_key_debug_effect";

    private static final String BUNDLE_KEY_PRICE = "bundle_key_price";

    private int mRadius;
    private float mDownScaleFactor;
    private boolean mDimming;
    private boolean mDebug;
    private String urlprice;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param radius          blur radius.
     * @param downScaleFactor down scale factor.
     * @param dimming         dimming effect.
     * @param debug           debug policy.
     * @return well instantiated fragment.
     */
    public static BlurryPriceFragment newInstance(int radius,
                                                  float downScaleFactor,
                                                  boolean dimming,
                                                  boolean debug, String urlprice) {
        BlurryPriceFragment fragment = new BlurryPriceFragment();
        Bundle args = new Bundle();
        args.putInt(
                BUNDLE_KEY_BLUR_RADIUS,
                radius
        );
        args.putFloat(
                BUNDLE_KEY_DOWN_SCALE_FACTOR,
                downScaleFactor
        );
        args.putBoolean(
                BUNDLE_KEY_DIMMING,
                dimming
        );
        args.putBoolean(
                BUNDLE_KEY_DEBUG,
                debug
        );
        args.putString( BUNDLE_KEY_PRICE, urlprice);

        fragment.setArguments(args);

        return fragment;
    }


    private int dpToPx(final int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();

        mRadius = args.getInt(BUNDLE_KEY_BLUR_RADIUS);
        mDownScaleFactor = args.getFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR);
        mDimming = args.getBoolean(BUNDLE_KEY_DIMMING);
        mDebug = args.getBoolean(BUNDLE_KEY_DEBUG);
       urlprice = args.getString(BUNDLE_KEY_PRICE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_blurrypricedialog, null);
        builder.setView(view);

        mListView  = (MaterialListView) view.findViewById(R.id.material_listview);
        mListView.addItemDecoration(new SpacesItemDecoration(dpToPx(20)));
        mListView.setClipToPadding(false);

setupMarker();
        return builder.create();
    }


    public void setupMarker() {
        mListView.getAdapter().clearAll();

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();



        if (networkInfo != null && networkInfo.isConnected()) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    urlprice, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {


                    try {


                        JSONArray genreArry = response.getJSONArray("data");

                        int parkings = genreArry.length();


                        for (int v = 0; v < parkings; v++) {

                            JSONObject nl = genreArry.getJSONObject(v);

                            String nameprice = nl.getString("LIBELLE_TARIF");
                            String price = String.valueOf(nl.getDouble("MONTANT"));

                            Card card2 = new Card.Builder(getActivity())
                                    .withProvider(new PriceProvider())
                                    .setLayout(R.layout.card_price)
                                    .setTitleprice(nameprice)
                                    .setPrice(price + " €")
                                    .endConfig()
                                    .build();


                            mListView.getAdapter().add(mListView.getAdapter().getItemCount(),card2, false);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("ERROR", "Error: " + error.getMessage());

                }
            });

            AppController.getInstance().addToRequestQueue(jsonObjReq);


        }



    }




    @Override
    protected boolean isDebugEnable() {
        return mDebug;
    }

    @Override
    protected boolean isDimmingEnable() {
        return mDimming;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    protected float getDownScaleFactor() {
        return mDownScaleFactor;
    }

    @Override
    protected int getBlurRadius() {
        return mRadius;
    }



}
