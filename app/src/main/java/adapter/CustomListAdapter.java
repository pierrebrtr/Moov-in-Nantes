package adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.pierre.tan.R;

import java.util.List;

import app.AppController;
import model.Arrets;
import util.Spfav;


public class CustomListAdapter extends BaseAdapter  {
    private Activity activity;
    private LayoutInflater inflater;
    private Context context;

    private List<Arrets> arretsItems;
    Spfav sharedPreference;








    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListAdapter(Activity activity, List<Arrets> arretsItems) {


        this.activity = activity;
        this.arretsItems = arretsItems;

        sharedPreference = new Spfav();



    }



    @Override
    public int getCount() {
        return arretsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return arretsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView arret = (TextView) convertView.findViewById(R.id.arret);
        TextView ligne = (TextView) convertView.findViewById(R.id.ligne);
        TextView lieu = (TextView) convertView.findViewById(R.id.lieu);



        ImageView favoriteImg = (ImageView) convertView.findViewById(R.id.imgbtn_favorite);

        Arrets m = arretsItems.get(position);


        if (checkFavoriteItem(m)) {
            favoriteImg.setImageResource(R.drawable.ic_heart_red);
            favoriteImg.setTag("red");
            Toast.makeText(activity, "Favoris deja !", Toast.LENGTH_LONG).show();
            Log.d("Request", "sweg");

        } else {
            favoriteImg.setImageResource(R.drawable.ic_heart_white);
            Log.d("Request", "sweg");
            favoriteImg.setTag("grey");

        }


        // getting movie data for the row



        // title
        arret.setText(m.getArret());


        // genre
        String ligneStr = "";
        for (String str : m.getLigne()) {
            ligneStr += str + ", ";
        }
        ligneStr = ligneStr.length() > 0 ? ligneStr.substring(0,
                ligneStr.length() - 2) : ligneStr;
        ligne.setText(ligneStr);

        lieu.setText(m.getLieu());

        return convertView;


    }




    public boolean checkFavoriteItem(Arrets checkProduct) {
        boolean check = false;

        List<Arrets> favorites = sharedPreference.getFavorites(activity);
        if (favorites != null) {
            for (Arrets product : favorites) {
                if (product.equals(checkProduct)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }


}