package resolution.ex6.vr.aps;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter implements View.OnClickListener {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    int resourceId;

    public interface ListClickListener {
        void onListClick(int position) ;
    }

    private ListClickListener listClickListener;

    // ListViewAdapter의 생성자
    public ListViewAdapter(Context context, int resource, ArrayList<ListViewItem> list, ListClickListener clickListener) {
        super(context, resource, list) ;

        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource ;

        this.listClickListener = clickListener ;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.telImageView) ;
        ImageView iconImageViewTwo = (ImageView) convertView.findViewById(R.id.markImageView);
        TextView jangsoTextView = (TextView) convertView.findViewById(R.id.jangsoTextView) ;
        TextView jusoTextView = (TextView) convertView.findViewById(R.id.jusoTextView) ;
        TextView distTextView = (TextView) convertView.findViewById(R.id.distTextView) ;


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getIcon());
        iconImageViewTwo.setImageDrawable(listViewItem.getIconTwo());
        jangsoTextView.setText(listViewItem.getJangso());
        jusoTextView.setText(listViewItem.getJuso());
        distTextView.setText(listViewItem.getDist());

        ImageView telImageView = (ImageView) convertView.findViewById(R.id.telImageView);
        telImageView.setTag(position);
        telImageView.setOnClickListener(this);

        ImageView markImageView = (ImageView) convertView.findViewById(R.id.markImageView);
        markImageView.setTag(position);
        markImageView.setOnClickListener(this);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable icon, String jangso, String juso, String dist, Drawable iconTwo) {
        ListViewItem item = new ListViewItem();

        item.setIcon(icon);
        item.setJangso(jangso);
        item.setJuso(juso);
        item.setIconTwo(iconTwo);
        item.setDist(dist);


        listViewItemList.add(item);
    }

    // button2가 눌려졌을 때 실행되는 onClick함수.
    public void onClick(View v) {
        // ListClickListener(MainActivity)의 onListClick() 함수 호출.
        if (this.listClickListener != null) {
            this.listClickListener.onListClick((int)v.getTag()) ;
        }
    }
}
