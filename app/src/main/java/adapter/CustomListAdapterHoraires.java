package adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.pierre.tan.R;

import java.util.List;

import app.AppController;
import model.Horaires;


public class CustomListAdapterHoraires extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Horaires> horaireItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListAdapterHoraires(Activity activity, List<Horaires> horaireItems) {
        this.activity = activity;
        this.horaireItems = horaireItems;
    }

    @Override
    public int getCount() {
        return horaireItems.size();
    }

    @Override
    public Object getItem(int location) {
        return horaireItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_rowhoraires, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView heure = (TextView) convertView.findViewById(R.id.heure);
        TextView passage = (TextView) convertView.findViewById(R.id.passage);



        // getting movie data for the row
       Horaires m = horaireItems.get(position);


        // title
        heure.setText(m.getHeure());


        String ligneStr = "";
        for (String str : m.getPassages()) {
            ligneStr += str + ", ";
        }
        ligneStr = ligneStr.length() > 0 ? ligneStr.substring(0,
                ligneStr.length() - 2) : ligneStr;
        passage.setText(ligneStr);




        return convertView;


    }


}