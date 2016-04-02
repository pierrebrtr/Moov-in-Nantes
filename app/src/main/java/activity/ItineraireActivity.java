package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.pandf.moovin.R;

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
public class ItineraireActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

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

    boolean started = false;

    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    boolean hasposition = false;
    Double myposlat;
    Double myposlong;


    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    String tempurl;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE2 = 2;

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
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("fr", "FR"));
      currentDateandTime = sdf.format(new Date());






        setContentView(R.layout.activity_itineraire);
        toolbar = (Toolbar) findViewById(R.id.toolbar);



        buildGoogleApiClient();

        LinearLayout layouttoolbar = (LinearLayout) findViewById(R.id.toolbaritinerary);

        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        layouttoolbar.startAnimation(slide_down);

        setSupportActionBar(toolbar);





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
                                            firstlon = Double.valueOf(lng);
                                    Log.d("LATITUDE", String.valueOf(Double.valueOf(lat)));

                }});






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
                   public void afterTextChanged(Editable s) {

                   }

                   @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                arrive.enoughToFilter();
                           }
        });


        arrive.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                              @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos,
                                                                                     long id) {


                                  if (pos == 0) {

                                      buildGoogleApiClient();

                                  }

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

                                  mListView.getAdapter().clearAll();
                                  mListView.getAdapter().notifyDataSetChanged();
                                  arrive.dismissDropDown();
                                  started = true;
                                  phase1(url);
                              }

                              });

        final Animation anim_exchange = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_exchange);


        final ImageButton imageButton2 = (ImageButton) findViewById(R.id.button2);

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageButton2.startAnimation(anim_exchange);

                arrive.dismissDropDown();
                depart.dismissDropDown();

                String tempstr1;
                String tempstr2;

                Double tempsdbl1;
                Double tempsdbl2;


              tempstr1 = arrive.getText().toString();
                tempsdbl1 = secondelat;
                tempsdbl2 = secondelon;


                arrive.setText(depart.getText().toString());
                secondelat = firstlat;
                secondelon = firstlon;

                depart.setText(tempstr1);
                firstlat = tempsdbl1;
                firstlon = tempsdbl2;

                String url = "https://api.navitia.io/v1/journeys?from=" + firstlon + ";" + firstlat + "&to=" + secondelon + ";" + secondelat + "&datetime=" + currentDateandTime;
                final MaterialListView mListView = (MaterialListView) findViewById(R.id.listitinerairephase1);

                mListView.getAdapter().clearAll();
                mListView.getAdapter().notifyDataSetChanged();
                arrive.dismissDropDown();
                depart.dismissDropDown();
                started = true;
                phase1(url);








            }
        });



        ImageButton imageButton = (ImageButton) findViewById(R.id.button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog.newInstance(ItineraireActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");

            }
        });

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.favorite);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                String url = "https://api.navitia.io/v1/journeys?from=" + firstlon + ";" + firstlat + "&to=" + secondelon + ";" + secondelat + "&datetime=" + currentDateandTime;


                Log.d("URL", String.valueOf(url));
                final MaterialListView mListView = (MaterialListView) findViewById(R.id.listitinerairephase1);

                mListView.getAdapter().clearAll();
                mListView.getAdapter().notifyDataSetChanged();


                InputMethodManager imm = (InputMethodManager) ItineraireActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(depart.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(arrive.getWindowToken(), 0);

                arrive.dismissDropDown();
                depart.dismissDropDown();


                started = true;


                phase1(url);
            }
        });




    }





    public void setResearchRequest(CharSequence s, final CustomListAdapterItineraireItem lAdapter) {


        if (hasposition) {

            if (itineraireList.isEmpty() != true) {
                itineraireList.remove(0);
            }

            ItineraireItem item2 = new ItineraireItem();
            item2.setArret("Ma position");
            item2.setLat(myposlat);
            item2.setLng(myposlong);

            itineraireList.add(0, item2);

        }


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
        mListView.getAdapter().clear();
        mListView.getAdapter().notifyDataSetChanged();
        final ArrayList<String> list = new ArrayList<String>();
        tempurl = urlph1;
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

                                    moreinfo = "Transferts : " + array.getJSONObject(i).getInt("nb_transfers");




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
                                        .withProvider(new ItineraireCardProvider())
                                        .setLayout(R.layout.card_layout_itiinfo)

                                        .setTemps(timeString)
                                        .setDirectionTxt(resultdate2 + " → " + resultdate)
                                        .setMoreinfo(moreinfo)


                                        .endConfig()
                                        .build();



                                mListView.getAdapter().add(mListView.getAdapter().getItemCount(),carditineraire, false);

                                list.add(i, timeString);








                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    } else if (array.length() == 0 || array.length() <= 0) {







                        Card card = new Card.Builder(ItineraireActivity.this)
                                .withProvider(new CardProvider())
                                .setLayout(R.layout.material_basic_image_buttons_card_layout)
                                .setTitle("Pas de resultat")
                                .setDescription("Une erreur est survenue")
                                .endConfig()
                                .build();

                        mListView.getAdapter().add(mListView.getAdapter().getItemCount(),card, false);

                        mListView.getAdapter().notifyDataSetChanged();
                    }



                } catch (NullPointerException e)

            {

            }



            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                // hide the progress dialog




                Card card = new Card.Builder(ItineraireActivity.this)
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_basic_image_buttons_card_layout)
                        .setTitle("Pas de resultat")
                        .setDescription("Une erreur est survenue")
                        .endConfig()
                        .setTag("ERROR")
                        .build();

                mListView.getAdapter().add(mListView.getAdapter().getItemCount(),card, false);

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



        mListView.getAdapter().notifyDataSetChanged();



       setListnerUpdate(list);




        mListView.getAdapter().notifyDataSetChanged();


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

    String tempsitinerairetemp;



    public void setListnerUpdate(final ArrayList<String> list) {

        final MaterialListView mListView = (MaterialListView) findViewById(R.id.listitinerairephase1);




        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {

                if (card.getTag().toString().contains("ERROR")){


                }else {


                    launchItinerary(Integer.parseInt(card.getTag().toString()), tempurl);
                    Log.d("CARD_TYPE", card.getTag().toString());
                }

            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {

            }

        });
    }


    public void launchItinerary(final int jsonobjectpos, String urlph2){


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

textiti.setText("");

        final MaterialListView mListView = (MaterialListView) dialogplus.findViewById(R.id.listitinerairephase2);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, tempurl, null, new Response.Listener<JSONObject>() {



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
                    mListView.getAdapter().clear();

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
                                        .withProvider(new WalkCardProvider())
                                        .setLayout(R.layout.card_layout_walkmode)
                                        .setHeure(heurearrivee)
                                        .setDirectionTxt("Aller à " + sections.getJSONObject(p).getJSONObject("to").getJSONObject("stop_point").getString("name"))
                                        .setMoreinfo(String.valueOf(sections.getJSONObject(p).getInt("duration") / 60) + " min - " + heurearrivee2)


                                        .endConfig()
                                        .build();


                                mListView.getAdapter().add(mListView.getAdapter().getItemCount(),carditinerairewalk, false);
                            } catch (JSONException e) {

                                Card carditinerairewalk = new Card.Builder(ItineraireActivity.this)
                                        .setTag("MARCHE")
                                        .withProvider(new WalkCardProvider())
                                        .setLayout(R.layout.card_layout_walkmode)
                                        .setHeure(heurearrivee)
                                        .setDirectionTxt("Aller à " + sections.getJSONObject(p).getJSONObject("to").getString("name"))
                                        .setMoreinfo(String.valueOf(sections.getJSONObject(p).getInt("duration") / 60) + " min - " + heurearrivee2)


                                        .endConfig()
                                        .build();


                                mListView.getAdapter().add(mListView.getAdapter().getItemCount(),carditinerairewalk, false);
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
                                    .withProvider(new TransportCardProvider())
                                    .setLayout(R.layout.card_layout_transportmode)
                                    .setHeureArrive(heurearrivee)
                                    .setHeureDepart(heuredepart)
                                    .setArretDepart(sections.getJSONObject(p).getJSONObject("from").getJSONObject("stop_point").getString("name"))
                                    .setArretArrive(sections.getJSONObject(p).getJSONObject("to").getJSONObject("stop_point").getString("name"))
                                    .setDirection(sections.getJSONObject(p).getJSONObject("display_informations").getString("direction"))
                                    .setLigne(sections.getJSONObject(p).getJSONObject("display_informations").getString("code"))
                                            .endConfig()
                                            .build();


                            mListView.getAdapter().add(mListView.getAdapter().getItemCount(),carditinerairewalk, false);


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

        Log.d("DATE", String.valueOf(month));

        String daystring = "";
        String monthstring = "";
        String yearstring = "";
        yearstring = "" + year;

        month++;
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

        TimePickerDialog.newInstance(ItineraireActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");


    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {


        if (minute < 10){

            currentDateandTime = dateset + hourOfDay + "0" + minute + "00";
        }else {

            currentDateandTime = dateset + hourOfDay + minute + "00";
        }


        String url = "https://api.navitia.io/v1/journeys?from=" + firstlon + ";" + firstlat + "&to=" + secondelon + ";" + secondelat + "&datetime=" + currentDateandTime;


        Log.d("URL", String.valueOf(url));

        if (started) {
            final MaterialListView mListView = (MaterialListView) findViewById(R.id.listitinerairephase1);

            mListView.getAdapter().clearAll();
            mListView.getAdapter().notifyDataSetChanged();


            phase1(url);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ItineraireActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);



        if (mLastLocation != null) {

            hasposition = true;

            myposlat = mLastLocation.getLatitude();


            myposlong = mLastLocation.getLongitude();


        } else {

        }

    }



    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
}


