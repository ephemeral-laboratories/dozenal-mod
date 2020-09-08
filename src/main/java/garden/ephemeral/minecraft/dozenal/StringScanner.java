package garden.ephemeral.minecraft.dozenal;

import com.google.common.collect.ImmutableList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringScanner {
    private final Pattern token = Pattern.compile("[\\s/,:()]+|[^\\s/,:()]+");
    private final Pattern separator = Pattern.compile("[\\s/,:()]+");
    private final Pattern integer = Pattern.compile("-?\\d+");
    private final Pattern number = Pattern.compile("-?\\d+\\.\\d+");
    private final Pattern percentage = Pattern.compile("-?\\d+(?:\\.\\d+)?%");

    public ImmutableList<Token> scan(String input) {
        ImmutableList.Builder<Token> builder = ImmutableList.builder();
        int index = 0;
        int length = input.length();

        while (index < length) {
            CharSequence remaining = input.subSequence(index, length);
            Matcher matcher = token.matcher(remaining);
            if (matcher.lookingAt()) {
                String token = matcher.group(0);
                TokenType type = categoriseToken(token);
                builder.add(new Token(type, token));
                index += token.length();
            }
        }

        return builder.build();
    }

    private TokenType categoriseToken(String token) {
        if (separator.matcher(token).matches()) {
            return TokenType.SEPARATOR;
        } else if (percentage.matcher(token).matches()) {
            return TokenType.PERCENTAGE;
        } else if (number.matcher(token).matches()) {
            return TokenType.NUMBER;
        } else if (integer.matcher(token).matches()) {
            return TokenType.INTEGER;
        } else {
            return TokenType.TEXT;
        }
    }
}
