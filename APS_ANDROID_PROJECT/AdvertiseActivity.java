package resolution.ex6.vr.aps;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AdvertiseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);
        View view = getLayoutInflater().inflate(R.layout.customlayout, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView textView = (TextView)findViewById(R.id.custom_textview);
        textView.setText("APS");
        ImageView custom_imageview = (ImageView)findViewById(R.id.custom_imageview);
        custom_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
