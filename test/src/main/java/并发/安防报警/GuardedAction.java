package 并发.安防报警;

import java.util.concurrent.Callable;

/**
 * 抽象目标动作，内部包含目标动作所需的保护条件
 * @author Administrator
 * @param <V>
 */
public abstract class GuardedAction<V> implements Callable<V> {

    /**
     * 保护条件
     */
    protected final Predicate predicate;

    public GuardedAction(Predicate predicate) {
        this.predicate = predicate;
    }
}