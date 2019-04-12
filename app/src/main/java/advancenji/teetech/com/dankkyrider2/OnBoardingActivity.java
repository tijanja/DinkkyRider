package advancenji.teetech.com.dankkyrider2;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.viewpagerindicator.CirclePageIndicator;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager view_pager;
    private CirclePageIndicator indicator;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int [] layouts = new int[]{R.layout.welcome_slide_one,R.layout.welcome_slide_two,R.layout.welcome_slide_three};
    FrameLayout skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        view_pager = (ViewPager) findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this,layouts);
        view_pager.setAdapter(myViewPagerAdapter);

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(view_pager);

        skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBoardingActivity.this.startActivity(new Intent(OnBoardingActivity.this,AccountsActivity.class));
                finish();
            }
        });
    }
}
