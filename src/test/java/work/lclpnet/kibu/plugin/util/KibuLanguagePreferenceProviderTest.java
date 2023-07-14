package work.lclpnet.kibu.plugin.util;

import org.junit.jupiter.api.Test;
import work.lclpnet.kibu.translate.pref.LanguagePreferenceProvider;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KibuLanguagePreferenceProviderTest {

    @Test
    void addChild() {
        var provider = new Mock();
        assertEquals(0, provider.children.length);

        provider.addChild(player -> Optional.of("de_de"));

        assertEquals(1, provider.children.length);

        provider.addChild(player -> Optional.empty());
        provider.addChild(player -> Optional.of("de_de"));
        provider.addChild(player -> Optional.of("en_us"));

        assertEquals(4, provider.children.length);
    }

    @Test
    void removeChild() {
        var provider = new Mock();
        LanguagePreferenceProvider child = player -> Optional.of("de_de");
        provider.addChild(child);

        assertEquals(1, provider.children.length);

        provider.removeChild(child);

        assertEquals(0, provider.children.length);

        LanguagePreferenceProvider a = player -> Optional.empty();
        LanguagePreferenceProvider b = player -> Optional.of("de_de");
        LanguagePreferenceProvider c = player -> Optional.of("en_us");

        provider.addChild(a);
        provider.addChild(child);
        provider.addChild(b);
        provider.addChild(c);

        assertEquals(4, provider.children.length);

        provider.removeChild(child);

        assertArrayEquals(new LanguagePreferenceProvider[] { a, b, c }, provider.children);
    }

    private static class Mock extends KibuLanguagePreferenceProvider {}
}