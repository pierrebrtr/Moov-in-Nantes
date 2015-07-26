package activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.pandf.moovin.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

import app.AppController;
import util.SpLite;
import util.Spfav;

/**
 * Created by dev on 19/04/15.
 */
public class MyPreferencesActivity extends ActionBarActivity {

    SpLite sharedlite;
    private Toolbar mToolbar;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();


        }



    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(MyPreferencesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public class MyPreferenceFragment extends PreferenceFragment{
        String ListPreference;
        boolean CheckboxPreference;
        @Override
        public void onCreate(final Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);


            Preference userButtontheme = (Preference) findPreference("theme");
            userButtontheme.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {

                    int[] mColorChoices=null;
                    String[] color_array = getResources().getStringArray(R.array.default_color_choice_values);


                    if (color_array!=null && color_array.length>0) {
                        mColorChoices = new int[color_array.length];
                        for (int i = 0; i < color_array.length; i++) {
                            mColorChoices[i] = Color.parseColor(color_array[i]);
                        }
                    }





                    ColorPickerDialog colorcalendar = ColorPickerDialog.newInstance(R.string.color_picker_default_title, mColorChoices, 4, 2, 1);

                    //Implement listener to get selected color value
                    colorcalendar.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener(){

                        @Override
                        public void onColorSelected(int color) {

                            


                        }

                    });

                    colorcalendar.show(getFragmentManager(),"cal");

                    return true;
                }
            });

            Preference userButton = (Preference) findPreference("user");
            userButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Intent intent = new Intent(getActivity(), ChangelogActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            Preference userButtona = (Preference) findPreference("propos");
            userButtona.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Intent intent = new Intent(getActivity(), AproposActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("lite");

            checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("MyApp", "Pref " + preference.getKey() + " changed to " + newValue.toString());





                    sharedlite = new SpLite();

                    if (newValue.toString() == "true") {


                    sharedlite.setPref(getActivity(), "true");




                    } else {


                        sharedlite.setPref(getActivity(), "false");




                    }



                    return true;
                }



            });

            Preference userButtone = (Preference) findPreference("cache");
            userButtone.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    String url = "https://open.tan.fr/ewp/arrets.json";
                    AppController.getInstance().getRequestQueue().getCache().remove(url);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Le cache a été vidé.", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            Preference userButtonee = (Preference) findPreference("library");
            userButtonee.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Disponible prochainement.", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);


        return true;//return true so that the menu pop up is opened

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_search){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.pandf.moovin"));
            startActivity(browserIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}



