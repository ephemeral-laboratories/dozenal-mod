package garden.ephemeral.minecraft.dozenal;

import javax.annotation.Nonnull;

public class StringMangler extends Mangler<String> {
    private final StringScanner scanner = new StringScanner();

    @Nonnull
    @Override
    protected String doMangle(@Nonnull String input) {
        StringBuilder builder = new StringBuilder();
        scanner.scan(input, (text, type) -> {
            String mangled = type.mangle(text);
            builder.append(mangled);
        });
        return builder.toString();
    }
}
