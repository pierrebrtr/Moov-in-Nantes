package activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.example.pierre.tan.R;

/**
 * Created by dev on 19/04/15.
 */
public class MyPreferencesActivity extends PreferenceActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        }



    public static class MyPreferenceFragment extends PreferenceFragment
        {
            @Override
            public void onCreate(final Bundle savedInstanceState)
            {
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
            }
        }

    }
