package work.lclpnet.kibu.plugin;

import work.lclpnet.kibu.plugin.cmd.CommandRegistrar;
import work.lclpnet.kibu.plugin.hook.HookRegistrar;
import work.lclpnet.kibu.scheduler.api.Scheduler;

public interface PluginContext extends HookRegistrar, CommandRegistrar {

    Scheduler getScheduler();
}
