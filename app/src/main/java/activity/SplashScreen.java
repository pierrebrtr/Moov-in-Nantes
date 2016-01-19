package activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.pandf.moovin.R;

import util.Utility;

public class SplashScreen extends Activity {



    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.themer(SplashScreen.this);

        boolean isFirstTime = MyFirstLaunchPreference.isFirst(SplashScreen.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageloco = (ImageView) findViewById(R.id.imgLogo);

        AnimatorSet animator = (AnimatorSet) AnimatorInflater
                .loadAnimator(getApplicationContext(), R.animator.splash_anim);
        animator.setTarget(imageloco);
        animator.start();

       if (isFirstTime){

           new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

               @Override
               public void run() {
                   // This method will be executed once the timer is over
                   // Start your app main activity
                   Intent i = new Intent(SplashScreen.this, MyIntro.class);
                   startActivity(i);

                   // close this activity
                   finish();
               }
           }, SPLASH_TIME_OUT);

       } else {


           new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

               @Override
               public void run() {
                   // This method will be executed once the timer is over
                   // Start your app main activity
                   Intent i = new Intent(SplashScreen.this, MainActivity.class);
                   startActivity(i);

                   // close this activity
                   finish();
               }
           }, SPLASH_TIME_OUT);

       }


    }

}