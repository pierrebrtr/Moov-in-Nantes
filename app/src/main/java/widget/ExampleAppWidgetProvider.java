package widget;



import java.math.BigInteger;
import java.security.SecureRandom;
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
import android.os.Bundle;
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



    public static final String PREFS_WIDGET = "PREF_WIDGET";
    public static final String PREF = "PREF_TEXT1";
    public static final String PREF2 = "PREF_TEXT2";

    String appid;


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Bundle extras = intent.getExtras();
        if (intent.getAction().equals(ACTION_TEXT_CHANGED)) {
            // handle intent here
            String s = intent.getStringExtra("ID");
            Log.d("DEBUG", s);
            sens = s;



            String l = intent.getStringExtra("ID2");
            Log.d("DEBUG", l);
            libelle = l;


            int[] appWidgetIds = extras
                    .getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (appWidgetIds.length > 0) {
                this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);//here you can call onUpdate method, and update your views as you wish
            }









        }
    }


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Log.i("ExampleWidget", "Updating widgets " + Arrays.asList(appWidgetIds));

        // Perform this loop procedure for each App Widget that belongs to this
        // provider


        for (int i = 0; i < N; i++) {

            int appWidgetId = appWidgetIds[i];




            String text1 = "";
            String text2 = "";

            text1 = getWidgetNewsCategory(context, appWidgetId);
             text2 = getWidgetNewsCategory2(context, appWidgetId);


            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, TempsActivity.class);


            intent.removeExtra("text");

            intent.removeExtra("libelle");

            Log.d("TEXT1", text1);
            Log.d("TEXT2", text2);


            intent.putExtra("text", text1);
            intent.putExtra("libelle", text2);



            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1);







            views.setTextViewText(R.id.textViewwidget, text1);

            views.setTextViewText(R.id.textViewwidget2, text2);



            //  Attach an on-click listener to the clock

            views.setOnClickPendingIntent(R.id.layoutwidget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);







        }
    }





    public String getWidgetNewsCategory(Context context, int appWidgetId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFS_WIDGET + String.valueOf(appWidgetId),
                Context.MODE_PRIVATE);
        Log.d("RETURN TEXT 1", sharedPreferences.getString(PREF + String.valueOf(appWidgetId), null) + "null ?");
        Log.d("Appid provider", String.valueOf(appWidgetId));

        return sharedPreferences.getString(
                PREF + String.valueOf(appWidgetId), "");






    }



    public String getWidgetNewsCategory2(Context context, int appWidgetId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFS_WIDGET + String.valueOf(appWidgetId),
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(
                PREF2 + String.valueOf(appWidgetId), "");
    }
}
