package IO.NIO;

import java.util.Scanner;

/**
 * @Author: znj
 * @Date: 2021/2/28 0028 21:26
 */
public class ClientTest {
    public static void main(String[] args) throws Exception {
        //运行客户端
        Client.start();
        while(Client.sendMsg(new Scanner(System.in).nextLine())) {
            ;
        }
    }
}
