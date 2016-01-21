package adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.pandf.moovin.R;

/**
 * Created by pierre on 23/10/2015.
 */
public class PriceProvider extends CardProvider<PriceProvider> {

    String titleprice;
   String price;

    @Override
    protected void onCreated() {
        super.onCreated();

    }

    @Override
    public int getLayout() {
        return R.layout.card_price;
    }


    public PriceProvider setTitleprice(String text){
        this.titleprice = text;
        notifyDataSetChanged();
        return this;
    }

    public PriceProvider setPrice(String text){
        this.price = text;
        notifyDataSetChanged();
        return this;
    }




    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);

        TextView textView = (TextView)view.findViewById(R.id.nameprice);
        textView.setText(titleprice);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
        TextDrawable drawable2 = TextDrawable.builder()
                .beginConfig()
                .fontSize(30)
                .bold()
                .endConfig()
                .buildRound(price, color1);
        ImageView image = (ImageView) view.findViewById(R.id.priceimage);
        image.setImageDrawable(drawable2);




    }

}
