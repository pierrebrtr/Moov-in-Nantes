package adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pandf.moovin.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import app.AppController;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Meteo;
import util.Spfav;


public class CustomListAdapterMeteo extends BaseAdapter  {
    private Activity activity;
    private LayoutInflater inflater;
    private Context context;
    private boolean showicon;

    private List<Meteo> arretsItems;
    Spfav sharedPreference;








    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListAdapterMeteo(Activity activity, List<Meteo> meteoItems) {


        this.activity = activity;
        this.arretsItems = meteoItems;

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
            convertView = inflater.inflate(R.layout.listmeteofragment, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();




        TextView temps = (TextView) convertView.findViewById(R.id.tempsheader);
        TextView minetmax = (TextView) convertView.findViewById(R.id.temperatureheader);
    CircleImageView imageCity = (CircleImageView) convertView.findViewById(R.id.iconheader);


        TextView jour = (TextView) convertView.findViewById(R.id.jour);






        Meteo m = arretsItems.get(position);

        temps.setText(m.getTemps());

        minetmax.setText(m.getMinetmax());

        jour.setText(m.getJour());
        URL url = null;
        try {
            url = new URL(m.getImage());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageCity.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return convertView;


    }







}