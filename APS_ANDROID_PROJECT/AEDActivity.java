package resolution.ex6.vr.aps;

import android.content.Intent;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.NMapView.OnMapViewTouchEventListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;

//adasd
public class AEDActivity extends NMapActivity
        implements OnMapStateChangeListener, OnCalloutOverlayListener, ListViewAdapter.ListClickListener, OnMapViewTouchEventListener{

    // API-KEY
    public static final String API_KEY = "mo8MMpoxRZggZYZ9JdKy";
    // 네이버 맵 객체
    NMapView mMapView = null;
    // 맵 컨트롤러
    NMapController mMapController = null;
    // 맵을 추가할 레이아웃
    LinearLayout MapContainer;

    // 오버레이의 리소스를 제공하기 위한 객체
    NMapViewerResourceProvider mMapViewerResourceProvider = null;
    // 오버레이 관리자
    NMapOverlayManager mOverlayManager;
    LocationManager manager;

    //현재 위치 값
    double longitude = -1;
    double latitude = -1;


    /******************제세동기 공공데이터 관련 변수***************/
    private static ArrayList<String> jusoArr = new ArrayList<>();
    private static ArrayList<String> jangsoArr = new ArrayList<>();
    private static ArrayList<String> telArr = new ArrayList<>();
    private static ArrayList<String> lonArr = new ArrayList<>(); // 북위
    private static ArrayList<String> latArr = new ArrayList<>(); // 동경
    private static ArrayList<String> distArr = new ArrayList<>();

    EditText edit;
    TextView text;

    ListView listview;
    ListViewAdapter adapter;


    XmlPullParser xpp;

    String data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aed);
        Intent intent = getIntent();
        longitude = intent.getExtras().getDouble("longitude");
        latitude = intent.getExtras().getDouble("lat");

        /******************* 지도 초기화 시작 ********************/
        // 네이버 지도를 넣기 위한 LinearLayout 컴포넌트
        MapContainer = (LinearLayout) findViewById(R.id.MapContainer);
        // 네이버 지도 객체 생성
        mMapView = new NMapView(this);
        // 네이버 지도 객체에 APIKEY 지정
        mMapView.setApiKey(API_KEY);
        // 생성된 네이버 지도 객체를 LinearLayout에 추가시킨다.
        MapContainer.addView(mMapView);
        // 지도를 터치할 수 있도록 옵션 활성화
        mMapView.setClickable(true);
        // 확대/축소를 위한 줌 컨트롤러 표시 옵션 활성화
        mMapView.setBuiltInZoomControls(true, null);
        //showMyLocation(latitude, longitude);
        mMapView.setOnMapStateChangeListener(this);

        /******************* 지도 초기화 끝 ********************/




        /**
        /******************* 오버레이 관련 코드 시작 ********************/
        // 오버레이 리소스 관리객체 할당
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        // 오버레이 관리자 추가
        mOverlayManager = new NMapOverlayManager(this, mMapView,
                mMapViewerResourceProvider);


        /*********************제세동기 위치 관련************************/

        text = (TextView) findViewById(R.id.text);


        ArrayList<ListViewItem> items = new ArrayList<ListViewItem>() ;
        // Adapter 생성
        adapter = new ListViewAdapter(this, R.layout.item, items, this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                data = getXmlData(longitude, latitude); //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //    text.setText(data);  //TextView에 문자열  data 출력
                    }
                });

            }
        });
        thread.start();
        try {
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }

        //스레드 시간차
        if(jangsoArr.size() != 0) {
            for (int j = 0; j < 10; j++) {

                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tele),
                        jangsoArr.get(j), jusoArr.get(j), distArr.get(j),
                        ContextCompat.getDrawable(this, R.drawable.marking));
                adapter.notifyDataSetChanged();
            }
            showAEDList(latitude, longitude);
        }
    }

    /**
     * 지도가 초기화된 후 호출된다.
     * 정상적으로 초기화되면 errorInfo 객체는 null이 전달되며,
     * 초기화 실패 시 errorInfo객체에 에러 원인이 전달된다
     */
    @Override
    public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
        if (errorInfo == null) { // success
            //mMapController.setMapCenter(
            //	new NGeoPoint(126.978371, 37.5666091), 11);
        } else { // fail
            android.util.Log.e("NMAP", "onMapInitHandler: error="
                    + errorInfo.toString());
        }
    }

    /**
     * 지도 레벨 변경 시 호출되며 변경된 지도 레벨이 파라미터로 전달된다.
     */
    @Override
    public void onZoomLevelChange(NMapView mapview, int level) {}

    /**
     * 지도 중심 변경 시 호출되며 변경된 중심 좌표가 파라미터로 전달된다.
     */
    @Override
    public void onMapCenterChange(NMapView mapview, final NGeoPoint center) {
    }

    /**
     * 지도 애니메이션 상태 변경 시 호출된다.
     * animType : ANIMATION_TYPE_PAN or ANIMATION_TYPE_ZOOM
     * animState : ANIMATION_STATE_STARTED or ANIMATION_STATE_FINISHED
     */
    @Override
    public void onAnimationStateChange(NMapView arg0, int animType,
                                       int animState) {   }

    @Override
    public void onMapCenterChangeFine(NMapView arg0) {}

    /** 지도 터치시 이벤트 */
    @Override
    public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {
    }

    @Override
    public void onLongPressCanceled(NMapView nMapView) {
    }

    @Override
    public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {
    }

    @Override
    public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {
    }

    @Override
    public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {

    }

    @Override
    public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {

    }

    /** 오버레이가 클릭되었을 때의 이벤트 */
    @Override
    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
                                                     NMapOverlayItem arg1, Rect arg2) {
        Toast.makeText(this, arg1.getTitle(),
                Toast.LENGTH_SHORT).show();
        return null;
    }




    private void showAEDList(double latitude, double longitude){
        NMapViewerResourceProvider nMapViewerResourceProvider = null;
        NMapOverlayManager nMapOverlayManager;

        nMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        nMapOverlayManager = new NMapOverlayManager(this, mMapView, nMapViewerResourceProvider);

        NGeoPoint myPoint = new NGeoPoint(longitude, latitude);

        // 오버레이들을 관리하기 위한 id값 생성
        int markerId = NMapPOIflagType.PIN;

        // 표시할 위치 데이터를 지정한다. 마지막 인자가 오버레이를 인식하기 위한 id값
        NMapPOIdata poiData = new NMapPOIdata(11, nMapViewerResourceProvider);
        poiData.beginPOIdata(11);
        int aedmarkerId = NMapPOIflagType.SPOT;
        poiData.addPOIitem(myPoint,  "현재위치", markerId, 11);
        for(int i = 0; i < 10; i++) {
            Double longtitude1 = parseDouble(lonArr.get(i));
            Double latitude1 = parseDouble(latArr.get(i));
            NGeoPoint tempPoint = new NGeoPoint(latitude1, longtitude1);
            poiData.addPOIitem(tempPoint, jangsoArr.get(i), aedmarkerId, 11);
        }
        poiData.endPOIdata();

        // 위치 데이터를 사용하여 오버레이 생성
        NMapPOIdataOverlay poiDataOverlay
                = nMapOverlayManager.createPOIdataOverlay(poiData, null);

        // id값이 0으로 지정된 모든 오버레이가 표시되고 있는 위치로
        // 지도의 중심과 ZOOM을 재설정
        poiDataOverlay.showAllPOIdata(11);
        NMapController controller = mMapView.getMapController();
        controller.animateTo(myPoint);
    }



    /**********************제세동기 파싱 *******************************/
    //XmlPullParser를 이용하여 Naver 에서 제공하는 OpenAPI XML 파일 파싱하기(parsing)
    String getXmlData(double longitude, double latitude) {

        double lon =  longitude;
        double lat = latitude;
        StringBuffer buffer = new StringBuffer();

        String queryUrl = "http://openapi.e-gen.or.kr/openapi/service/rest/AEDInfoInqireService/getAedLcinfoInqire?serviceKey=sOeLXw%2B0H2SQr5smAbXhw2Q%2FK6SbySO0oS49h2OW1n4BjyHGRm%2FrXP3DT6n1cywpwCBOYkp92tK3C9FhZEw4YA%3D%3D&" +
                "WGS84_LON=" + lon + "&WGS84_LAT=" + lat +
                "&numOfRows=10&pageSize=1&pageNo=1&startPage=1";

        try {
            URL url = new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("start XML parsing...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("buildAddress")) {
                            buffer.append("주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            jusoArr.add(xpp.getText().toString());
                            buffer.append("\n");          //줄바꿈 문자 추가
                        }
                        else if (tag.equals("distance")) {
                            buffer.append("거리");
                            xpp.next();
                            buffer.append(xpp.getText());
                            distArr.add(xpp.getText().toString()+"km");
                            buffer.append("\n");
                        }
                        else if (tag.equals("org")) {
                            buffer.append("장소 : ");
                            xpp.next();
                            buffer.append(xpp.getText()); //category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            jangsoArr.add(xpp.getText().toString());
                            buffer.append("\n");          //줄바꿈 문자 추가
                        } else if (tag.equals("managerTel")) {
                            buffer.append("연락처 :");
                            xpp.next();
                            telArr.add(xpp.getText().toString());
                            buffer.append(xpp.getText()); //telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");          //줄바꿈 문자 추가
                        } else if (tag.equals("wgs84Lat")) {
                            buffer.append("북위 :");
                            xpp.next();
                            lonArr.add(xpp.getText().toString());
                            buffer.append(xpp.getText()); //mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("  ,  ");          //줄바꿈 문자 추가
                        } else if (tag.equals("wgs84Lon")) {
                            buffer.append("동경 :");
                            xpp.next();
                            latArr.add(xpp.getText().toString());
                            buffer.append(xpp.getText()); //mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");          //줄바꿈 문자 추가
                        }

                        //  adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tele),
                        //        String.valueOf(i) , String.valueOf(i));
                        //  i++;

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기

                        if (tag.equals("item")) buffer.append("\n"); // 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return buffer.toString(); //StringBuffer 문자열 객체 반환

    }//getXmlData method....

    @Override
    public void onListClick(int position) {
        Intent intent;
        Uri uri;
        uri = Uri.parse("tel:"+telArr.get(position));
        intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }
    ////
}