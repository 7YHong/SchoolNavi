package csust.schoolnavi.util;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MyLocationData;

/**
 * Created by QPF on 2015/8/15.
 */
public class MyLocationListener implements BDLocationListener {
    OnLocationListener locationListener;

    //在该类实例化时传入OnLocationListener
    public MyLocationListener(OnLocationListener locationListener) {
        this.locationListener = locationListener;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
// mapfrag view 销毁后不在处理新接收的位置
        if (location == null || locationListener == null)
            return;
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();

        //位置改变时调用接口将结果反馈到地图上
        locationListener.setCurrentLocation(locData);

//        map.setMyLocationData(locData);
//        if (isFirstLoc) {
//            isFirstLoc = false;
//            LatLng ll = new LatLng(location.getLatitude(),
//                    location.getLongitude());
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//            map.animateMapStatus(u);
//        }
    }

    //利用此接口将位置信息传出
    public interface OnLocationListener {
        void setCurrentLocation(MyLocationData locData);
    }
}
