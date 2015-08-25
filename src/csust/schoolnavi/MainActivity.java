package csust.schoolnavi;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import csust.schoolnavi.fragment.MyMapFragment;
import csust.schoolnavi.util.MyLocationListener;

/**
 * Created by 7YHong on 2015/7/5.
 */
public class MainActivity extends FragmentActivity {


    private MyMapFragment mapfrag;
    private LocationClient mLocClient;
    private MyLocationListener mLocListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化定位SDK
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_fragment);

        mapfrag = new MyMapFragment();
        initLocation();
        //将MapFragment添加进视图
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.map, mapfrag, "map_fragment").commit();
    }

    /**
     * 定位初始化
     */
    private void initLocation(){
        //这里要从含有BaiduMap的地方拿OnLocationListener出来
        mLocListener = new MyLocationListener(mapfrag.getLocationListener());
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(mLocListener);
        LocationClientOption option = new LocationClientOption();
        //设置定位服务参数
        //option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);       //获取位置的间隔时间
        option.setIsNeedAddress(true);  //设置获取地址信息
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        super.onDestroy();
    }
}