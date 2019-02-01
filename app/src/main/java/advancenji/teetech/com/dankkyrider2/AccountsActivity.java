package advancenji.teetech.com.dankkyrider2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class AccountsActivity extends AppCompatActivity {

    RelativeLayout userAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        userAccount = findViewById(R.id.userBtn);
        userAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountsActivity.this.startActivity(new Intent(AccountsActivity.this,LoginActivity.class));
            }
        });
    }
}
