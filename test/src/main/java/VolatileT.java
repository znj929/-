public class VolatileT extends Thread {
    boolean flag = false;
    int a = 0;

    public void run(){
        while (!flag) {
            a++;
        }
    }
    public static void main(String[] args) throws Exception {
        VolatileT volatileT = new VolatileT();
        volatileT.start();
        Thread.sleep(2000);
        volatileT.flag = true;
        System.out.println("停止："+volatileT.a);
    }


}

