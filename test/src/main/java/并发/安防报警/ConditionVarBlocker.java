package 并发.安防报警;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于jdk中condition条件变量实现的blocker
 *
 * @Author: znj
 * @Date: 2021/7/5 0005 22:18
 */
public class ConditionVarBlocker implements Blocker {


    /**
     * lock 锁
     */
    private final Lock lock;

    /**
     * 条件变量
     */
    private final Condition condition;

    /**
     * 是否允许获取当前blocker的锁
     */
    private final boolean allowAccess2Lock;

    public ConditionVarBlocker(Lock lock, Condition condition, boolean allowAccess2Lock) {
        this.lock = lock;
        this.condition = condition;
        this.allowAccess2Lock = allowAccess2Lock;
    }

    public ConditionVarBlocker(boolean allowAccess2Lock) {
        this(new ReentrantLock(), allowAccess2Lock);
    }

    public ConditionVarBlocker(Lock lock, boolean allowAccess2Lock) {
        this.lock = lock;
        this.condition = lock.newCondition();
        this.allowAccess2Lock = allowAccess2Lock;
    }


    /**
     * 先执行stateOperation，如果返回true则确定唤醒该Blocker上阻塞的一个线程
     * @param stateOperation
     * @throws Exception
     */
    @Override
    public void signalAfter(Callable<Boolean> stateOperation) throws Exception {
        //显示中断点
        lock.lockInterruptibly();
        try {
            if (stateOperation.call()) {
                // 条件满足唤醒
                System.out.println("alarm connected ,signal thread ");
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <V> V callWithGuard(GuardedAction<V> guardedAction) throws Exception {
        lock.lockInterruptibly();
        try {
            // 判断条件是否满足，满足则执行目标动作，不满足则进入到条件等待队列中
            final Predicate predicate = guardedAction.predicate;
            while (!predicate.evaluate()) {
                System.out.println("alarm connecting alarm system，thread wait");
                // 条件不满足
                condition.await();

                // 当线程从条件等待队列欢迎后，获取锁成功，然后再次尝试去判断条件是否满足
            }

            // 条件满足，执行目标内容
            System.out.println("alarm connected execute call");
            return guardedAction.call();
        } finally {
            lock.unlock();
        }
    }
}
