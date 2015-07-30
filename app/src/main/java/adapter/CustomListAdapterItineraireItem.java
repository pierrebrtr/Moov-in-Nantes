package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pandf.moovin.R;

import java.util.ArrayList;
import java.util.List;

import model.Arrets;
import model.ItineraireItem;

/**
 * Created by dev on 30/07/2015.
 */
public class CustomListAdapterItineraireItem extends BaseAdapter implements Filterable {
    private Activity activity;

    private Context context;
    private LayoutInflater inflater;

    private List<ItineraireItem> itineraireItems;
    private List<ItineraireItem> filtereditineraireItems;
    private ItemFilter mFilter = new ItemFilter();

    public CustomListAdapterItineraireItem(Context context, List<ItineraireItem> itineraireItems) {
        //super(context, R.layout.your_row, items);
        this.context = context;
        this.itineraireItems = itineraireItems;
        this.filtereditineraireItems = itineraireItems;
    }

    @Override
    public int getCount() {
        return filtereditineraireItems.size();
    }

    @Override
    public Object getItem(int position) {
        return filtereditineraireItems.get(position);
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




        TextView libelle = (TextView) convertView.findViewById(R.id.libelle);
        TextView lng = (TextView) convertView.findViewById(R.id.lng);
        TextView lat = (TextView) convertView.findViewById(R.id.lat);


        ItineraireItem m = itineraireItems.get(position);



        libelle.setText(m.getLibelle());
        lng.setText(String.valueOf(m.getLng()));
        lat.setText(String.valueOf(m.getLat()));


        return convertView;
    }

    public static class ViewHolder {
        TextView libelle;
        TextView lng;
        TextView lat;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = itineraireItems.size();


            results.values = itineraireItems;
            results.count = count;

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtereditineraireItems = (ArrayList<ItineraireItem>) results.values;
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        return mFilter;
    }


}
