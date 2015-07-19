package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Arrets;


public class SpLite {


    public static final String PREFS_LITE = "PRODUCT_LITE";
    public static final String FAVORITESLITE = "Product_LITE";


    public SpLite() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context,String favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_LITE,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonLite = gson.toJson(favorites);

        editor.putString(FAVORITESLITE, jsonLite);

        editor.commit();
        editor.apply();
    }



    public void setPref(Context context,String string) {

        String favorites = getFavorites(context);

        favorites = string;
        saveFavorites(context, favorites);


    }

    public String getFavorites(Context context) {
        SharedPreferences settings;
        String favorites;

        settings = context.getSharedPreferences(PREFS_LITE,
                Context.MODE_PRIVATE);

            if (settings.contains(FAVORITESLITE)){



            String jsonFavorites = settings.getString(FAVORITESLITE, null);
            Gson gson = new Gson();
            String favoriteItems = gson.fromJson(jsonFavorites,
                    String.class);

            favorites = favoriteItems;

            } else {

                favorites = "false";

            }

        return (String) favorites;
    }
}