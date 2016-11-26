package resolution.ex6.vr.aps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class EmergencylectureActivity extends AppCompatActivity {

    Fragment emergency1Fragment;
    Fragment emergency2Fragment;
    Fragment emergency3Fragment;
    Fragment emergency4Fragment;
    ImageView emergency_button1;
    ImageView emergency_button2;
    ImageView emergency_button3;
    ImageView emergency_button4;
    ImageView advertiseImageView;
    int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencylecture);
        layoutinit();

        if (findViewById(R.id.container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            emergency1Fragment = new Emergency1Fragment();
            emergency1Fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    emergency1Fragment).commit();
        }

        emergency2Fragment = new Emergency2Fragment();
        emergency3Fragment = new Emergency3Fragment();
        emergency4Fragment = new Emergency4Fragment();


        emergency_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStepImage(1);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, emergency1Fragment)
                        .commit();
                currentPage = 1;
            }
        });

        emergency_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStepImage(2);
                emergency_button2.setImageResource(R.drawable.steptwo);
                emergency_button3.setImageResource(R.drawable.step3_off);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, emergency2Fragment)
                        .commit();
                currentPage = 2;
            }
        });
        emergency_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStepImage(3);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, emergency3Fragment)
                        .commit();
                currentPage = 3;
            }
        });
        emergency_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStepImage(4);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, emergency4Fragment)
                        .commit();
                currentPage = 4;
            }
        });
        advertiseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), AdvertiseActivity.class);
                startActivity(intent);
            }
        });

    }

    public void layoutinit() {
        View view = getLayoutInflater().inflate(R.layout.customlayout, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView textView = (TextView)findViewById(R.id.custom_textview);
        textView.setText("응급 처치");
        ImageView custom_imageview = (ImageView)findViewById(R.id.custom_imageview);
        custom_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        emergency_button1 = (ImageView) findViewById(R.id.emergency_button1);
        emergency_button2 = (ImageView) findViewById(R.id.emergency_button2);
        emergency_button3 = (ImageView) findViewById(R.id.emergency_button3);
        emergency_button4 = (ImageView) findViewById(R.id.emergency_button4);
        advertiseImageView = (ImageView) findViewById(R.id.advertiseImageView);
    }

    public Fragment findFragmentId(int fragmentId) {
        if (fragmentId == 1) {
            return emergency1Fragment;
        } else if (fragmentId == 2) {
            return emergency2Fragment;
        } else if (fragmentId == 3) {
            return emergency3Fragment;
        } else if (fragmentId == 4) {
            return emergency4Fragment;
        }
        return new Fragment();
    }
    public void setStepImage(int stepNumber){
        if(stepNumber == 1){
            emergency_button2.setImageResource(R.drawable.step2_off);
            emergency_button3.setImageResource(R.drawable.step3_off);
            emergency_button4.setImageResource(R.drawable.step4_off);
        }else if(stepNumber == 2){
            emergency_button2.setImageResource(R.drawable.steptwo);
            emergency_button3.setImageResource(R.drawable.step3_off);
            emergency_button4.setImageResource(R.drawable.step4_off);
        }else if(stepNumber == 3){
            emergency_button2.setImageResource(R.drawable.steptwo);
            emergency_button3.setImageResource(R.drawable.stepthree);
            emergency_button4.setImageResource(R.drawable.step4_off);

        }else if(stepNumber == 4){
            emergency_button2.setImageResource(R.drawable.steptwo);
            emergency_button3.setImageResource(R.drawable.stepthree);
            emergency_button4.setImageResource(R.drawable.stepfour);
        }
    }
}
