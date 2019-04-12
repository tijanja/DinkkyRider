package advancenji.teetech.com.dankkyrider2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import advancenji.teetech.com.dankkyrider2.driver.DriverMapsActivity;
import advancenji.teetech.com.dankkyrider2.helper.PlayerConfig;
import advancenji.teetech.com.dankkyrider2.helper.Utils;
import advancenji.teetech.com.dankkyrider2.model.Driver;
import advancenji.teetech.com.dankkyrider2.model.SharedValues;
import advancenji.teetech.com.dankkyrider2.model.User;

public class SignupActivity extends AppCompatActivity {

    EditText name,email,customerPhone,city,password,confirmpassword;
    CheckBox terms;
    FrameLayout next;
    boolean isTermsChecked = false;
    String userid = UUID.randomUUID()+"";
    int measuredWidth;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
    }

    private void initializeViews() {

        name =  findViewById(R.id.name);
        email =   findViewById(R.id.email);
        customerPhone =  findViewById(R.id.customerPhone);
        city =   findViewById(R.id.city);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        terms = findViewById(R.id.terms);
        next = findViewById(R.id.login);

        terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTermsChecked = isChecked;
                terms.invalidate();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(verlidateform())
                {
                    measuredWidth = Utils.startButtonAnim(SignupActivity.this,next);
                    try
                    {
                        saveTodatabase();
                    }
                    catch (Exception e)
                    {
                       Log.e("database error",e.getMessage());
                    }

                }
            }
        });
    }

    private void saveTodatabase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference();



        final DatabaseReference dataRef = firebaseDatabase.getReference("dinkky");
        if(getIntent().getBooleanExtra("isDriver",false))
        {
            final Driver driver = new Driver();
            driver.setName(name.getText().toString().trim());
            driver.setEmail(email.getText().toString().trim());
            driver.setPhone(customerPhone.getText().toString().trim());
            driver.setCity(city.getText().toString().trim());
            driver.setTerms(!isTermsChecked);
            driver.setType("Driver");


            final FirebaseAuth fAuth = FirebaseAuth.getInstance();

            fAuth.createUserWithEmailAndPassword(driver.getEmail(),password.getText().toString().trim()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    fAuth.signInWithEmailAndPassword(driver.getEmail(),confirmpassword.getText().toString().trim()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Utils.stopButtonAnim(SignupActivity.this,next,measuredWidth);
                            if(task.isSuccessful())
                            {
                                driver.setId(task.getResult().getUser().getUid());

                                dataRef.child("Riders").child(driver.getId()).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        SharedValues.saveValue(SignupActivity.this.getBaseContext(),"userid",driver.getId());
                                        SharedValues.saveValue(SignupActivity.this.getBaseContext(),"usertype",PlayerConfig.USER_TYPE_Driver);
                                        Intent intent = new Intent(SignupActivity.this,DriverMapsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                            else
                            {
                                Log.i("sign-in","can't sign-in");
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("sign-in exception",e.getMessage());
                        }
                    });


                }
            });
        }
        else
        {
            final User user = new User();
            user.setName(name.getText().toString().trim());
            user.setEmail(email.getText().toString().trim());
            user.setCity(city.getText().toString().trim());
            user.setPhone(customerPhone.getText().toString().trim());
            user.setType("Customer");
            user.setTerms(!isTermsChecked);


            final FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.createUserWithEmailAndPassword(user.getEmail(),password.getText().toString().trim()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    user.setId(task.getResult().getUser().getUid());
                    fAuth.signInWithEmailAndPassword(user.getEmail(),confirmpassword.getText().toString().trim()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            dataRef.child("Customers").child(user.getId()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Utils.stopButtonAnim(SignupActivity.this,next,measuredWidth);
                                        SharedValues.saveValue(SignupActivity.this.getBaseContext(),"userid",user.getId());
                                        SharedValues.saveValue(SignupActivity.this.getBaseContext(),"usertype",PlayerConfig.USER_TYPE_CUSTOMER);
                                        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Utils.alert(SignupActivity.this,e.getMessage());
                        }
                    });

                }
            });


        }


    }

    private boolean verlidateform() {

        if(TextUtils.isEmpty(name.getText().toString().trim()))
        {
            name.setError("Please enter your full name");
            return false;
        }
        else if(TextUtils.isEmpty(email.getText().toString().trim()))
        {
            email.setError("Please enter email");
            return false;
        }
        else if(TextUtils.isEmpty(customerPhone.getText().toString().trim()))
        {
            customerPhone.setError("Please enter Phone Number");
            return false;
        }
        else if(TextUtils.isEmpty(city.getText().toString().trim()))
        {
            city.setError("Please enter city namme");
            return false;
        }
        else if(TextUtils.isEmpty(password.getText().toString().trim()))
        {
            password.setError("Please enter Password");
            return false;
        }
        else if(TextUtils.isEmpty(confirmpassword.getText().toString().trim()))
        {
            confirmpassword.setError("Please enter Confirm Password");
            return false;
        }else if(!isTermsChecked)
        {
            terms.setError("Please Agree with terms");
            return false;
        }

        return true;
    }

}
