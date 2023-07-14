package work.lclpnet.kibu.plugin.hook;

import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.kibu.translate.TranslationService;

public interface TranslationsLoadedCallback {

    Hook<TranslationsLoadedCallback> HOOK = HookFactory.createArrayBacked(TranslationsLoadedCallback.class, hooks -> (translationService) -> {
        for (var hook : hooks) {
            hook.onLoaded(translationService);
        }
    });

    void onLoaded(TranslationService translationService);
}
