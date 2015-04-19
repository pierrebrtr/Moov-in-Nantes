package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Arrets;


public class Spfav {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public Spfav() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Arrets> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Arrets product) {
        List<Arrets> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Arrets>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Arrets product) {
        ArrayList<Arrets> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Arrets> getFavorites(Context context) {
        SharedPreferences settings;
        List<Arrets> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Arrets[] favoriteItems = gson.fromJson(jsonFavorites,
                    Arrets[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Arrets>(favorites);
        } else
            return null;

        return (ArrayList<Arrets>) favorites;
    }
}