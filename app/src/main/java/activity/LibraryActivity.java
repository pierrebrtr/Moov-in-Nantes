package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.pandf.moovin.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

/**
 * Created by dev on 19/04/15.
 */
public class LibraryActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_library);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = new Intent(getApplicationContext(), LibsActivity.class);

        i.putExtra(Libs.BUNDLE_TITLE, "Crédits librairies");
        i.putExtra(Libs.BUNDLE_THEME, R.style.MyMaterialTheme);
        startActivity(i);

    }


}