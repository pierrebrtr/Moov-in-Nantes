package activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.pierre.tan.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

import app.AppController;

/**
 * Created by dev on 19/04/15.
 */
public class MyPreferencesActivity extends ActionBarActivity {
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

    public static class MyPreferenceFragment extends PreferenceFragment{
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

}



