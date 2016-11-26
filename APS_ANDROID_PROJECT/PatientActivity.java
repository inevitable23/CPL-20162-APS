package resolution.ex6.vr.aps;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import static resolution.ex6.vr.aps.R.id.patientLocationText;

public class PatientActivity extends AppCompatActivity {

    private  TextView patientInfo;;
    private  TextView patientlocation;
    private  TextView occurTime;
    private WebView mWebView;
    private String patientInfoStr;
    private String patientLocationStr;
    String patientOccurTime = null;
    /*******소켓 ***************/
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://52.78.1.158:3000");
        } catch (URISyntaxException e) {}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        layoutinit();
        // 웹뷰에서 자바스크립트실행가능
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 구글홈페이지 지정
        mWebView.loadUrl("http://ec2-52-78-1-158.ap-northeast-2.compute.amazonaws.com:3100");
        // WebViewClient 지정
        mWebView.setWebViewClient(new WebViewClientClass());
        mSocket.connect();
        mSocket.on("fromServer2", listen_start_person2); //환자 상세 위치
        mSocket.on("fromServer3", listen_start_person3); // 환자 상태
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    /*
     * Layout
     */
    private void layoutinit(){
        View view = getLayoutInflater().inflate(R.layout.customlayout, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView textView = (TextView)findViewById(R.id.custom_textview);
        textView.setText("환자 위치");
        ImageView custom_imageview = (ImageView)findViewById(R.id.custom_imageview);
        custom_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        patientInfo = (TextView)findViewById(R.id.patientInfo);
        patientlocation = (TextView)findViewById(R.id.patientlocation);
        occurTime = (TextView)findViewById(R.id.occurTime);
        mWebView = (WebView) findViewById(R.id.webview);
        Intent intent = getIntent();
        patientOccurTime = intent.getExtras().getString("occurTime");
        if(patientOccurTime != null){
            occurTime.setText(patientOccurTime);
        }

    }
    /**********************socketio 통신 리스너 ****************/
    private Emitter.Listener listen_start_person2 = new Emitter.Listener() {

        public void call(Object... args) {
            final JSONObject obj = (JSONObject)args[0];
            //서버에서 보낸 JSON객체를 사용할 수 있습니다.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //이곳에 ui 관련 작업을 할 수 있습니다.
                    /*************환자 발생시 Dialog + 경고음 추가해야함(함수로 바꿀껏) ***********/
                    try {
                        patientLocationStr = obj.getString("data");
                        patientlocation.setText(patientLocationStr);

                    }catch (JSONException e){
                        return ;
                    }
                }
            });
        }
    };
    /**********************socketio 통신 리스너 ****************/
    private Emitter.Listener listen_start_person3 = new Emitter.Listener() {

        public void call(Object... args) {
            final JSONObject obj = (JSONObject)args[0];
            //서버에서 보낸 JSON객체를 사용할 수 있습니다.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //이곳에 ui 관련 작업을 할 수 있습니다.
                    try {
                        patientInfoStr = obj.getString("data");
                        patientInfo.setText(patientInfoStr);
                    }catch (JSONException e){
                        return ;
                    }
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
       // patientInfo.setText("불러오는 중...");
       // patientlocation.setText("불러오는 중...");
    }
}
