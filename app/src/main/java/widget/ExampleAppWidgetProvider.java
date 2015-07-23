package widget;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pandf.moovin.R;

import activity.ArretsFragment;
import activity.MainActivity;
import activity.TempsActivity;
import model.Arrets;


public class ExampleAppWidgetProvider extends AppWidgetProvider {


    public static final String ACTION_TEXT_CHANGED = "com.pandf.moovin.TEXT_CHANGED";
    public String sens = "";
    public String libelle = "";



    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_TEXT_CHANGED)) {
            // handle intent here
            String s = intent.getStringExtra("ID");
            Log.d("DEBUG", s);
            sens = s;



            String l = intent.getStringExtra("ID2");
            Log.d("DEBUG", l);
            libelle = l;
        }
    }


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Log.i("ExampleWidget",  "Updating widgets " + Arrays.asList(appWidgetIds));

        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];








            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, TempsActivity.class);



            intent.putExtra("text", sens);
            intent.putExtra("libelle", libelle);



            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1);










        }
    }
}
