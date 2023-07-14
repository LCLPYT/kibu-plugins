package work.lclpnet.kibu.plugin.util;

import work.lclpnet.kibu.plugin.ext.TranslatedPlugin;
import work.lclpnet.kibu.translate.TranslationService;
import work.lclpnet.translations.loader.translation.TranslationLoader;
import work.lclpnet.translations.model.StaticLanguage;
import work.lclpnet.translations.model.StaticLanguageCollection;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TestTranslatedPlugin implements TranslatedPlugin {

    @Override
    public void injectTranslationService(TranslationService translationService) {}

    @Override
    public TranslationLoader createTranslationLoader() {
        return () -> CompletableFuture.completedFuture(new StaticLanguageCollection(Map.of(
                "en_us", new StaticLanguage(Map.of("hello", "Hello")),
                "de_de", new StaticLanguage(Map.of("hello", "Hallo"))
        )));
    }
}
