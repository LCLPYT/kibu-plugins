package work.lclpnet.kibu.plugin.ext;

import org.slf4j.Logger;
import work.lclpnet.kibu.plugin.cmd.CommandRegistrar;
import work.lclpnet.kibu.plugin.hook.HookRegistrar;
import work.lclpnet.kibu.scheduler.api.Scheduler;
import work.lclpnet.mplugins.ext.PluginEnvironment;

public interface PluginContext extends HookRegistrar, CommandRegistrar {

    Scheduler getScheduler();

    PluginEnvironment getEnvironment();

    Logger getLogger();
}
