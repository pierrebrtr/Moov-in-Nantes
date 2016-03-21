package activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.pandf.moovin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.ParkingCardProvider;
import app.AppController;
import helper.BlurryParkingFragment;
import helper.SpacesItemDecoration;
import util.SpLite;


public class ParkingFragment extends Fragment {


    String condition = " ";
    String tmp = " ";
    String icon = " ";


    String condition2 = " ";
    String tmp2 = " ";
    String icon2 = " ";
    MaterialListView mListView;
    View coordinatorLayoutView;
    ImageView viewimage;

    List<String> list1;

    private static final String TAG = MainActivity.class.getSimpleName();


    SpLite sharedlite;


    private int dpToPx(final int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =inflater.inflate(R.layout.fragment_parking,container,false);
        coordinatorLayoutView = v.findViewById(R.id.snackbarPosition);

        mListView  = (MaterialListView) v.findViewById(R.id.material_listviewparking);
   viewimage = (ImageView) v.findViewById(R.id.erreur1);

        mListView.addItemDecoration(new SpacesItemDecoration(dpToPx(20)));

        mListView.setClipToPadding(false);
        SetupView();

        return v;

    }


    public void SetupView() {
        list1 = new ArrayList<>();
        viewimage.setVisibility(View.GONE);
        mListView.getAdapter().clearAll();

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();



        if (networkInfo != null && networkInfo.isConnected()) {
            // Create an object for subclass of AsyncTask

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    "http://data.nantes.fr/api/getDisponibiliteParkingsPublics/1.0/39W9VSNCSASEOGV/?output=json", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {


                        JSONArray genreArry = response.getJSONObject("opendata").getJSONObject("answer").getJSONObject("data").getJSONObject("Groupes_Parking").getJSONArray("Groupe_Parking");



                        int parkings = genreArry.length();


                        for (int v = 0; v < parkings; v++) {

                            JSONObject nl = genreArry.getJSONObject(v);




                            String idobject =  nl.getString("IdObj");
                            String dispo = nl.getString("Grp_disponible");
                            String lieuname = nl.getString("Grp_nom");

                            Card carparking = new Card.Builder(getContext())
                                    .setTag("Parking")
                                    .withProvider(new ParkingCardProvider())
                                    .setLayout(R.layout.card_parking)
                                    .setLieu(lieuname)
                                    .setNbplaces(dispo)


                                    .endConfig()
                                    .build();



                            mListView.getAdapter().add(mListView.getAdapter().getItemCount(),carparking, false);




                            list1.add(idobject);







                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
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





        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Log.d("Click", "Clicked");

                FragmentManager fm = getActivity().getFragmentManager();
                String id = list1.get(position);
                BlurryParkingFragment fragment = BlurryParkingFragment.newInstance(10,2.0f,false,false, id);
                fragment.show(fm, String.valueOf(position));

            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {


            }
        });

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







    }




    public void onCreate (
            final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);





    }





}