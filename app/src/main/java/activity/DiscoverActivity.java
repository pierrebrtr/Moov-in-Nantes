package activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.pandf.moovin.R;

import java.text.SimpleDateFormat;

import util.Utility;

/**
 * Created by dev on 29/07/2015.
 */
public class DiscoverActivity extends ActionBarActivity  {

    private Toolbar mToolbar;
    AutoCompleteTextView depart;
    AutoCompleteTextView arrive;
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    private static final String TAG = MainActivity.class.getSimpleName();


    Activity activity;

    Toolbar toolbar;


    public Bitmap blurBitmap(Bitmap bitmap){

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(getApplicationContext());

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(30.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;


    }




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Utility.themer(DiscoverActivity.this);
        super.onCreate(savedInstanceState);










        setContentView(R.layout.activity_discover);
        toolbar = (Toolbar) findViewById(R.id.toolbar);




        setSupportActionBar(toolbar);


        LinearLayout layout_root = (LinearLayout) findViewById(R.id.content_root);


         /* adapt the image to the size of the display */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),R.drawable.pierre),size.x,size.y,true);

        Drawable d = new BitmapDrawable(getResources(), blurBitmap(bmp));

        layout_root.setBackground(d);


        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(DiscoverActivity.this, MainActivity.class);

                DiscoverActivity.this.startActivity(myIntent);

            }
        });




    }









    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(DiscoverActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



















}


