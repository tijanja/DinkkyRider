package advancenji.teetech.com.dankkyrider2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import advancenji.teetech.com.dankkyrider2.driver.DriverMapsActivity;
import advancenji.teetech.com.dankkyrider2.helper.PlayerConfig;
import advancenji.teetech.com.dankkyrider2.helper.Utils;
import advancenji.teetech.com.dankkyrider2.model.SharedValues;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    FrameLayout next;
    int measuredWidth=0;

    private static final int RC_SIGN_IN = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.userMail);
        password = findViewById(R.id.password);

        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    public boolean validate()
    {
        if(TextUtils.isEmpty(email.getText().toString().trim()))
        {
            email.setError("Please enter email address");
            return false;
        }
        else if(TextUtils.isEmpty(password.getText().toString().trim()))
        {
            password.setError("Please enter Password");
            return false;
        }

        return true;
    }

    public void gotoSignup(View v)
    {
        startActivity(new Intent(this,SignupActivity.class));
    }

    public void createSignInIntent() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null)
        {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
        else
        {
            auth.signOut();
        }

    }


    public void loginUser()
    {
        if(validate())
        {
           measuredWidth = Utils.startButtonAnim(LoginActivity.this,next);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    SharedValues.saveValue(LoginActivity.this,PlayerConfig.SHARED_KEY_USERID,authResult.getUser().getUid());


//                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("dinkky");
//
//                    switch (SharedValues.getValue(LoginActivity.this,"usertype"))
//                    {
//                        case PlayerConfig.USER_TYPE_CUSTOMER:
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
//                            break;

//                        case PlayerConfig.USER_TYPE_Driver:
//                            startActivity(new Intent(LoginActivity.this,DriverMapsActivity.class));
//                            finish();
//                            break;
//                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Utils.alert(LoginActivity.this,e.getMessage());
                    Utils.stopButtonAnim(LoginActivity.this,next,measuredWidth);
                    Log.i("rrrr",measuredWidth+"");
                }
            });

        }
    }
}
