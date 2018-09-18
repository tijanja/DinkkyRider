package advancenji.teetech.com.dankkyrider2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void gotoSignup(View v)
    {
        startActivity(new Intent(this,SignupActivity.class));
    }

    public void login(View v)
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
