package garden.ephemeral.minecraft.dozenal;

import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StringScanner {
    private final Pattern target = Pattern.compile("(?<![._\\-\\d])" +
            "-?" +
            "(?<integerPart>\\d+(?:,\\d+)*)" +
            "(" +
            "(?<fractionalPart>\\.\\d+)?" +
            "(?<suffix>[kKMG%])?" +
            "(?![._\\-\\d])" +
            "|" +
            "(?![_\\-\\d])" +
            ")",
            Pattern.COMMENTS);

    void scan(String input, BiConsumer<String, TokenType> consumer) {
        Matcher matcher = target.matcher(input);
        int lastEndPosition = 0;
        while (matcher.find(lastEndPosition)) {
            if (matcher.start() > lastEndPosition) {
                consumer.accept(input.substring(lastEndPosition, matcher.start()), TokenType.TEXT);
            }
            consumer.accept(matcher.group(), categoriseToken(matcher));
            lastEndPosition = matcher.end();
        }
        if (lastEndPosition < input.length()) {
            consumer.accept(input.substring(lastEndPosition), TokenType.TEXT);
        }
    }

    private TokenType categoriseToken(Matcher matcher) {
        String fraction = matcher.group("fractionalPart");
        String suffix = matcher.group("suffix");
        if (suffix == null) {
            if (fraction == null) {
                return TokenType.INTEGER;
            } else {
                return TokenType.NUMBER;
            }
        } else {
            if ("%".equals(suffix)) {
                return TokenType.PERCENTAGE;
            } else {
                return TokenType.SCIENTIFIC;
            }
        }
    }
}
