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
import com.pandf.moovin.R;

import java.util.List;

import app.AppController;
import model.Bicloo;


public class CustomListAdapterBicloo extends BaseAdapter  {
    private Activity activity;
    private LayoutInflater inflater;
    private Context context;

    private List<Bicloo> biclooItems;










    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListAdapterBicloo(Activity activity, List<Bicloo> biclooItems) {


        this.activity = activity;
        this.biclooItems = biclooItems;




    }



    @Override
    public int getCount() {
        return biclooItems.size();
    }

    @Override
    public Object getItem(int location) {
        return biclooItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_rowbicloo, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView adresse = (TextView) convertView.findViewById(R.id.adresse);
        TextView placedispo = (TextView) convertView.findViewById(R.id.placedispo);
        TextView velodispo = (TextView) convertView.findViewById(R.id.velodispo);

        ImageView favoriteImg = (ImageView) convertView.findViewById(R.id.banking);



        Bicloo m = biclooItems.get(position);



        if(m.getIcon().equals("true")){

            favoriteImg.setImageResource(R.drawable.bank);
            favoriteImg.setTag("bank");

        } else if (m.getIcon().equals("false")){


            favoriteImg.setImageResource(R.drawable.nobank);
            favoriteImg.setTag("nobank");
        }

        // getting movie data for the row



        // title
        adresse.setText(m.getAdresse());





        placedispo.setText("Places disponibles: " + m.getPlacedispo());

        velodispo.setText("Vélos disponibles: " + m.getVelodispo());

        return convertView;


    }







}