package garden.ephemeral.minecraft.dozenal;

import com.google.common.collect.ImmutableList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringScanner {
    private final Pattern target = Pattern.compile("(?<![._\\-\\d])" +
            "-?" +
            "\\d+" +
            "(" +
            "(?<fraction>\\.\\d+)?" +
            "(?<tail>[kKMG%])?" +
            "(?![._\\-\\d])" +
            "|" +
            "(?![_\\-\\d])" +
            ")",
            Pattern.COMMENTS);

    public ImmutableList<Token> scan(String input) {
        ImmutableList.Builder<Token> builder = ImmutableList.builder();

        Matcher matcher = target.matcher(input);
        int lastEndPosition = 0;
        while (matcher.find(lastEndPosition)) {
            if (matcher.start() > lastEndPosition) {
                builder.add(new Token(TokenType.TEXT, input.substring(lastEndPosition, matcher.start())));
            }
            String tokenText = matcher.group();
            builder.add(new Token(categoriseToken(matcher), tokenText));
            lastEndPosition = matcher.end();
        }
        if (lastEndPosition < input.length()) {
            builder.add(new Token(TokenType.TEXT, input.substring(lastEndPosition)));
        }

        return builder.build();
    }

    private TokenType categoriseToken(Matcher matcher) {
        String fraction = matcher.group("fraction");
        String tail = matcher.group("tail");
        if (tail == null) {
            if (fraction == null) {
                return TokenType.INTEGER;
            } else {
                return TokenType.NUMBER;
            }
        } else {
            if ("%".equals(tail)) {
                return TokenType.PERCENTAGE;
            } else {
                return TokenType.SCIENTIFIC;
            }
        }
    }
}
