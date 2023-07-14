package work.lclpnet.kibu.plugin.ext;

import work.lclpnet.kibu.translate.TranslationService;
import work.lclpnet.translations.loader.translation.TranslationLoader;

public interface TranslatedPlugin {

    void injectTranslationService(TranslationService translationService);

    TranslationLoader createTranslationLoader();
}
