package 并发.不可变类;

import java.util.Map;

/**
 * @Author: znj
 * @Date: 2021/6/22 0022 23:31
 */
public class SmsTest {
    public static void main(String[] args) {
        SmsRouter smsRouter = new SmsRouter();
        Map<Integer, SmsInfo> smsInfoRouteMap = smsRouter.getSmsInfoRouteMap();
        System.out.println("->"+smsInfoRouteMap.get(3).toString());
        smsRouter.change();
        Map<Integer, SmsInfo> smsInfoRouteMap1 = smsRouter.getSmsInfoRouteMap();
        System.out.println("->"+smsInfoRouteMap1.get(3).toString());
    }
}
