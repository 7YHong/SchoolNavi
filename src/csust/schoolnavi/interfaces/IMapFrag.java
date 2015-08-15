package csust.schoolnavi.interfaces;

import android.content.Context;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;

/**
 * Created by 7YHong on 2015/7/23.
 */
public interface IMapFrag {

    Context getContext();

    void addRoute(DrivingRouteLine route);

    void addRoute(WalkingRouteLine route);

    void addRoute(TransitRouteLine route);

    void clear();

    void addMarker(OverlayOptions marker);
}
