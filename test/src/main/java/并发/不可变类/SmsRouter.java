package 并发.不可变类;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: znj
 * @Date: 2021/6/22 0022 23:22
 */
public class SmsRouter {

    /**
     * 使用 volatile 关键字修饰来保证其他线程的可见性
     */
    private static volatile SmsRouter instance = new SmsRouter();

    public static SmsRouter getInstance() {
        return instance;
    }

    public static void setInstance(SmsRouter instance) {
        SmsRouter.instance = instance;
    }

    private final Map<Integer,SmsInfo> smsInfoRouteMap;

    public SmsRouter() {
        this.smsInfoRouteMap = this.loadSmsInfoRouteMapFromDb();
    }

    public Map<Integer, SmsInfo> getSmsInfoRouteMap() {
        //防止信息更改
        return Collections.unmodifiableMap( smsInfoRouteMap);
    }

    private Map<Integer,SmsInfo> loadSmsInfoRouteMapFromDb(){
        Map<Integer,SmsInfo> routerMap = new HashMap<>();
        routerMap.put(1,new SmsInfo("http://www.1.com","111"));
        routerMap.put(2,new SmsInfo("http://www.2.com","222"));
        routerMap.put(3,new SmsInfo("http://www.3.com","333"));
        routerMap.put(4,new SmsInfo("http://www.4.com","444"));
        return routerMap;
    }

    private Map<Integer,SmsInfo> deepCopy(Map<Integer,SmsInfo> smsInfoRouteMap){
        Map<Integer,SmsInfo> result = new HashMap<>(smsInfoRouteMap.size());
        for (Map.Entry<Integer,SmsInfo> entity: smsInfoRouteMap.entrySet()) {
            result.put(entity.getKey(),entity.getValue());
        }
        return result;
    }



    public void update(){
        Map<Integer, SmsInfo> smsInfoRouteMap = instance.getSmsInfoRouteMap();
        SmsInfo smsInfo = smsInfoRouteMap.get(3);
        smsInfo.setUrl("http://www.3gai.com");
        smsInfo.setContext("333gai");
    }

    public void  change(){
        update();
        SmsRouter.setInstance(new SmsRouter());
    }

}
