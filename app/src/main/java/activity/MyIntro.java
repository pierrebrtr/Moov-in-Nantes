package activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.pandf.moovin.R;

/**
 * Created by dev on 27/07/2015.
 */
public class MyIntro extends AppIntro {


    @Override
    public void init(Bundle savedInstanceState) {

        setFadeAnimation();

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Bienvenue", "Sur Moov'in Nantes", R.drawable.ic_logo, R.color.colorPrimary));

        // OPTIONAL METHODS
        // Override bar/separator color
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        setDoneText("C'est partit !");
        setSkipText("Passer");
        // Hide Skip/Done button
        showSkipButton(true);
        showDoneButton(true);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permesssion in Manifest
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed() {

        Intent i = new Intent(MyIntro.this, MainActivity.class);
        startActivity(i);

    }

    @Override
    public void onDonePressed() {
        Intent i = new Intent(MyIntro.this, MainActivity.class);
        startActivity(i);
    }
}

