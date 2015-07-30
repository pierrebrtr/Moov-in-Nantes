package activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;

import com.pandf.moovin.R;

import java.util.List;

import adapter.CustomListAdapter;
import model.Arrets;
import util.Spfav;
import util.Utility;

/**
 * Created by dev on 29/07/2015.
 */
public class ItineraireActivity extends Activity {

    private Toolbar mToolbar;
    AutoCompleteTextView depart;

    Activity activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.themer(ItineraireActivity.this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_itineraire);
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





        depart=(AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);








    }








}
