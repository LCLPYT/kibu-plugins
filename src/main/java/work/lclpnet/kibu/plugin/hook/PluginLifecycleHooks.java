package work.lclpnet.kibu.plugin.hook;

import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.mplugins.event.PluginLifecycleEvents;

public class PluginLifecycleHooks {

    public static final Hook<PluginLifecycleEvents.Loading> LOADING = HookFactory.createArrayBacked(PluginLifecycleEvents.Loading.class,
            listeners -> (plugin) -> {
                for (var listener : listeners) {
                    listener.loading(plugin);
                }
            });

    public static final Hook<PluginLifecycleEvents.Loaded> LOADED = HookFactory.createArrayBacked(PluginLifecycleEvents.Loaded.class,
            listeners -> (plugin) -> {
                for (var listener : listeners) {
                    listener.loaded(plugin);
                }
            });

    public static final Hook<PluginLifecycleEvents.Unloading> UNLOADING = HookFactory.createArrayBacked(PluginLifecycleEvents.Unloading.class,
            listeners -> (plugin) -> {
                for (var listener : listeners) {
                    listener.unloading(plugin);
                }
            });

    public static final Hook<PluginLifecycleEvents.Unloaded> UNLOADED = HookFactory.createArrayBacked(PluginLifecycleEvents.Unloaded.class,
            listeners -> (plugin) -> {
                for (var listener : listeners) {
                    listener.unloaded(plugin);
                }
            });

    public static final Hook<PluginLifecycleEvents.Reloading> RELOADING = HookFactory.createArrayBacked(PluginLifecycleEvents.Reloading.class,
            listeners -> (plugins) -> {
                for (var listener : listeners) {
                    listener.reloading(plugins);
                }
            });

    public static final Hook<PluginLifecycleEvents.Reloaded> RELOADED = HookFactory.createArrayBacked(PluginLifecycleEvents.Reloaded.class,
            listeners -> (plugins) -> {
                for (var listener : listeners) {
                    listener.reloaded(plugins);
                }
            });

    public static final Hook<PluginLifecycleEvents.WorldStateChange> WORLD_STATE_CHANGED = HookFactory.createArrayBacked(PluginLifecycleEvents.WorldStateChange.class,
            listeners -> (ready) -> {
                for (var listener : listeners) {
                    listener.onWorldStateChanged(ready);
                }
            });

    static {
        PluginLifecycleEvents.LOADING.register(loadedPlugin -> LOADING.invoker().loading(loadedPlugin));
        PluginLifecycleEvents.LOADED.register(loadedPlugin -> LOADED.invoker().loaded(loadedPlugin));
        PluginLifecycleEvents.UNLOADING.register(loadedPlugin -> UNLOADING.invoker().unloading(loadedPlugin));
        PluginLifecycleEvents.UNLOADED.register(loadedPlugin -> UNLOADED.invoker().unloaded(loadedPlugin));
        PluginLifecycleEvents.RELOADING.register(loadedPlugin -> RELOADING.invoker().reloading(loadedPlugin));
        PluginLifecycleEvents.RELOADED.register(loadedPlugin -> RELOADED.invoker().reloaded(loadedPlugin));
        PluginLifecycleEvents.WORLD_STATE_CHANGED.register(loadedPlugin -> WORLD_STATE_CHANGED.invoker().onWorldStateChanged(loadedPlugin));
    }

    private PluginLifecycleHooks() {}
}
