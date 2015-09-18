package activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.pandf.moovin.R;

import java.util.Random;

import util.Utility;

/**
 * Created by dev on 19/04/15.
 */

public class EasterActivity extends ActionBarActivity {

    int clickable = 0;


    private Toolbar mToolbar;
    public void onCreate(Bundle savedInstanceState) {

        Utility.themer(EasterActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter);

        final TextView mSwitcher = (TextView) findViewById(R.id.textMoov);
        final TextView mSwitcher2 = (TextView) findViewById(R.id.textversion);
        final ImageView heart = (ImageView) findViewById(R.id.heart);

        final Animation dd = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
       OvershootInterpolator interpolator = new OvershootInterpolator(4f);
        dd.setInterpolator(interpolator);
        dd.setDuration(600);
        mSwitcher.startAnimation(dd);
        mSwitcher2.startAnimation(dd);

        heart.setClickable(true);

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickable = clickable + 1;


                final Animation dd = new ScaleAnimation(1.0f,0.7f,1.0f,0.7f,Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                OvershootInterpolator interpolator = new OvershootInterpolator(5f);
                dd.setInterpolator(interpolator);
                dd.setDuration(600);

                heart.startAnimation(dd);


                Vibrator vibrator = (Vibrator) EasterActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                vibrator.vibrate(60);

                if(clickable == 10) {

                    Vibrator vibrator2 = (Vibrator) EasterActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    vibrator2.vibrate(600);


                    final boolean mStopHandler = false;
                    final Handler mHandler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            // do your stuff - don't create a new runnable here!
                            if (!mStopHandler) {

                                mHandler.postDelayed(this, 200);


                                final Animation dd = new ScaleAnimation(1.0f,0.7f,1.0f,0.7f,Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                                OvershootInterpolator interpolator = new OvershootInterpolator(5f);
                                dd.setInterpolator(interpolator);
                                dd.setDuration(200);

                                heart.startAnimation(dd);


                                Random rnd = new Random();
                                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                                heart.setColorFilter(color);

                            }
                        }
                    };

                    mHandler.post(runnable);





                }

            }
        });





    }


}