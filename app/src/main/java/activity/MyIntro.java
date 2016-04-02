package activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.pandf.moovin.R;

/**
 * Created by dev on 27/07/2015.
 */
public class MyIntro extends AppIntro2 {

    private static final String MY_PREFERENCES = "my_preferences";


    @Override
    public void init(Bundle savedInstanceState) {





setFlowAnimation();

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Bienvenue", "Sur Moov'in Nantes", R.drawable.ic_logo, Color.parseColor("#2196F3")));
        addSlide(AppIntroFragment.newInstance("Itinéraires", "Planifiez tous vos déplacements grâce au moteur d'itinéraire.", R.drawable.itineraire, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Trafic", "Retrouvez les alertes trafic de la Tan.", R.drawable.trafic, Color.parseColor("#388E3C")));
        addSlide(AppIntroFragment.newInstance("Parkings", "Trouvez une place rapidement dans un des parkings Nantais.", R.drawable.parking, Color.parseColor("#C2185B")));
        addSlide(AppIntroFragment.newInstance("Météo", "Besoin de prendre le parapluie ? Retrouvez la météo de la semaine. Pratique non ?", R.drawable.meteo, Color.parseColor("#795548")));
        addSlide(AppIntroFragment.newInstance("Parfait !", "Bonne visite sur Moov'in Nantes, si vous avez un problème n'hésitez pas à nous en faire part.", R.drawable.check, Color.parseColor("#4CAF50")));
        // OPTIONAL METHODS

        showDoneButton(true);


        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permesssion in Manifest
        setVibrate(true);
        setVibrateIntensity(30);
    }


    public void onSkipPressed() {




        Intent i = new Intent(MyIntro.this, MainActivity.class);
        startActivity(i);

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        Intent i = new Intent(MyIntro.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onSlideChanged() {

    }
}

