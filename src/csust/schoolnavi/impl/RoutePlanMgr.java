package csust.schoolnavi.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.*;
import csust.schoolnavi.interfaces.OnTaskDoneListener;
import csust.schoolnavi.interfaces.RoutePM_inter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 7YHong on 2015/7/6.
 */
public class RoutePlanMgr implements RoutePM_inter,OnGetRoutePlanResultListener {
    public static final int TYPE_DRIVING=1;
    public static final int TYPE_WALKING=2;
    public static final int TYPE_TRANSIT=3;

    private static RoutePlanMgr ourInstance = new RoutePlanMgr();
    private static Context c;
    RoutePlanSearch mSearch;
    Map<String,Object> searchResult;//List<XXRouteLine>��������ת��ΪObject    ͬʱ����·���滮�ĸ�����Ϣ
    OnTaskDoneListener listener;

    private RoutePlanMgr() {
        mSearch=RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        searchResult =new HashMap<String, Object>();
    }

    public static RoutePlanMgr get(Context context){
        c=context;
        return ourInstance;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    /**
     *
     * @param result List<DrivingRouteLine>
     * �����������д�������ҳ�������ʾ�������лص�
     * ֻ�е������ȷ���Ž�������벢���лص�
     */
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(c, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
            //result.getSuggestAddrInfo()
            Toast.makeText(c, "��ȷ������ĵص�û������!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            //�˴���Map�з���һ��List<DrivingRouteLine>
            searchResult.put("type",TYPE_DRIVING);
            searchResult.put("result",result.getRouteLines());
            listener.onTaskDone(searchResult);
        }
        Log.i("RoutePlanManager","GetDrivingRouteResult ed!");
    }

    /**
     *
     * @param stNode
     * @param enNode
     * @param listener �ص��ӿڽ�����List<XXRouteLine>���͵�Object
     *
     */
    @Override
    public void SearchDriving(PlanNode stNode, PlanNode enNode, OnTaskDoneListener listener) {
        this.listener=listener;
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode));
        //Log.i("RoutePlanManager","���ؿ�ֵ");
        //if(result==null) Toast.makeText(c,"���ؿ�ֵ",Toast.LENGTH_SHORT).show();

        //return ((DrivingRouteResult)result).getSearchResult();
    }

    @Override
    public List<TransitRouteLine> SearchTransit(PlanNode stNode, PlanNode enNode) {
        return null;
    }

    @Override
    public List<WalkingRouteLine> SearchWalking(PlanNode stNode, PlanNode enNode) {
        return null;
    }

    @Override
    public Map getSearchResult() {
        return searchResult;
    }

}
