package work.lclpnet.kibu.plugin.util;

import work.lclpnet.kibu.plugin.ext.TranslatedPlugin;
import work.lclpnet.translations.loader.translation.MultiSourceTranslationLoader;
import work.lclpnet.translations.loader.translation.TranslationLoader;
import work.lclpnet.translations.model.LanguageCollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class KibuTranslationLoader extends MultiSourceTranslationLoader {

    private final Map<TranslatedPlugin, TranslationLoader> children = new HashMap<>();
    private boolean dirty = false;

    @Override
    protected void collectFutures(List<CompletableFuture<? extends LanguageCollection>> futures) {
        synchronized (this) {
            for (TranslationLoader loader : children.values()) {
                futures.add(loader.load());
            }
        }
    }

    public void register(TranslatedPlugin plugin) {
        synchronized (this) {
            TranslationLoader loader = plugin.createTranslationLoader();

            if (loader != null) {
                children.put(plugin, loader);
                dirty = true;
            }
        }
    }

    public void unregister(TranslatedPlugin plugin) {
        synchronized (this) {
            children.remove(plugin);
            dirty = true;
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
