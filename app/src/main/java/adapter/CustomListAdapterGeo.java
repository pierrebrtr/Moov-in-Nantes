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
import model.Arretsgeo;


public class CustomListAdapterGeo extends BaseAdapter  {
    private Activity activity;
    private LayoutInflater inflater;
    private Context context;
    private boolean showicon;

    private List<Arretsgeo> geoItems;









    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListAdapterGeo(Activity activity, List<Arretsgeo> geoItems) {


        this.activity = activity;
        this.geoItems = geoItems;
        this.showicon = showicon;




    }



    @Override
    public int getCount() {
        return geoItems.size();
    }

    @Override
    public Object getItem(int location) {
        return geoItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_rowgeo, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView arret = (TextView) convertView.findViewById(R.id.arretgeo);
        TextView ligne = (TextView) convertView.findViewById(R.id.lignegeo);
        TextView lieu = (TextView) convertView.findViewById(R.id.lieugeo);
        TextView geo = (TextView) convertView.findViewById(R.id.geometre);



        ImageView favoriteImg = (ImageView) convertView.findViewById(R.id.imgbtn_favorite);

        Arretsgeo m = geoItems.get(position);






        // getting movie data for the row



        // title
        arret.setText(m.getArret());
        geo.setText(m.getGeo());

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







}