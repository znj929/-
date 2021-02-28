package IO.AIO;

import IO.AIO.client.Client;

import java.util.Scanner;

/**
 * @Author: znj
 * @Date: 2021/2/28 0028 21:45
 */
public class ClientTest {
    public static void main(String[] args) throws Exception {
        Client.start();
        System.out.println("请输入请求消息：");
        Scanner scanner = new Scanner(System.in);
        while(Client.sendMsg(scanner.nextLine())) {
            ;
        }
    }
}
