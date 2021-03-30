import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @Author: znj
 * @Date: 2021/3/25 0025 0:09
 */
public class test {
    /**
     * 强引用
     */
    public static A a = new A();

    /**
     * 软引用
     */
    public static SoftReference<A> a1 = new SoftReference<>(new A());

    /**
     * 弱引用
     */
    public static WeakReference<A> a2 = new WeakReference<>(new A());

    public static void main(String[] args) {
        A a = new A();
        a.check();
    }
}

