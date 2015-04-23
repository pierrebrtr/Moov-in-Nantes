package activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.pandf.moovin.R;

/**
 * Created by dev on 19/04/15.
 */
public class ChangelogActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_changelog);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View searchContainer = findViewById(R.id.search_container);
        final EditText toolbarSearchView = (EditText) findViewById(R.id.search);
        ImageView searchClearButton = (ImageView) findViewById(R.id.search_clear);
        searchClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSearchView.setText("");
            }
        });
        searchContainer.setVisibility(View.GONE);

    }


}