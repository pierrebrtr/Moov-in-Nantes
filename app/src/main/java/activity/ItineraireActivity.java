package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BasicImageButtonsCardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.pandf.moovin.R;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adapter.CustomListAdapterItineraireItem;
import adapter.ItineraireCardProvider;
import adapter.TransportCardProvider;
import adapter.WalkCardProvider;
import app.AppController;
import model.ItineraireItem;
import util.Utility;

/**
 * Created by dev on 29/07/2015.
 */
public class ItineraireActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Toolbar mToolbar;
    AutoCompleteTextView depart;
    AutoCompleteTextView arrive;
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    private static final String TAG = MainActivity.class.getSimpleName();

    private List<ItineraireItem> itineraireList = new ArrayList<ItineraireItem>();

    Activity activity;
    private String url1 = "https://api.navitia.io/v1/coverage/fr-nw/places?q=moulinais%20nantes";
    final String basicAuth = "Basic " + Base64.encodeToString("a6ca7725-5504-474f-925b-6aa310d48cce:stream53".getBytes(), Base64.NO_WRAP);
    Toolbar toolbar;
    Double firstlat;
    Double firstlon;
    Double secondelat;
    Double secondelon;
    String timeString = "";
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    String currentDateandTime;


    Map<String, String> createBasicAuthHeader(String username, String password) {
        Map<String, String> headerMap = new HashMap<String, String>();

        String credentials = username + ":" + password;
        String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Authorization", "Basic " + base64EncodedCredentials);

        return headerMap;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Utility.themer(ItineraireActivity.this);
        super.onCreate(savedInstanceState);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("fr", "FR"));
      currentDateandTime = sdf.format(new Date());






        setContentView(R.layout.activity_itineraire);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(ItineraireActivity.this, MainActivity.class);

                ItineraireActivity.this.startActivity(myIntent);

            }
        });



        final CustomListAdapterItineraireItem lAdapter = new CustomListAdapterItineraireItem(ItineraireActivity.this, getApplicationContext(), itineraireList);

        depart= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

        depart.setAdapter(lAdapter);


        depart.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                itineraireList.clear();
                lAdapter.notifyDataSetChanged();


                setResearchRequest(s, lAdapter);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                depart.enoughToFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {

                lAdapter.notifyDataSetChanged();

            }
        });


        depart.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {

                TextView textView = (TextView) view.findViewById(R.id.libelle);
                String text = textView.getText().toString();

                TextView textView2 = (TextView) view.findViewById(R.id.lat);
                String lat = textView2.getText().toString();

                TextView textView3 = (TextView) view.findViewById(R.id.lng);
                String lng = textView3.getText().toString();
                depart.setListSelection(pos);
                depart.setText(text);

                firstlat = Double.valueOf(lat);
                Log.d("LATITUDE", String.valueOf(Double.valueOf(lat)));

                firstlon = Double.valueOf(lng);
                Log.d("LONGITUDE", String.valueOf(Double.valueOf(lng)));


            }
        });




        arrive= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);

        arrive.setAdapter(lAdapter);


        arrive.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                itineraireList.clear();
                lAdapter.notifyDataSetChanged();


                setResearchRequest(s, lAdapter);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                arrive.enoughToFilter();

                itineraireList.clear();
                lAdapter.notifyDataSetChanged();


                setResearchRequest(s, lAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

                itineraireList.clear();
                lAdapter.notifyDataSetChanged();


                setResearchRequest(s, lAdapter);

            }
        });


        arrive.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {

                TextView textView = (TextView) view.findViewById(R.id.libelle);
                String text = textView.getText().toString();

                TextView textView2 = (TextView) view.findViewById(R.id.lat);
                String lat = textView2.getText().toString();

                TextView textView3 = (TextView) view.findViewById(R.id.lng);
                String lng = textView3.getText().toString();
                arrive.setListSelection(pos);
                arrive.setText(text);

                secondelat = Double.valueOf(lat);
                Log.d("SECLATITUDE", String.valueOf(Double.valueOf(lat)));

                secondelon = Double.valueOf(lng);
                Log.d("SECLONGITUDE", String.valueOf(Double.valueOf(lng)));


                InputMethodManager imm = (InputMethodManager) ItineraireActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(depart.getWindowToken(), 0);

                String url = "https://api.navitia.io/v1/journeys?from=" + firstlon + ";" + firstlat + "&to=" + secondelon + ";" + secondelat + "&datetime=" + currentDateandTime;


                Log.d("URL", String.valueOf(url));
                final MaterialListView mListView = (MaterialListView) findViewById(R.id.listitinerairephase1);

                mListView.clearAll();

                arrive.dismissDropDown();

                phase1(url);


            }
        });


        ImageButton imageButton = (ImageButton) toolbar.findViewById(R.id.button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Calendar calendar = Calendar.getInstance();

              final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(ItineraireActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
              final   TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(ItineraireActivity.this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(2015, 2017);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);






            }
        });


        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }

    }





    public void setResearchRequest(CharSequence s, final CustomListAdapterItineraireItem lAdapter) {



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "https://api.navitia.io/v1/coverage/fr-nw/places?q=" + s + "%20nantes", null, new Response.Listener<JSONObject>() {


            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                // Parsing json object response
                // response will be a json object

                String test = "";
                Double lat = 0.0;
                Double lng = 0.0;

                JSONArray array = null;

                try {
                    array = response.getJSONArray("places");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    for (int i = 0; i < array.length(); i++) {


                        try {

                            String type = array.getJSONObject(i).getString("embedded_type");



                            test = array.getJSONObject(i).getJSONObject(type).getString("name");


                            lat = array.getJSONObject(i).getJSONObject(type).getJSONObject("coord").getDouble("lat");

                            lng = array.getJSONObject(i).getJSONObject(type).getJSONObject("coord").getDouble("lon");





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ItineraireItem item = new ItineraireItem();

                        item.setArret(test);

                        item.setLng(lng);

                        item.setLat(lat);

                        itineraireList.add(item);




                        lAdapter.notifyDataSetChanged();


                    }

                } catch (NullPointerException e) {
                    // do something other
                }


            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createBasicAuthHeader("a6ca7725-5504-474f-925b-6aa310d48cce", "stream53");
            }
        };


        // Adding request to request queue
        try {
            jsonObjReq.getHeaders();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }


        AppController.getInstance().addToRequestQueue(jsonObjReq);


        lAdapter.notifyDataSetChanged();


    }



    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ItineraireActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    public void phase1(final String urlph1) {
        final MaterialListView mListView = (MaterialListView) findViewById(R.id.listitinerairephase1);
        final ArrayList<String> list = new ArrayList<String>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlph1, null, new Response.Listener<JSONObject>() {


            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                // Parsing json object response
                // response will be a json object
                JSONArray array = null;

                try {
                    array = response.getJSONArray("journeys");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {

                    if (array.length() > 0){

                        for (int i = 0; i < array.length(); i++) {


                            try {


                              String moreinfo = "";


                                String arriveprevue = array.getJSONObject(i).getString("arrival_date_time");

                                DateFormat df1 = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("fr", "FR"));

                                Date result1 = null;
                                try {
                                     result1 = df1.parse(arriveprevue);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                final SimpleDateFormat outputFormatter =
                                        new SimpleDateFormat("HH':'mm ", Locale.FRANCE);

                                final String resultdate = outputFormatter.format(result1);

                                String departprevu = array.getJSONObject(i).getString("departure_date_time");

                                Date result2 = null;
                                try {
                                    result2 = df1.parse(departprevu);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                final SimpleDateFormat outputFormatter2 =
                                        new SimpleDateFormat("dd MMMM HH':'mm", new Locale("fr", "FR"));

                                final String resultdate2 = outputFormatter2.format(result2);

                                    moreinfo = "Nb de transferts : " + array.getJSONObject(i).getInt("nb_transfers");




                                int totalSecs = array.getJSONObject(i).getInt("duration");

                                int hours = totalSecs / 3600;
                                int minutes = (totalSecs % 3600) / 60;
                                int seconds = totalSecs % 60;

                                timeString = "";

                                if (hours == 00) {
                                    timeString = String.format("%02d", minutes);

                                } else {

                                    timeString = String.format("%02d:%02d", hours, minutes);
                                }

                                timeString = timeString + " minutes";

                                Card carditineraire = new Card.Builder(ItineraireActivity.this)
                                        .setTag(i)
                                        .withProvider(ItineraireCardProvider.class)
                                        .setTemps(timeString)
                                        .setDirectionTxt(resultdate2 + " --> " + resultdate)
                                        .setMoreinfo(moreinfo)


                                        .endConfig()
                                        .build();



                                mListView.add(carditineraire);

                                list.add(i, timeString);








                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    } else if (array.length() == 0 || array.length() <= 0) {







                        Card card = new Card.Builder(ItineraireActivity.this)
                                .withProvider(BasicImageButtonsCardProvider.class)
                                .setTitle("Pas de resultat")
                                .setDescription("Une erreur est survenue")
                                .endConfig()
                                .build();

                        mListView.add(card);
                    }



                } catch (NullPointerException e)

            {

            }



            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog


                Card card = new Card.Builder(ItineraireActivity.this)
                        .withProvider(BasicImageButtonsCardProvider.class)

                        .setTitle("Pas de resultat")
                        .setDescription("Une erreur est survenue")
                        .endConfig()
                        .setTag("ERROR")
                        .build();

                mListView.add(card);

            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createBasicAuthHeader("a6ca7725-5504-474f-925b-6aa310d48cce", "stream53");
            }
        };


        // Adding request to request queue
        try {
            jsonObjReq.getHeaders();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }


        AppController.getInstance().addToRequestQueue(jsonObjReq);




        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {

                    if (card.getTag().toString().contains("ERROR")){


                    }else {
                        launchItinerary(Integer.parseInt(card.getTag().toString()), urlph1, list.get(position));
                        Log.d("CARD_TYPE", card.getTag().toString());
                    }

            }

            @Override
            public void onItemLongClick(Card card, int position) {

            }
        });


    }

    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        } else {
            return DATE_TIME_FORMAT.format(date);
        }
    }

    public static Date parseDateTime(String dateStr) {
        try {
            return DATE_TIME_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }




    public void launchItinerary(final int jsonobjectpos, String urlph2, String tempsiti){


// custom dialog
    //    final Dialog dialog = new Dialog(ItineraireActivity.this);
      //  dialog.setContentView(R.layout.dialog_itineraireph2);
        //dialog.setTitle("Itineraire");

        //dialog.show();



        DialogPlus dialogplus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.dialog_itineraireph2))
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setInAnimation(R.anim.abc_fade_in)
                .setHeader(R.layout.header_dialogplus)
                .setOutAnimation(R.anim.abc_fade_out)
                .setExpanded(false)



                .create();
        dialogplus.show();


       TextView textiti = (TextView) dialogplus.getHeaderView().findViewById(R.id.textheadertemps);
        textiti.setText(tempsiti);

        final MaterialListView mListView = (MaterialListView) dialogplus.findViewById(R.id.listitinerairephase2);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlph2, null, new Response.Listener<JSONObject>() {


            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                // Parsing json object response
                // response will be a json object
                JSONArray array = null;

                try {
                    array = response.getJSONArray("journeys");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {


                    String type = "";
                    String heuredepart = "";
                    String heurearrivee = "";
                    String marchedirection = "";
                    String minutes = "";
                    String metre = "";
                    String directionbus = "";
                    String lignebus = "";
                    String arriveebus = "";


                    JSONArray sections = array.getJSONObject(jsonobjectpos).getJSONArray("sections");


                    for (int p = 0; p < sections.length(); p++) {



                        type = sections.getJSONObject(p).getString("type");


                        if (type.contains("street_network")){


                            DateFormat df1 = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("fr", "FR"));

                            Date result1 = null;
                            try {
                                result1 = df1.parse(sections.getJSONObject(p).getString("departure_date_time"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            final SimpleDateFormat outputFormatter =
                                    new SimpleDateFormat("HH':'mm", Locale.FRANCE);

                          heurearrivee =  outputFormatter.format(result1);



                            DateFormat df2 = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("fr", "FR"));

                            Date result2 = null;
                            try {
                                result2 = df2.parse(sections.getJSONObject(p).getString("arrival_date_time"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            final SimpleDateFormat outputFormatter2 =
                                    new SimpleDateFormat("HH':'mm", Locale.FRANCE);

                           String  heurearrivee2 =  outputFormatter2.format(result2);
                            try {
                                Card carditinerairewalk = new Card.Builder(ItineraireActivity.this)
                                        .setTag("MARCHE")
                                        .withProvider(WalkCardProvider.class)
                                        .setHeure(heurearrivee)
                                        .setDirectionTxt("Aller a " + sections.getJSONObject(p).getJSONObject("to").getJSONObject("stop_point").getString("name"))
                                        .setMoreinfo(String.valueOf(sections.getJSONObject(p).getInt("duration") / 60) + " min - " + heurearrivee2)


                                        .endConfig()
                                        .build();


                                mListView.add(carditinerairewalk);
                            } catch (JSONException e) {

                                Card carditinerairewalk = new Card.Builder(ItineraireActivity.this)
                                        .setTag("MARCHE")
                                        .withProvider(WalkCardProvider.class)
                                        .setHeure(heurearrivee)
                                        .setDirectionTxt("Aller a " + sections.getJSONObject(p).getJSONObject("to").getString("name"))
                                        .setMoreinfo(String.valueOf(sections.getJSONObject(p).getInt("duration") / 60) + " min - " + heurearrivee2)


                                        .endConfig()
                                        .build();


                                mListView.add(carditinerairewalk);
                                e.printStackTrace();
                            }


                        } else if (type.contains("public_transport")) {

                            DateFormat df1 = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("fr", "FR"));

                            Date result1 = null;
                            try {
                                result1 = df1.parse(sections.getJSONObject(p).getString("arrival_date_time"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Date result2 = null;
                            try {
                                result2 = df1.parse(sections.getJSONObject(p).getString("departure_date_time"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            final SimpleDateFormat outputFormatter =
                                    new SimpleDateFormat("HH':'mm", Locale.FRANCE);

                            heurearrivee =  outputFormatter.format(result1);

                            heuredepart =  outputFormatter.format(result2);

                            Card carditinerairewalk = new Card.Builder(ItineraireActivity.this)
                                    .setTag("MARCHE")
                                    .withProvider(TransportCardProvider.class)
                                    .setHeureArrive(heurearrivee)
                                    .setHeureDepart(heuredepart)
                                    .setArretDepart(sections.getJSONObject(p).getJSONObject("from").getJSONObject("stop_point").getString("name"))
                                    .setArretArrive(sections.getJSONObject(p).getJSONObject("to").getJSONObject("stop_point").getString("name"))
                                    .setDirection(sections.getJSONObject(p).getJSONObject("display_informations").getString("direction"))
                                    .setLigne(sections.getJSONObject(p).getJSONObject("display_informations").getString("code"))
                                            .endConfig()
                                            .build();


                            mListView.add(carditinerairewalk);


                        }







                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }




        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createBasicAuthHeader("a6ca7725-5504-474f-925b-6aa310d48cce", "stream53");
            }
        };


        // Adding request to request queue
        try {
            jsonObjReq.getHeaders();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }


        AppController.getInstance().addToRequestQueue(jsonObjReq);







    }

String dateset;
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Toast.makeText(ItineraireActivity.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();


        String daystring = "";
        String monthstring = "";
        String yearstring = "";
        yearstring = "" + year;

        if (month < 10){
            monthstring = "0" + month;
            if (day < 10){



                daystring = "0" + day;
            } else if (day >= 10){

                daystring = "" + day;
            }

        } else if (month >= 10){

            monthstring = "" + month;

            if (day < 10){



                daystring = "0" + day;
            } else if (day >= 10){

                daystring = "" + day;
            }

        }

        DateFormat df1 = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("fr", "FR"));

        Date result1 = null;

        String date = yearstring + monthstring + daystring;





         dateset = yearstring + monthstring + daystring + "T";




        Calendar calendar = Calendar.getInstance();

       final   TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(ItineraireActivity.this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        timePickerDialog.setVibrate(true);
        timePickerDialog.setCloseOnSingleTapMinute(false);

        timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);









    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(ItineraireActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();


        if (minute < 10){

            currentDateandTime = dateset + hourOfDay + "0" + minute + "00";
        }else {

            currentDateandTime = dateset + hourOfDay + minute + "00";
        }


        String url = "https://api.navitia.io/v1/journeys?from=" + firstlon + ";" + firstlat + "&to=" + secondelon + ";" + secondelat + "&datetime=" + currentDateandTime;


        Log.d("URL", String.valueOf(url));
        final MaterialListView mListView = (MaterialListView) findViewById(R.id.listitinerairephase1);

        mListView.clearAll();



        phase1(url);


    }
}


