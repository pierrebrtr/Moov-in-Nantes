package adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CardProvider;
import com.pandf.moovin.R;

/**
 * Created by pierre on 23/10/2015.
 */
public class WalkCardProvider extends CardProvider<WalkCardProvider> {

    String heure;
    String directiontxt;
    String moreinfo;

    @Override
    protected void onCreated() {
        super.onCreated();

    }

    @Override
    public int getLayout() {
        return R.layout.card_layout_walkmode;
    }


    public WalkCardProvider setHeure(String text){
        this.heure = text;
        notifyDataSetChanged();
        return this;
    }

    public WalkCardProvider setDirectionTxt(String text){
        this.directiontxt = text;
        notifyDataSetChanged();
        return this;
    }

    public WalkCardProvider setMoreinfo(String text){
        this.moreinfo = text;
        notifyDataSetChanged();
        return this;
    }


    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);

        TextView textView = (TextView)view.findViewById(R.id.heure);
        textView.setText(heure);
        TextView textView2 = (TextView)view.findViewById(R.id.txtdir);
        textView2.setText(directiontxt);
        TextView textView3 = (TextView)view.findViewById(R.id.infos);
        textView3.setText(moreinfo);



    }

}
