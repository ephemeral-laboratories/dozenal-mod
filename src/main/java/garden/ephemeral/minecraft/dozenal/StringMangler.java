package garden.ephemeral.minecraft.dozenal;

import com.google.common.collect.ImmutableList;

import java.util.stream.Collectors;

/**
 * Mangles numeric values in strings into base 12.
 */
public class StringMangler extends Mangler<String> {
    private final StringScanner scanner = new StringScanner();

    @Override
    protected String doMangle(String input) {
        ImmutableList<Token> tokens = scanner.scan(input);
        return tokens.stream()
                .map(this::mangleToken)
                .collect(Collectors.joining());
    }

    private String mangleToken(Token token) {
        String text = token.getText();
        return token.getType().mangle(text);
    }
}
