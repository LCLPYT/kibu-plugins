package work.lclpnet.kibu.plugin.hook;

import work.lclpnet.kibu.plugin.ext.KibuPlugin;

/**
 * Interface for registering event listeners.
 * Can be used to listen for multiple events that may be relevant for a module.
 * <p>
 * Implementations can easily be registered using {@link KibuPlugin#registerHooks(HookListenerModule)}
 */
public interface HookListenerModule {

    void registerListeners(HookRegistrar registrar);
}
