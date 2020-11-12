package garden.ephemeral.minecraft.dozenal;

import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.Style;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TextPropertiesMangler extends Mangler<ITextProperties> {
    private final Mangler<String> stringMangler;

    public TextPropertiesMangler(Mangler<String> stringMangler) {
        this.stringMangler = stringMangler;
    }

    @Nonnull
    @Override
    public ITextProperties doMangle(@Nonnull ITextProperties input) {
        return new MangledTextProperties(input);
    }

    private class MangledTextProperties implements ITextProperties {
        private final ITextProperties input;

        private MangledTextProperties(ITextProperties input) {
            this.input = input;
        }

        @Nonnull
        @Override
        public <T> Optional<T> getComponent(@Nonnull ITextAcceptor<T> acceptor) {
            return input.getComponent(originalString -> {
                String mangledString = stringMangler.mangle(originalString);
                return acceptor.accept(mangledString);
            });
        }

        @Nonnull
        @Override
        public <T> Optional<T> getComponentWithStyle(@Nonnull IStyledTextAcceptor<T> acceptor, @Nonnull Style baseStyle) {
            return input.getComponentWithStyle((mergedStyle, originalString) -> {
                String mangledString = stringMangler.mangle(originalString);
                return acceptor.accept(mergedStyle, mangledString);
            }, baseStyle);
        }
    }
}
