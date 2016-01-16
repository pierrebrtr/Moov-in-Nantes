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
public class TransportCardProvider extends CardProvider<TransportCardProvider> {

    String heuredepart;
    String heurearrive;
    String arretdepart;
    String direction;
    String ligne;
    String arretarrive;


    @Override
    protected void onCreated() {
        super.onCreated();

    }

    @Override
    public int getLayout() {
        return R.layout.card_layout_transportmode;
    }


    public TransportCardProvider setHeureDepart(String text){
        this.heuredepart = text;
        notifyDataSetChanged();
        return this;
    }

    public TransportCardProvider setHeureArrive(String text){
        this.heurearrive = text;
        notifyDataSetChanged();
        return this;
    }

    public TransportCardProvider setArretDepart(String text){
        this.arretdepart = text;
        notifyDataSetChanged();
        return this;
    }


    public TransportCardProvider setArretArrive(String text){
        this.arretarrive = text;
        notifyDataSetChanged();
        return this;
    }

    public TransportCardProvider setDirection(String text){
        this.direction = text;
        notifyDataSetChanged();
        return this;
    }


    public TransportCardProvider setLigne(String text){
        this.ligne = text;
        notifyDataSetChanged();
        return this;
    }

    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);

        TextView textView = (TextView)view.findViewById(R.id.Heure1);
        textView.setText(heuredepart);
        TextView textView2 = (TextView)view.findViewById(R.id.heurearrive);
        textView2.setText(heurearrive);
        TextView textView3 = (TextView)view.findViewById(R.id.arretdepar);
        textView3.setText(arretdepart);

        TextView textView4 = (TextView)view.findViewById(R.id.ArretArrive);
        textView4.setText(arretarrive);
        TextView textView5 = (TextView)view.findViewById(R.id.directionbus);
        textView5.setText(direction);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(ligne, color1);

        ImageView image = (ImageView) view.findViewById(R.id.image_view);
        image.setImageDrawable(drawable);


    }

}
