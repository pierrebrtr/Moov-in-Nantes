package util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

        if(theme == "lime"){

            activity.setTheme(R.style.lime);

        } else if(theme == "red"){

            activity.setTheme(R.style.red);

        } else if(theme == "green"){

            activity.setTheme(R.style.green);

        }else if(theme == "pink"){

            activity.setTheme(R.style.pink);

        }else if(theme == "purple"){

            activity.setTheme(R.style.purple);

        }else if(theme == "brown"){

            activity.setTheme(R.style.brown);

        }else if(theme == "default"){

            activity.setTheme(R.style.defaut);

        }else if(theme == "indigo"){

            activity.setTheme(R.style.indigo);

        }

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


        }

    }





}
