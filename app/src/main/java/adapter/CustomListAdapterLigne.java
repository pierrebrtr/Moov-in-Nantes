package adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.toolbox.ImageLoader;
import com.pandf.moovin.R;

import java.util.List;

import app.AppController;
import model.Lines;


public class CustomListAdapterLigne extends BaseAdapter  {
    private Activity activity;
    private LayoutInflater inflater;
    private Context context;


    private List<Lines> linesItems;









    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListAdapterLigne(Activity activity, List<Lines> linesItems, boolean showicon) {


        this.activity = activity;
        this.linesItems = linesItems;




    }



    @Override
    public int getCount() {
        return linesItems.size();
    }

    @Override
    public Object getItem(int location) {
        return linesItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_rowlines, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();




        TextView ligne = (TextView) convertView.findViewById(R.id.ligne);
        TextView color = (TextView) convertView.findViewById(R.id.color);
        TextView id = (TextView) convertView.findViewById(R.id.id);

        ImageView image = (ImageView) convertView.findViewById(R.id.image_viewlines);

        Lines m = linesItems.get(position);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();



        // getting movie data for the row

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(m.getNumero(), Color.parseColor("#" + m.getColor() ));

        // title
        ligne.setText(m.getLigne());
        color.setText(m.getColor());
        id.setText(m.getId());
        image.setImageDrawable(drawable1);

        return convertView;


    }







}