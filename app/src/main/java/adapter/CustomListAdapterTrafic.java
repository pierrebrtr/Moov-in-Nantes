package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pandf.moovin.R;

import java.util.List;

import app.AppController;
import model.Trafic;

/**
 * Created by dev on 25/04/15.
 */
public class CustomListAdapterTrafic extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private Context context;

    private List<Trafic> traficItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListAdapterTrafic(Activity activity, List<Trafic> biclooItems) {


        this.activity = activity;
        this.traficItems = traficItems;


    }


    @Override
    public int getCount() {
        return traficItems.size();
    }

    @Override
    public Object getItem(int location) {
        return traficItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_rowtrafic, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView resume = (TextView) convertView.findViewById(R.id.resume);
        TextView date_debut = (TextView) convertView.findViewById(R.id.date_debut);
        TextView date_fin = (TextView) convertView.findViewById(R.id.date_fin);
        TextView heure_debut = (TextView) convertView.findViewById(R.id.heure_debut);
        TextView heure_fin = (TextView) convertView.findViewById(R.id.heure_fin);

        Trafic m = traficItems.get(position);

        resume.setText(m.getResume());
        date_debut.setText(m.getdate_debut());
        date_fin.setText(m.getDate_fin());
        heure_debut.setText(m.getHeure_debut());
        heure_fin.setText(m.getHeure_fin());

        return convertView;
    }

}
