package adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.pierre.tan.R;

import java.util.List;

import app.AppController;
import model.Arrets;
import util.Spfav;


public class CustomListAdapter extends BaseAdapter  {
    private Activity activity;
    private LayoutInflater inflater;

    private List<Arrets> arretsItems;
    Spfav sharedPreference;








    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListAdapter(Activity activity, List<Arrets> arretsItems) {
        this.activity = activity;
        this.arretsItems = arretsItems;





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
        ImageView image = (ImageView) convertView.findViewById(R.id.imgbtn_favorite);


        Arrets product = (Arrets) getItem(position);

        if (checkFavoriteItem(product)) {
            image.setImageResource(R.drawable.ic_heart_red);
            image.setTag("red");
        } else {
            image.setImageResource(R.drawable.ic_heart_white);
           image.setTag("grey");
        }

        // getting movie data for the row
        Arrets m = arretsItems.get(position);


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
        sharedPreference = new Spfav();
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