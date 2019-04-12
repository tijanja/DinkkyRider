package advancenji.teetech.com.dankkyrider2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import advancenji.teetech.com.dankkyrider2.driver.DriverMapsActivity;

public class AccountsActivity extends AppCompatActivity {

    RelativeLayout userAccount,riderAccount;
    private static final int RC_SIGN_IN = 400;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        firebaseAuth = FirebaseAuth.getInstance();

        userAccount = findViewById(R.id.userBtn);
        riderAccount = findViewById(R.id.riderAccount);

        userAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

//                if(firebaseUser != null)
//                {
//                    Intent intent = new Intent(AccountsActivity.this,MapsActivity.class);
//                    intent.putExtra("isDriver",false);
//                    AccountsActivity.this.startActivity(intent);
//                    finish();
//                }
//                else
//                {
////                    List<AuthUI.IdpConfig> providers = Arrays.asList(
////                            new AuthUI.IdpConfig.EmailBuilder().build(),
////                            new AuthUI.IdpConfig.PhoneBuilder().build(),
////                            new AuthUI.IdpConfig.GoogleBuilder().build(),
////                            new AuthUI.IdpConfig.FacebookBuilder().build());
////
////                    AuthUI.getInstance()
//
                    Intent intent = new Intent(AccountsActivity.this,SignupActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    AccountsActivity.this.startActivity(intent);
                    finish();

               // }


            }
        });


        riderAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();


//                    List<AuthUI.IdpConfig> providers = Arrays.asList(
//                            new AuthUI.IdpConfig.EmailBuilder().build(),
//                            new AuthUI.IdpConfig.PhoneBuilder().build(),
//                            new AuthUI.IdpConfig.GoogleBuilder().build(),
//                            new AuthUI.IdpConfig.FacebookBuilder().build());
//
//                    // Create and launch sign-in intent
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setAvailableProviders(providers)
//                                    .build(),
//                            RC_SIGN_IN);
                    Intent intent = new Intent(AccountsActivity.this,SignupActivity.class);
                    intent.putExtra("isDriver",true);

                    startActivity(intent);
                    finish();


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == Activity.RESULT_OK)
            {
                startActivity(new Intent(this,DriverMapsActivity.class));
            }

        }
    }
}
