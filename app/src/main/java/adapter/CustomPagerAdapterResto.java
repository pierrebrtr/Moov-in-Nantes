package adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pandf.moovin.R;

import java.util.List;

import model.DiscoverResto;

/**
 * Created by pierre on 30/12/2015.
 */
public class CustomPagerAdapterResto extends PagerAdapter {


    private Context mContext;
    private List<DiscoverResto> geoItems;


    public CustomPagerAdapterResto(Context context, List<DiscoverResto> list) {

        geoItems = list;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.discoverresto, collection, false);



        TextView nomresto = (TextView) layout.findViewById(R.id.restoname);
        TextView typecuisine = (TextView) layout.findViewById(R.id.cuisinetype);
        TextView adresse = (TextView) layout.findViewById(R.id.adresseresto);
        TextView nbcouvert = (TextView) layout.findViewById(R.id.restocouvertnb);

        DiscoverResto m = geoItems.get(position);


        nomresto.setText(m.getNomresto());
        typecuisine.setText(m.getTypecuisine());
        adresse.setText(m.getAdresse());
        nbcouvert.setText(m.getNbcouverts());

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return geoItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {


        return "Test";
    }

}
