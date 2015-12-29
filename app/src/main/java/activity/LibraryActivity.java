package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.mikepenz.aboutlibraries.LibsBuilder;
import com.pandf.moovin.BuildConfig;
import com.pandf.moovin.MapsActivity;
import com.pandf.moovin.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.ui.LibsActivity;
import util.Utility;

/**
 * Created by dev on 19/04/15.
 */
public class LibraryActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    public void onCreate(Bundle savedInstanceState) {

        Utility.themer(LibraryActivity.this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_library);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new LibsBuilder()
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT)
                //start the activity
                .start(this);

    }


}