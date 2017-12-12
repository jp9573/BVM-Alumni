package jaypatel.co.in.bvmalumni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import jaypatel.co.in.bvmalumni.Register.Register;
import jaypatel.co.in.bvmalumni.SignIn.SignIn;

public class StartActivity extends AppCompatActivity {

    Button signIn, register;
    TextView bvmHeading;
    Animation animFadeIn,animBounce, animSlideInLeft, animSlideOutLeft, animSlideInRight, animSlideOutRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        signIn = (Button) findViewById(R.id.btn_signIn);
        register = (Button) findViewById(R.id.btn_register);
        bvmHeading = (TextView) findViewById(R.id.textView2);

        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        animSlideInLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        animSlideOutLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);
        animSlideInRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        animSlideOutRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);

        animSlideOutLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animSlideOutRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.startAnimation(animSlideOutLeft);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn.startAnimation(animSlideOutRight);
            }
        });

        bvmHeading.setVisibility(View.VISIBLE);
        bvmHeading.startAnimation(animFadeIn);
        signIn.startAnimation(animSlideInRight);
        register.startAnimation(animSlideInLeft);

    }

}
