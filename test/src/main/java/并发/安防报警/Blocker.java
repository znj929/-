package 并发.安防报警;

import java.util.concurrent.Callable;

/**
 * @Author: znj
 * @Date: 2021/7/5 0005 22:17
 */
public interface Blocker {
    /**
     * 先执行stateOperation，如果返回true则确定唤醒该Blocker上阻塞的一个线程
     * @param stateOperation
     * @throws Exception
     */
    void signalAfter(Callable<Boolean> stateOperation) throws Exception;

    /**
     * 在保护条件成立时执行目标动作，否则阻塞当前线程，知道保护条件成立
     *
     * @param guardedAction 带保护条件的目标动作
     * @param <V>           执行目标动作返回的结果泛型参数类型
     * @return 执行目标动作返回的结果
     * @throws Exception 异常信息
     */
    <V> V callWithGuard(GuardedAction<V> guardedAction) throws Exception;
}
