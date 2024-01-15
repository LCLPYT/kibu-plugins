package work.lclpnet.kibu.plugin;

import work.lclpnet.kibu.plugin.ext.TranslatedPlugin;
import work.lclpnet.kibu.plugin.hook.TranslationsLoadedCallback;
import work.lclpnet.kibu.plugin.util.KibuLanguagePreferenceProvider;
import work.lclpnet.kibu.plugin.util.KibuTranslationLoader;
import work.lclpnet.kibu.translate.TranslationService;
import work.lclpnet.kibu.translate.pref.LanguagePreferenceProvider;
import work.lclpnet.mplugins.event.PluginBootstrapEvents;
import work.lclpnet.mplugins.event.PluginLifecycleEvents;
import work.lclpnet.mplugins.event.PluginShutdownEvents;
import work.lclpnet.mplugins.ext.MPluginsInit;
import work.lclpnet.plugin.Plugin;
import work.lclpnet.translations.DefaultLanguageTranslator;

import java.util.concurrent.atomic.AtomicBoolean;

public class KibuPlugins implements MPluginsInit {

    private final KibuTranslationLoader translationLoader;
    private final KibuLanguagePreferenceProvider languagePreferenceProvider;
    private final DefaultLanguageTranslator translator;
    private final TranslationService translationService;
    private final AtomicBoolean bootstrap = new AtomicBoolean(false);
    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    public KibuPlugins() {
        translationLoader = new KibuTranslationLoader();
        languagePreferenceProvider = new KibuLanguagePreferenceProvider();
        translator = new DefaultLanguageTranslator(translationLoader);
        translationService = new TranslationService(translator, languagePreferenceProvider);
    }

    @Override
    public void beforeBootstrap() {
        PluginLifecycleEvents.LOADING.register(loadedPlugin -> {
            Plugin plugin = loadedPlugin.getPlugin();

            if (plugin instanceof TranslatedPlugin translatedPlugin) {
                initTranslatedPlugin(translatedPlugin);
            }

            if (plugin instanceof LanguagePreferenceProvider provider) {
                languagePreferenceProvider.addChild(provider);
            }
        });

        PluginLifecycleEvents.UNLOADING.register(loadedPlugin -> {
            Plugin plugin = loadedPlugin.getPlugin();

            if (plugin instanceof TranslatedPlugin translatedPlugin) {
                unregisterTranslatedPlugin(translatedPlugin);
            }

            if (plugin instanceof LanguagePreferenceProvider provider) {
                languagePreferenceProvider.removeChild(provider);
            }
        });

        PluginBootstrapEvents.BEGIN.register(pluginFrame -> bootstrap.set(true));

        PluginBootstrapEvents.COMPLETE.register(pluginFrame -> {
            bootstrap.set(false);

            if (translationLoader.isDirty()) {
                reloadTranslations();
            }
        });

        PluginShutdownEvents.BEGIN.register(pluginManager -> shutdown.set(true));

        PluginShutdownEvents.COMPLETE.register(pluginManager -> shutdown.set(false));
    }

    private void unregisterTranslatedPlugin(TranslatedPlugin translatedPlugin) {
        translationLoader.unregister(translatedPlugin);

        if (!bootstrap.get() && !shutdown.get()) {
            reloadTranslations();
        }
    }

    private void initTranslatedPlugin(TranslatedPlugin translatedPlugin) {
        translationLoader.register(translatedPlugin);

        translatedPlugin.injectTranslationService(translationService);

        if (!bootstrap.get()) {
            reloadTranslations();
        }
    }

    private void reloadTranslations() {
        translator.reload().thenRun(() -> {
            translationLoader.setDirty(false);
            TranslationsLoadedCallback.HOOK.invoker().onLoaded(translationService);
        });
    }
}
