package resolution.ex6.vr.aps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    LocationManager manager;
    Intent aedintent;
    //현재 위치 값
    static double longitude = -1;
    static double latitude = -1;
    //
    /***************
     * 레이아웃관련 변수
     ***********/
    LinearLayout find_patient_Button;
    LinearLayout find_AED_Button;
    LinearLayout emergnecy_button;
    TextView patientLocationText;
    ImageView patientImageView;

    String patientOccurTime = null;
    /***********경고음 ************/
    private SoundPool sound;
    private int soundId;
    private int streamId;
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
        setContentView(R.layout.activity_main);
        layoutinit();
        mSocket.connect();
        mSocket.on("fromServer1", listen_start_person1);

        /*************환자 발생시 Dialog + 경고음 추가해야함 ***********/
        find_AED_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aedintent = new Intent(getApplicationContext(), AEDActivity.class);
                if (longitude != -1 && latitude != -1) {
                    aedintent.putExtra("longitude", longitude);
                    aedintent.putExtra("lat", latitude);
                    startActivity(aedintent);
                } else {
                    startLocationService();
                    Toast.makeText(getApplicationContext(), "현재 위치를 찾는중...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        find_patient_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PatientActivity.class);
                i.putExtra("occurTime", patientOccurTime);
                startActivity(i);
            }
        });
        emergnecy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EmergencylectureActivity.class);
                startActivity(i);
            }
        });


    }

    /**********************socketio 통신 리스너 ****************/
    private Emitter.Listener listen_start_person1 = new Emitter.Listener() {

        public void call(Object... args) {
            final JSONObject obj = (JSONObject)args[0];
            //서버에서 보낸 JSON객체를 사용할 수 있습니다.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //이곳에 ui 관련 작업을 할 수 있습니다.
                    /*************환자 발생시 Dialog + 경고음 추가해야함(함수로 바꿀껏) ***********/
                    String strColor = "#ff0000";
                    patientImageView.setImageResource(R.drawable.menu1_warning);
                    patientLocationText.setText("환자 발생");
                    patientLocationText.setTextColor(Color.parseColor(strColor));
                    streamId = sound.play(soundId, 1.0F, 1.0F, 1, -1, 1.0F);//눈을 뜨기 전까지 무한반복
                    showDialog();
                    patientOccurTime = DateFormat.getDateTimeInstance().format(new Date());
                }
            });
        }
    };
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

// 여기서 부터는 알림창의 속성 설정
        builder.setTitle("환자 발생!!!!")        // 제목 설정
                .setMessage("심정지 환자가 발생했습니다.")        // 메세지 설정
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        sound.autoPause();
                        dialog.cancel();
                    }
                });
                /*
                .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        sound.autoPause();
                        dialog.cancel();
                    }
                });*/


        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

    /***********
     * layout inflater
     **********/
    public void layoutinit() {
        find_patient_Button = (LinearLayout) findViewById(R.id.find_patient_Button);
        find_AED_Button = (LinearLayout) findViewById(R.id.find_aed_button);
        emergnecy_button = (LinearLayout) findViewById(R.id.emergency_button);
        patientLocationText = (TextView) findViewById(R.id.patientLocationText);
        patientImageView = (ImageView) findViewById(R.id.patientImageView);
        streamId = -1;
        sound = new SoundPool(1, 3, 0);// maxStreams, streamType, srcQuality
        soundId = sound.load(this, R.raw.alarm1, 1);
        String strColor = "#ffffff";
        patientImageView.setImageResource(R.drawable.menu1);
        patientLocationText.setText("환자 위치");
        patientLocationText.setTextColor(Color.parseColor(strColor));
    }

    //현재 위치를 찾는것.
    private void startLocationService() {

        // get manager instance
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        long minTime = 1000;
        float minDistance = 0;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "체크 퍼미션", Toast.LENGTH_SHORT).show();
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener);

    }

    private void stopLocationService() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "체크 퍼미션", Toast.LENGTH_SHORT).show();
            return;
        }
        manager.removeUpdates(mLocationListener);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            stopLocationService();
            if (longitude != -1 && latitude != -1) {
                aedintent.putExtra("longitude", longitude);
                aedintent.putExtra("lat", latitude);
                startActivity(aedintent);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sound.autoPause();
        sound.release();
        mSocket.disconnect();
        mSocket.off("new message", listen_start_person1);
        mSocket.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String strColor = "#ffffff";
        patientImageView.setImageResource(R.drawable.menu1);
        patientLocationText.setText("환자 위치");
        patientLocationText.setTextColor(Color.parseColor(strColor));
    }
}