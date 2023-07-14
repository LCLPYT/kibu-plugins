package work.lclpnet.kibu.plugin;

import net.fabricmc.api.ModInitializer;
import work.lclpnet.kibu.plugin.ext.TranslatedPlugin;
import work.lclpnet.kibu.plugin.hook.TranslationsLoadedCallback;
import work.lclpnet.kibu.plugin.util.KibuLanguagePreferenceProvider;
import work.lclpnet.kibu.plugin.util.KibuTranslationLoader;
import work.lclpnet.kibu.translate.TranslationService;
import work.lclpnet.kibu.translate.pref.LanguagePreferenceProvider;
import work.lclpnet.mplugins.event.PluginBootstrapEvents;
import work.lclpnet.mplugins.event.PluginLifecycleEvents;
import work.lclpnet.plugin.Plugin;
import work.lclpnet.translations.DefaultLanguageTranslator;

public class KibuPlugins implements ModInitializer {

    private final KibuTranslationLoader translationLoader;
    private final KibuLanguagePreferenceProvider languagePreferenceProvider;
    private final DefaultLanguageTranslator translator;
    private final TranslationService translationService;
    private boolean bootstrap = false;

    public KibuPlugins() {
        translationLoader = new KibuTranslationLoader();
        languagePreferenceProvider = new KibuLanguagePreferenceProvider();
        translator = new DefaultLanguageTranslator(translationLoader);
        translationService = new TranslationService(translator, languagePreferenceProvider);
    }

    @Override
    public void onInitialize() {
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

        PluginBootstrapEvents.BEGIN.register(pluginFrame -> bootstrap = true);

        PluginBootstrapEvents.COMPLETE.register(pluginFrame -> {
            bootstrap = false;

            if (translationLoader.isDirty()) {
                reloadTranslations();
            }
        });
    }

    private void unregisterTranslatedPlugin(TranslatedPlugin translatedPlugin) {
        translationLoader.unregister(translatedPlugin);

        if (!bootstrap) {
            reloadTranslations();
        }
    }

    private void initTranslatedPlugin(TranslatedPlugin translatedPlugin) {
        translationLoader.register(translatedPlugin);

        translatedPlugin.injectTranslationService(translationService);

        if (!bootstrap) {
            reloadTranslations();
        }
    }

    private void reloadTranslations() {
        translator.reload().thenRun(() -> TranslationsLoadedCallback.HOOK.invoker().onLoaded(translationService));
    }
}
