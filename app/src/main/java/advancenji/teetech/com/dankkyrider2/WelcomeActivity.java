package advancenji.teetech.com.dankkyrider2;


import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.android.widget.AnimatedSvgView;

import advancenji.teetech.com.dankkyrider2.driver.Driver;
import advancenji.teetech.com.dankkyrider2.driver.DriverMapsActivity;
import advancenji.teetech.com.dankkyrider2.helper.PlayerConfig;
import advancenji.teetech.com.dankkyrider2.model.SharedValues;


public class WelcomeActivity extends AppCompatActivity {

    private AnimatedSvgView svgView;
    private static int SPLASH_TIME_OUT = 3000;

    private String userid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("dinkky");
//        Query queryRef = dataRef.child("onlineDrivers");
//        queryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Driver driver = postSnapshot.getValue(Driver.class);
//                    Log.i("database-value",driver.getCoordinate().getLatitude()+"");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                databaseError.toException().printStackTrace();
//            }
//        });


        try
        {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            //firebaseAuth.signOut();

            if (firebaseAuth.getCurrentUser() != null)
            {
                //String userType = SharedValues.getValue(this,"usertype");

//                switch (userType)
//                {
//                    case PlayerConfig.USER_TYPE_CUSTOMER:
                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                        finish();
                       // break;
//
//                    case PlayerConfig.USER_TYPE_Driver:
//                        startActivity(new Intent(WelcomeActivity.this,DriverMapsActivity.class));
//                        finish();
//                        break;
//                }

            }
            else if(SharedValues.getValue(this,"userid")!="")
            {
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            }
        }
        catch (Exception e)
        {
            Log.i("error",e.getMessage());
        }


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
                            Intent intent = new Intent(WelcomeActivity.this,OnBoardingActivity.class);
                            WelcomeActivity.this.startActivity(intent);
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }

            }
        });
    }

}
