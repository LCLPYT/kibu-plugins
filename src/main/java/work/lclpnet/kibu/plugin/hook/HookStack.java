package work.lclpnet.kibu.plugin.hook;

import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.plugin.util.UnloadableStack;

public class HookStack extends UnloadableStack<HookContainer> implements HookRegistrar {

    public HookStack() {
        super(HookContainer::new);
    }

    @Override
    public <T> void registerHook(Hook<T> hook, T listener) {
        current().registerHook(hook, listener);
    }

    @Override
    public <T> void unregisterHook(Hook<T> hook, T listener) {
        current().unregisterHook(hook, listener);
    }

    @Override
    public void registerHooks(HookListenerModule hooks) {
        current().registerHooks(hooks);
    }
}
