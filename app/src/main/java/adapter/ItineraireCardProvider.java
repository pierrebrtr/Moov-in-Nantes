package adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CardProvider;
import com.pandf.moovin.R;

/**
 * Created by pierre on 23/10/2015.
 */
public class ItineraireCardProvider extends CardProvider<ItineraireCardProvider> {

    String temps;
    String dateetheure;
    String mindemarche;
    String bus1;
    String bus2;
    String bus3;
    String bus4;
    String bus5;

    @Override
    protected void onCreated() {
        super.onCreated();

    }

    @Override
    public int getLayout() {
        return R.layout.card_layout_itiinfo;
    }


    public ItineraireCardProvider setTemps(String text){
        this.temps = text;
        notifyDataSetChanged();
        return this;
    }

    public ItineraireCardProvider setDirectionTxt(String text){
        this.dateetheure = text;
        notifyDataSetChanged();
        return this;
    }

    public ItineraireCardProvider setMoreinfo(String text){
        this.mindemarche = text;
        notifyDataSetChanged();
        return this;
    }







    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
        TextView textView = (TextView)view.findViewById(R.id.tpsiti);
        textView.setText(temps);
        TextView textView2 = (TextView)view.findViewById(R.id.txtdatheu);
        textView2.setText(dateetheure);
        TextView textView3 = (TextView)view.findViewById(R.id.miniti);
        textView3.setText(mindemarche);




    }

}
