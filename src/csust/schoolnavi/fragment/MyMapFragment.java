package csust.schoolnavi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import csust.schoolnavi.MyRoutePlanSearch;
import csust.schoolnavi.R;
import csust.schoolnavi.impl.RoutePlanMgr;
import csust.schoolnavi.interfaces.IMapFrag;
import csust.schoolnavi.present.MapPresent;
import csust.schoolnavi.util.MyLocationListener;

/**
 * Created by 7YHong on 2015/7/5.
 */
public class MyMapFragment extends Fragment implements IMapFrag {
    public final int MAP_SEARCHROUTE = 11;

    double latitude, longitude;

    private MapView bmap;
    private BaiduMap map;
    private boolean isFirstLoc = true;
    private LocationMode mLocMode;

    MapPresent present;

    OverlayManager routeOverlay;

    /**
     * @return 用于设置位置的接口
     */
    public csust.schoolnavi.util.MyLocationListener.OnLocationListener getLocationListener() {

        return new csust.schoolnavi.util.MyLocationListener.OnLocationListener() {
            @Override
            public void setCurrentLocation(MyLocationData locData) {
                map.setMyLocationData(locData);
                latitude = locData.latitude;
                longitude = locData.longitude;
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(locData.latitude,
                            locData.longitude);
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    map.animateMapStatus(u);
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        present = new MapPresent(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapStatus ms = new MapStatus.Builder().overlook(-20).zoom(16).build();
        BaiduMapOptions bo = new BaiduMapOptions().mapStatus(ms)
                .compassEnabled(true).zoomControlsEnabled(true);
        bmap = new MapView(getActivity(), bo);
        map = bmap.getMap();

        //设置位置设定
        mLocMode = LocationMode.NORMAL;
        map.setMyLocationConfigeration(new MyLocationConfiguration(mLocMode, true, null));
        map.setMyLocationEnabled(true);


        return bmap;
    }

    /**
     * 以下是接口方法
     */

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void addRoute(DrivingRouteLine route) {
        DrivingRouteOverlay overlay = new DrivingRouteOverlay(map);
        routeOverlay = overlay;
        map.setOnMarkerClickListener(overlay);
        overlay.setData(route);
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    @Override
    public void addRoute(WalkingRouteLine route) {
        WalkingRouteOverlay overlay = new WalkingRouteOverlay(map);
        routeOverlay = overlay;
        map.setOnMarkerClickListener(overlay);
        overlay.setData(route);
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    @Override
    public void addRoute(TransitRouteLine route) {
        TransitRouteOverlay overlay = new TransitRouteOverlay(map);
        routeOverlay = overlay;
        map.setOnMarkerClickListener(overlay);
        overlay.setData(route);
        overlay.addToMap();
        overlay.zoomToSpan();

    }


    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void addMarker(OverlayOptions marker) {
        map.addOverlay(marker);
    }

    /**
     * 以下是菜单
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_searchroute: {
                Toast.makeText(getActivity(), "发起路径规划!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), MyRoutePlanSearch.class);
                startActivityForResult(i, MAP_SEARCHROUTE);
            }
            break;
            case R.id.map_loc: {
                LatLng ll = new LatLng(latitude, longitude);
                map.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
                Log.i("CURRENT LOCATION", String.valueOf(latitude) + " " + String.valueOf(longitude));
            }
            break;
            default:
                Toast.makeText(getActivity(), "暂未绑定事件", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 以下对应生命周期方法
     */
    @Override
    public void onResume() {
        super.onResume();
        bmap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        bmap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.setMyLocationEnabled(false);
        bmap.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", "act result 1");
        if (requestCode != MAP_SEARCHROUTE) return;
        Log.i("TAG", "act result 2");
        if (resultCode != MyRoutePlanSearch.SEARCH_WITHDISP) return;
        Log.i("TAG", "act result 3");
        present.dispCurrentOverlay();
    }

}
