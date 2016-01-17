package util;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pandf.moovin.R;

public class Utility {

    public static final String PREFS_COLOR = "PRODUCT_THEME";
    public static final String THEME = "Product_theme";
    private static String theme;





    public static void themer(Activity activity){



        theme = PreferenceManager.getDefaultSharedPreferences(activity).getString(THEME,
                "default");



        Log.d("COLORCHOOSED", theme);


        switch (theme){
            default:
            case "default":
                activity.setTheme(R.style.defaut);
                break;
            case "lime":
                activity.setTheme(R.style.lime);
                break;
            case "red":
                activity.setTheme(R.style.red);
                break;
            case "green":
                activity.setTheme(R.style.green);
                break;
            case "pink":
                activity.setTheme(R.style.pink);
                break;
            case "purple":
                activity.setTheme(R.style.purple);
                break;
            case "brown":
                activity.setTheme(R.style.brown);
                break;
            case "indigo":
                activity.setTheme(R.style.indigo);
                break;
            case "cyan":
                activity.setTheme(R.style.cyan);
                break;


        }

    }





}
