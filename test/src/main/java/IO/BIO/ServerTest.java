package IO.BIO;

import java.io.IOException;
import java.util.Random;

/**
 * 服务端启动
 * @Author: znj
 * @Date: 2021/2/27 0027 22:00
 */
public class ServerTest {
    //测试主方法
    public static void main(String[] args) throws InterruptedException {
        //运行服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerNormal.start();
                    ServerBetter.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
