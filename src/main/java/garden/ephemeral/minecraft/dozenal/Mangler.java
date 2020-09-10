package garden.ephemeral.minecraft.dozenal;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class Mangler<T> {
    private final Map<T, T> cache = new WeakHashMap<>();

    public final T mangle(T input) {
        return cache.computeIfAbsent(input, this::doMangle);
    }

    protected abstract T doMangle(T input);
}
