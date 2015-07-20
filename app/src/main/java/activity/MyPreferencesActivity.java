package activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
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
                    Intent i = new Intent(getActivity().getApplicationContext(), LibsActivity.class);
//Pass the fields of your application to the lib so it can find all external lib information
                    i.putExtra(Libs.BUNDLE_FIELDS, Libs.toStringArray(R.string.class.getFields()));
                    i.putExtra(Libs.BUNDLE_VERSION, true);
                    i.putExtra(Libs.BUNDLE_LICENSE, true);
                    i.putExtra(Libs.BUNDLE_TITLE, "Crédits librairies");
                    i.putExtra(Libs.BUNDLE_THEME, R.style.Theme_AppCompat);
                    startActivity(i);
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



