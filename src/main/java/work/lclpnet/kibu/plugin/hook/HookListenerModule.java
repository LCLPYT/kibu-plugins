package work.lclpnet.kibu.plugin.hook;

/**
 * Interface for registering event listeners.
 * Can be used to listen for multiple events that may be relevant for a module.
 * <p>
 * Implementations can easily be registered using {@link work.lclpnet.kibu.plugin.KibuPlugin#registerHooks(HookListenerModule)}
 */
public interface HookListenerModule {

    void registerListeners(HookRegistrar registrar);
}
