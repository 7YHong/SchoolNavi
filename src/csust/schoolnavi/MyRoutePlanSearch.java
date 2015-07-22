package csust.schoolnavi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.route.*;
import csust.schoolnavi.impl.RoutePlanMgr;
import csust.schoolnavi.interfaces.OnTaskDoneListener;
import csust.schoolnavi.tools.MyAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by 7YHong on 2015/7/6.
 * ·���滮Activity
 * Ҫ��ҵ��ʵ���෵��List<XXRouteLine>����
 */
public class MyRoutePlanSearch extends Activity {
    /**
     * ��̬�����ı�Ÿ���Activity���ֵ�˳�����
     */
    public static final int SEARCH_WITHDISP=21;
    public static final int SEARCH_WITHNODISP=20;

    EditText start,end;
    List routeLines;//�ں�XXRouteLine��Ԫ��
    RecyclerView rv;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=getApplicationContext();
        SDKInitializer.initialize(c);
        setContentView(R.layout.activity_routeplan);
        start=(EditText)findViewById(R.id.start);
        end=(EditText)findViewById(R.id.end);
        //lv=(ListView)findViewById(R.id.itemlist);
        rv=(RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager manager=new LinearLayoutManager(c);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);

    }

    public void SearchButtonProcess(View v) {
        routeLines =null;
        Log.i("TAG","Button Clicked!");
        switch (v.getId()) {
            case R.id.drive:
                searchDriving();
                break;
            default:
                Toast.makeText(c, "��ť���ڻ�����Ч", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void BackToMap(View v){
        Toast.makeText(c,"��ť��������!",Toast.LENGTH_SHORT).show();
        setResult(SEARCH_WITHDISP);
        finish();
    }

    private void searchDriving() {
        PlanNode stNode=PlanNode.withCityNameAndPlaceName("����",start.getText().toString());
        PlanNode enNode=PlanNode.withCityNameAndPlaceName("����",end.getText().toString());
        RoutePlanMgr.get(c).SearchDriving(stNode, enNode, new OnTaskDoneListener() {
            @Override
            public void onTaskDone(Map result) {
                routeLines =(List)result.get("result");
                Log.i("OnTaskDone_SearchBack",String.valueOf(routeLines.size()));
                rv.setAdapter(new MyAdapter(routeLines.get(0)));
            }
        });
        //routeLines = RoutePlanMgr.get(c).SearchDriving(stNode,enNode);
        //lv.setAdapter(new MyAdapter(routeLines.get(0)));
    }


    /*private class MyAdapter extends BaseAdapter {
        DrivingRouteLine routeLine;
        MyAdapter(Object routeLine){
            //�˴�ֱ�Ӱ���ȡ�����Ľ��ת������DrivingRouteLine���ͣ���Ϊ��
            this.routeLine=(DrivingRouteLine)routeLine;
        }

        @Override
        public int getCount() {
            return routeLine.getAllStep().size();
        }

        @Override
        public Object getItem(int position) {
            return routeLine.getAllStep().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                LayoutInflater mInflater=LayoutInflater.from(parent.getContext());
                convertView = mInflater.inflate(R.layout.simple2text, parent,false);

            }
            TextView tv1=(TextView)convertView.findViewById(R.id.text1);
            TextView tv2=(TextView)convertView.findViewById(R.id.text2);
            String text1=null,text2=null;
            Object step=getItem(position);
            if (step instanceof DrivingRouteLine.DrivingStep) {
                text2 = ((DrivingRouteLine.DrivingStep) step).getEntrace().getLocation().toString();
                text1 = ((DrivingRouteLine.DrivingStep) step).getInstructions();
            } else if (step instanceof WalkingRouteLine.WalkingStep) {
                text2 = ((WalkingRouteLine.WalkingStep) step).getEntrace().getLocation().toString();
                text1 = ((WalkingRouteLine.WalkingStep) step).getInstructions();
            } else if (step instanceof TransitRouteLine.TransitStep) {
                text2 = ((TransitRouteLine.TransitStep) step).getEntrace().getLocation().toString();
                text1 = ((TransitRouteLine.TransitStep) step).getInstructions();
            }
            tv1.setText(text1);
            tv2.setText(text2);
            return convertView;
        }
    }*/

}
