package adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.pandf.moovin.R;

/**
 * Created by pierre on 23/10/2015.
 */
public class ParkingCardProvider extends CardProvider<ParkingCardProvider> {

    String lieu;
    String nbdeplaces;


    @Override
    protected void onCreated() {
        super.onCreated();

    }

    @Override
    public int getLayout() {
        return R.layout.card_parking;
    }


    public ParkingCardProvider setLieu(String text){
        this.lieu = text;
        notifyDataSetChanged();
        return this;
    }

    public ParkingCardProvider setNbplaces(String text){
        this.nbdeplaces = text;
        notifyDataSetChanged();
        return this;
    }




    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);

        TextView textView = (TextView)view.findViewById(R.id.nnomparking);
        textView.setText(lieu);
        TextView textView3 = (TextView)view.findViewById(R.id.placesparking);
        textView3.setText("Nb de places : " + nbdeplaces);



    }

}
