package csust.schoolnavi.present;

import android.content.Context;
import android.os.AsyncTask;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import csust.schoolnavi.R;
import csust.schoolnavi.impl.JsonReader;
import csust.schoolnavi.impl.RoutePlanMgr;
import csust.schoolnavi.interfaces.IMapFrag;
import csust.schoolnavi.model.LocMsg;

import java.util.List;

/**
 * Created by 7YHong on 2015/7/23.
 */
public class MapPresent {
    //地图Frag接口
    IMapFrag mapFrag;
    //JSON工具
    JsonReader jsonReader;
    //路径规划工具
    RoutePlanMgr routePlanMgr;
    //Context
    Context c;

    public MapPresent(final IMapFrag mapFrag) {
        this.mapFrag = mapFrag;
        jsonReader = JsonReader.getInstance();
        routePlanMgr = RoutePlanMgr.get();
        c = mapFrag.getContext();

        //读取位置信息并添加到地图上
        new AsyncTask<Void, Void, List<LocMsg>>() {
            @Override
            protected List doInBackground(Void... params) {
                //jsonReader.init(c.getExternalCacheDir() + "/locmsg.data");
                jsonReader.init(c);
                return jsonReader.getAll();
            }

            @Override
            protected void onPostExecute(List<LocMsg> list) {
                super.onPostExecute(list);
                BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);

                for (LocMsg locMsg : list) {
                    LatLng ll = new LatLng(locMsg.getLatitude(), locMsg.getLongitude());
                    OverlayOptions op = new MarkerOptions()
                            .title(locMsg.getName())
                            .position(ll)
                            .icon(bitmap);
                    mapFrag.addMarker(op);
                }
                bitmap=null;
            }
        }.execute();
    }

    public void dispCurrentOverlay() {
        mapFrag.clear();
        RouteLine routeLine = routePlanMgr.getSearchResult().get(0);
        int type = routePlanMgr.getType();
        switch (type) {
            case RoutePlanMgr.TYPE_DRIVING:
                mapFrag.addRoute((DrivingRouteLine) routeLine);
                break;
            case RoutePlanMgr.TYPE_WALKING:
                mapFrag.addRoute((WalkingRouteLine) routeLine);
                break;
            case RoutePlanMgr.TYPE_TRANSIT:
                mapFrag.addRoute((TransitRouteLine) routeLine);
                break;
        }
    }
}
