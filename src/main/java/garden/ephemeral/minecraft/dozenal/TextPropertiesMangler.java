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

        // getComponent
        @Nonnull
        @Override
        public <T> Optional<T> func_230438_a_(@Nonnull ITextAcceptor<T> acceptor) {
            return input.func_230438_a_(originalString -> {
                String mangledString = stringMangler.mangle(originalString);
                return acceptor.accept(mangledString);
            });
        }

        // getComponentWithStyle
        @Nonnull
        @Override
        public <T> Optional<T> func_230439_a_(@Nonnull IStyledTextAcceptor<T> acceptor, @Nonnull Style baseStyle) {
            return input.func_230439_a_((mergedStyle, originalString) -> {
                String mangledString = stringMangler.mangle(originalString);
                return acceptor.accept(mergedStyle, mangledString);
            }, baseStyle);
        }
    }
}
