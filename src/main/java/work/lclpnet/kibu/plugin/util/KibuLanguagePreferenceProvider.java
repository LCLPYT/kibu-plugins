package work.lclpnet.kibu.plugin.util;

import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.access.PlayerLanguage;
import work.lclpnet.kibu.translate.pref.LanguagePreferenceProvider;

import java.util.Optional;

public class KibuLanguagePreferenceProvider implements LanguagePreferenceProvider {

    protected LanguagePreferenceProvider[] children = new LanguagePreferenceProvider[0];

    @Override
    public Optional<String> getLanguagePreference(ServerPlayerEntity player) {
        synchronized (this) {
            for (LanguagePreferenceProvider provider : children) {
                Optional<String> preference = provider.getLanguagePreference(player);

                if (preference.isPresent()) {
                    return preference;
                }
            }
        }

        return Optional.of(PlayerLanguage.getLanguage(player));
    }

    public void addChild(LanguagePreferenceProvider provider) {
        synchronized (this) {
            final int len = this.children.length;

            LanguagePreferenceProvider[] children = new LanguagePreferenceProvider[len + 1];
            System.arraycopy(this.children, 0, children, 0, len);
            children[len] = provider;

            this.children = children;
        }
    }

    public void removeChild(LanguagePreferenceProvider provider) {
        synchronized (this) {
            final int len = this.children.length;

            int idx = -1;

            for (int i = 0; i < len; i++) {
                if (this.children[i] == provider) {
                    idx = i;
                    break;
                }
            }

            if (idx == -1) return;

            LanguagePreferenceProvider[] children = new LanguagePreferenceProvider[len - 1];
            System.arraycopy(this.children, 0, children, 0, idx);
            System.arraycopy(this.children, idx + 1, children, idx, len - idx - 1);

            this.children = children;
        }
    }
}
