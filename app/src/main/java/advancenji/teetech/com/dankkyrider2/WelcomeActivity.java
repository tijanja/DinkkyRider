package advancenji.teetech.com.dankkyrider2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jaredrummler.android.widget.AnimatedSvgView;

public class WelcomeActivity extends AppCompatActivity {

    private AnimatedSvgView svgView;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        svgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        svgView.postDelayed(new Runnable() {
            @Override
            public void run() {
                svgView.start();
            }
        },1000);

        svgView.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {

                if(state == AnimatedSvgView.STATE_FINISHED)
                {
                    new Handler().postDelayed(new Runnable() {

                        /*
                         * Showing splash screen with a timer. This will be useful when you
                         * want to show case your app logo / company
                         */
                        @Override
                        public void run() {

                            Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                            WelcomeActivity.this.startActivity(intent);

                        }
                    }, SPLASH_TIME_OUT);
                }

            }
        });
    }
}
