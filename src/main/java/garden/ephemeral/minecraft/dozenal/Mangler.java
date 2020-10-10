package garden.ephemeral.minecraft.dozenal;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class Mangler<T> {
    private final Map<T, T> cache = new WeakHashMap<>();

    @Nonnull
    public final T mangle(@Nonnull T input) {
        return cache.computeIfAbsent(input, this::doMangle);
    }

    @Nonnull
    protected abstract T doMangle(@Nonnull T input);
}
