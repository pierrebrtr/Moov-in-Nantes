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
public class InfoTrafficTanProvider extends CardProvider<InfoTrafficTanProvider> {

    String title;
    String subtitle;
    String text;
    String urlimage;

    @Override
    protected void onCreated() {
        super.onCreated();

    }

    @Override
    public int getLayout() {
        return R.layout.card_layout_infotraffictan;
    }


    public InfoTrafficTanProvider setTitle(String text){
        this.title = text;
        notifyDataSetChanged();
        return this;
    }

    public InfoTrafficTanProvider setSubTitle(String text){
        this.subtitle = text;
        notifyDataSetChanged();
        return this;
    }

    public InfoTrafficTanProvider setText(String text){
        this.text = text;
        notifyDataSetChanged();
        return this;
    }


    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);

        TextView textView = (TextView)view.findViewById(R.id.title);
        textView.setText(title);
        TextView textView2 = (TextView)view.findViewById(R.id.subtitle);
        textView2.setText(subtitle);
        TextView textView3 = (TextView)view.findViewById(R.id.supportingText);
        textView3.setText(text);



    }

}
