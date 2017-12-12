package jaypatel.co.in.bvmalumni;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    Animation animFadeIn,animBounce;
    ImageView logo;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = (ImageView) findViewById(R.id.splashscreenImg);
        text = (TextView) findViewById(R.id.splashscreenTxt);

        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        logo.setVisibility(View.VISIBLE);
        logo.setAnimation(animFadeIn);
        text.setAnimation(animBounce);

        final Context context = this;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                if (pref.getBoolean("activity_executed", false)) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(context, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}