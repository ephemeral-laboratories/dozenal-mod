package garden.ephemeral.minecraft.dozenal;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Wraps the string mangler to add caching.
 */
public class CachingStringMangler {
    private final Map<String, String> cache1 = new WeakHashMap<>();

    private final StringMangler delegate = new StringMangler();

    public String mangle(String input) {
        return cache1.computeIfAbsent(input, delegate::mangle);
    }
}
