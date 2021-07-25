package 并发.安防报警;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *(1)建立连接，
 * (2)检查心跳，如果报警系统和报警服务器失去连接后
 * (3)重新建立连接 的过程。
 *
 * @Author: znj
 * @Date: 2021/7/5 0005 22:09
 */
public class AlarmAgent {

    /**
     * 报警系统是否连接上了报警服务器
     */
    private volatile boolean connectedToServer = false;

    /**
     * 保护性条件
     */
    Predicate agentConnected = new Predicate() {
        @Override
        public boolean evaluate() {
            // 连接是否建立完成
            return connectedToServer;
        }
    };

    /**
     * blocker对象
     */
    private Blocker blocker = new ConditionVarBlocker(false);

    /**
     * 初始化报警服务
     * 1.和报警服务器建立连接
     * 2.定时发送心跳 检查连接
     * 3.重新建立连接
     */
    public void init() {
        // 报警服务于报警服务器连接的线程
        ConnectingTask connectingTask = new ConnectingTask();
        new Thread(connectingTask).start();

        /**
         * 每 5S 发送一次心跳到报警服务器
         */
        ScheduledThreadPoolExecutor heartbeatExecutor = new ScheduledThreadPoolExecutor(5, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread();
                thread.setName("heartbeat-thread-" + index);
                // 当jvm退出的时候退出
                thread.setDaemon(true);
                return thread;
            }
        });

        /**
         * 两次开始执行最小间隔时间 每5s执行一次
         */
        heartbeatExecutor.scheduleAtFixedRate(new HeartbeatTask(), 5000, 2000, TimeUnit.MILLISECONDS);

    }

    /**
     * 上报报警信息给报警服务
     */
    public void sendAlarm(AlarmInfo alarmInfo) throws Exception {
        // 构建guardedAction
        GuardedAction<Void> guardedAction = new GuardedAction<Void>(agentConnected) {
            @Override
            public Void call() throws Exception {
                // 执行目标函数，发送报警信息给报警服务器
                doSendAlarm(alarmInfo);
                return null;
            }
        };

        // 通过blocker执行目标
        blocker.callWithGuard(guardedAction);
    }



    

    /**
     * 发送报警信息给报警服务器
     *
     * @param alarmInfo 报警信息
     */
    private void doSendAlarm(AlarmInfo alarmInfo) {
        // 建立socket连接发送数据给报警信息
        System.out.println("start send alarm:" + alarmInfo);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 模拟上报50ms
        System.out.println("end sen alarm");
    }









    /**
     *与 报警服务器建立连接的线程
     */
    class ConnectingTask implements Runnable {
        /**
         * blocker对象
         */
        private Blocker blocker = new ConditionVarBlocker(false);

        /**
         * 定时方式每隔10秒建立一次连接
         * 真实可以使用 socketChannel 的方式建立连接
         */
        @Override
        public void run() {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 连接建立完成
            System.out.println("alarm connected");
            onConnected();
        }

        /**
         * 通过 blocker 去唤醒
         */
        private void onConnected() {
            try {
                blocker.signalAfter(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        // 唤醒前的状态动作
                        // 修改连接报警服务器的状态
                        System.out.println("update connectedServer = true");
                        connectedToServer = true;
                        // 条件满足，执行唤醒
                        return Boolean.TRUE;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 心跳检查机制
     *
     * @Author: znj
     * @Date: 2021/7/9 0009 23:04
     */
    class HeartbeatTask implements Runnable {
        @Override
        public void run() {
            // 通过socket和报警服务器发送心跳机制
            if (!testConnection()) {
                // 连接断开
                onDisconnected();
                // 重连
                reconnected();
            }
        }

        /**
         * 重新连接
         */
        private void reconnected() {
            // 重新执行一次
            ConnectingTask connectingTask = new ConnectingTask();
            // 直接通过心跳线程执行一次重连，这里就不单独开启thread
            connectingTask.run();
        }

        /**
         * 测试连接是否正常
         *
         * @return 结果
         */
        private boolean testConnection() {
            // 通过socket给报警服务器发送一次连接
            // 模拟发送一次
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test connection normal");
            return true;
        }

        /**
         * 连接断开
         */
        private void onDisconnected() {
            // 通过volatile的语义让其他线程读取到，其他线程上报报警消息是stateOperation不满足则阻塞
            connectedToServer = false;
        }
    }


}
