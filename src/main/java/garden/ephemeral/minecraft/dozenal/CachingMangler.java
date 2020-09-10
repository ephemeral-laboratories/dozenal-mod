package garden.ephemeral.minecraft.dozenal;

import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextProperties;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Wraps the string mangler to add caching.
 */
public class CachingMangler {
    private final Map<String, String> stringCache = new WeakHashMap<>();
    private final Map<ITextProperties, ITextProperties> textPropertiesCache = new WeakHashMap<>();
    private final Map<IReorderingProcessor, IReorderingProcessor> reorderingProcessorCache = new WeakHashMap<>();

    private final StringMangler stringMangler = new StringMangler();
    private final TextPropertiesMangler textPropertiesMangler = new TextPropertiesMangler(this);
    private final ReorderingProcessorMangler reorderingProcessorMangler = new ReorderingProcessorMangler(this);

    public String mangle(String input) {
        return stringCache.computeIfAbsent(input, stringMangler::mangle);
    }

    public ITextProperties mangle(ITextProperties input) {
        return textPropertiesCache.computeIfAbsent(input, textPropertiesMangler::mangle);
    }

    public IReorderingProcessor mangle(IReorderingProcessor input) {
        return reorderingProcessorCache.computeIfAbsent(input, reorderingProcessorMangler::mangle);
    }
}
