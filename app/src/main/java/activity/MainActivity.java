package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.pandf.moovin.R;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

import adapter.FragmentDrawer;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import model.Arrets;
import util.Utility;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    public static Arrets arrets = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utility.themer(MainActivity.this);

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);






        AppRate.with(this)
                .setInstallDays(5) // default 10, 0 means install day.
                .setLaunchTimes(10) // default 10
                .setRemindInterval(1) // default 1
                .setShowNeutralButton(false) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);


        arrets = new Arrets();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);



        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        View searchContainer = findViewById(R.id.search_container);




        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        drawerFragment.setFocusableInTouchMode(false);

        // display the first navigation drawer view on app launch
        displayView(0);



    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Voulez-vous quitter l\'application ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent relaunch = new Intent(MainActivity.this, Exiter.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK // CLEAR_TASK requires this
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK // finish everything else in the task
                                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); // hide (remove, in this case) task from recents
                        startActivity(relaunch);
                    }
                }).setNegativeButton("Non", null).show();


    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new ArretsFragment();
                title = "Arrêts";
                break;
            case 2:
                fragment = new GeoFragment();
                title = "Géolocalisation";
                break;
            case 3:
                fragment = new BiclooFragment();
                title = "Bicloos";
                break;
            case 4:
                Intent i = new Intent(MainActivity.this, MyPreferencesActivity.class);
                title = "Paramètres";
                startActivity(i);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }








}
