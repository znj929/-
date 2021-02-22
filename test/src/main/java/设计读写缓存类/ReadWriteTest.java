package 设计读写缓存类;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写缓存
 *
 * @author 119503
 */
public class ReadWriteTest {

    /**
     * 简单作为一个缓存容器
     */
    private static ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

    public static Object getValue(String key) {
        //从缓存中取值
        Object obj = map.get(key);
        if(obj == null ){
            //缓存中没有取到值，去数据库中取
            obj = "a";
            //数据库中取出值放入map中
            map.put(key,obj);
        }
        return obj;
    }





    /**
     * 简单作为一个缓存容器
     */
    private static HashMap<String, Object> hashMap = new HashMap<>();
    /**
     * 声明一个读写锁
     */
    private static ReadWriteLock rwl = new ReentrantReadWriteLock();


    public static Object getValueForLock(String key) {
        Object obj = null;
        try {
            //开启读锁
            rwl.readLock().lock();
            //从缓存中取值
            obj = map.get(key);
            if(obj == null ){
                try{
                    //关闭读锁
                    rwl.readLock().unlock();
                    //开启写锁
                    rwl.writeLock().lock();
                    //缓存中没有取到值，去数据库中取
                    obj = "a";
                    //数据库中取出值放入map中
                    map.put(key,obj);
                }catch (Exception e){e.printStackTrace();}
                finally {
                    rwl.writeLock().unlock();
                    rwl.readLock().lock();//开启读锁
                }
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            rwl.readLock().unlock();
        }
        return obj;
    }


}
