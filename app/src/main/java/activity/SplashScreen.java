package activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;

import com.pandf.moovin.R;

import util.Utility;

public class SplashScreen extends Activity {


    private static String URL = "open.tan.fr/ewp/arrets.json";
    private static String FILE_NAME = "CachedResponse";

    private static int REQUEST_CODE = 1;


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;


    public void checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(SplashScreen.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);

            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // continue here - permission was granted
                } else {

                    Toast.makeText(SplashScreen.this, "Vous devez accepter la permission pour l'application (au prochain démarage)", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.themer(SplashScreen.this);

        boolean isFirstTime = MyFirstLaunchPreference.isFirst(SplashScreen.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkDrawOverlayPermission();

        ImageView imageloco = (ImageView) findViewById(R.id.imgLogo);

        AnimatorSet animator = (AnimatorSet) AnimatorInflater
                .loadAnimator(getApplicationContext(), R.animator.splash_anim);
        animator.setTarget(imageloco);
        animator.start();

      if (isFirstTime){


          StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
          StrictMode.setThreadPolicy(policy);




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