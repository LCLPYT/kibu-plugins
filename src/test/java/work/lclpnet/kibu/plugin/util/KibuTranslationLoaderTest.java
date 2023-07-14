package work.lclpnet.kibu.plugin.util;

import org.junit.jupiter.api.Test;
import work.lclpnet.translations.model.LanguageCollection;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class KibuTranslationLoaderTest {

    @Test
    void isDirty() {
        var loader = new KibuTranslationLoader();
        var plugin = new TestTranslatedPlugin();

        assertFalse(loader.isDirty());

        loader.register(plugin);

        assertTrue(loader.isDirty());
    }

    @Test
    void load() {
        var loader = new KibuTranslationLoader();
        var plugin = new TestTranslatedPlugin();

        LanguageCollection dictionary = loader.load().join();
        assertFalse(dictionary.keys().iterator().hasNext());

        loader.register(plugin);
        dictionary = loader.load().join();

        assertEquals(StreamSupport.stream(dictionary.keys().spliterator(), false).collect(Collectors.toSet()),
                Set.of("en_us", "de_de"));
    }
}