package userthreadfactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class UserThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable runnableTask) {
        return Executors.defaultThreadFactory().newThread(runnableTask);
    }
}
