package resolution.ex6.vr.aps;

import android.graphics.drawable.Drawable;

/**
 * Created by 강승규 on 2016-11-13.
 */
public class ListViewItem {
    private String distStr;
    private String jusoStr;
    private String jangsoStr;
    private Drawable iconDrawable;
    private Drawable iconDrawableTwo;
//
    public void setDist(String dist) { distStr = dist; }
    public void setJuso(String juso) { jusoStr = juso; }
    public void setJangso(String jangso) { jangsoStr = jangso; }
    public void setIcon(Drawable icon) { iconDrawable = icon; }
    public void setIconTwo(Drawable iconTwo) { iconDrawableTwo = iconTwo; }

    public String getDist() { return this.distStr; }
    public String getJuso() { return this.jusoStr; }
    public String getJangso() { return this.jangsoStr; }
    public Drawable getIcon() { return this.iconDrawable; }
    public Drawable getIconTwo() { return this.iconDrawableTwo; }

}
