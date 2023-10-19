package work.lclpnet.kibu.plugin.scheduler;

import org.slf4j.Logger;
import work.lclpnet.kibu.plugin.util.UnloadableStack;
import work.lclpnet.kibu.scheduler.KibuScheduling;
import work.lclpnet.kibu.scheduler.RootScheduler;
import work.lclpnet.kibu.scheduler.api.ScheduledTask;
import work.lclpnet.kibu.scheduler.api.SchedulerAction;
import work.lclpnet.kibu.scheduler.api.TaskHandle;
import work.lclpnet.kibu.scheduler.api.TaskScheduler;

public class SchedulerStack extends UnloadableStack<ChildScheduler> implements TaskScheduler {

    public SchedulerStack(Logger logger) {
        this(KibuScheduling.getRootScheduler(), logger);
    }

    public SchedulerStack(RootScheduler root, Logger logger) {
        super(() -> new ChildScheduler(root, logger));
    }

    @Override
    public TaskHandle schedule(ScheduledTask task) {
        return current().schedule(task);
    }

    @Override
    public TaskHandle immediate(SchedulerAction action) {
        return current().immediate(action);
    }

    @Override
    public TaskHandle immediate(Runnable action) {
        return current().immediate(action);
    }

    @Override
    public TaskHandle timeout(SchedulerAction action, long timeoutTicks) {
        return current().timeout(action, timeoutTicks);
    }

    @Override
    public TaskHandle timeout(Runnable action, long timeoutTicks) {
        return current().timeout(action, timeoutTicks);
    }

    @Override
    public TaskHandle interval(SchedulerAction action, long intervalTicks) {
        return current().interval(action, intervalTicks);
    }

    @Override
    public TaskHandle interval(Runnable action, long intervalTicks) {
        return current().interval(action, intervalTicks);
    }

    @Override
    public TaskHandle interval(SchedulerAction action, long intervalTicks, long timeoutTicks) {
        return current().interval(action, intervalTicks, timeoutTicks);
    }

    @Override
    public TaskHandle interval(Runnable action, long intervalTicks, long timeoutTicks) {
        return current().interval(action, intervalTicks, timeoutTicks);
    }
}
