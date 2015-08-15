package csust.schoolnavi.impl;

import android.content.Context;
import com.google.gson.Gson;
import csust.schoolnavi.R;
import csust.schoolnavi.model.LocMsg;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QPF on 2015/8/13.
 */
public class JsonReader {

    List<LocMsg> list;
    Gson gson;  //用来JSON与对象互转的工具


    //单例方法
    private static JsonReader ourInstance = new JsonReader();

    public static JsonReader getInstance() {
        return ourInstance;
    }

    private JsonReader() {
        gson=new Gson();
        list=new ArrayList<LocMsg>();
    }

    public boolean init(Context c) {
        try{
            BufferedReader reader=new BufferedReader(new InputStreamReader(c.getResources().openRawResource(R.raw.locmsg),"utf-8"));
            String temp;
            while ((temp=reader.readLine())!=null){
                LocMsg locMsg=gson.fromJson(temp,LocMsg.class);
                list.add(locMsg);
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public List<LocMsg> getAll() {
        return list;
    }

    public LocMsg get(int index) {
        return list.get(index);
    }

}
