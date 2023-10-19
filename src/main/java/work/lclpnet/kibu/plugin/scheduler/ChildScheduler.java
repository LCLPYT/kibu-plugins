package work.lclpnet.kibu.plugin.scheduler;

import org.slf4j.Logger;
import work.lclpnet.kibu.scheduler.RootScheduler;
import work.lclpnet.kibu.scheduler.api.Scheduler;
import work.lclpnet.mplugins.ext.Unloadable;

public class ChildScheduler extends Scheduler implements Unloadable {

    private final RootScheduler parent;

    public ChildScheduler(RootScheduler parent, Logger logger) {
        super(logger);

        this.parent = parent;
        this.parent.addChild(this);
    }

    @Override
    public void unload() {
        this.parent.removeChild(this);
    }
}
