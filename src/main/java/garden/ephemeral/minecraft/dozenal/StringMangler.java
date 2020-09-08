package garden.ephemeral.minecraft.dozenal;

import com.google.common.collect.ImmutableList;
import garden.ephemeral.dozenal.DozenalNumberFormatProvider;

import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Mangles numeric values in strings into base 12.
 */
public class StringMangler {
    private final StringScanner scanner = new StringScanner();
    private final NumberFormat integerFormat;
    private final NumberFormat numberFormat;
    private final NumberFormat percentFormat;

    public StringMangler() {
        // Reference the number format provider directly - avoids needing to set locale
        // for the process and also removes the need to install the locale provider.
        NumberFormatProvider provider = new DozenalNumberFormatProvider();
        Locale locale = new Locale("en", "US", "DOZ");
        integerFormat = provider.getIntegerInstance(locale);
        numberFormat = provider.getNumberInstance(locale);
        percentFormat = provider.getPercentInstance(locale);
    }

    public String mangle(String input) {
        ImmutableList<Token> tokens = scanner.scan(input);
        return tokens.stream()
                .map(this::mangleToken)
                .collect(Collectors.joining());
    }

    private String mangleToken(Token token) {
        String text = token.getText();
        switch (token.getType()) {
            case INTEGER: {
                long value = Long.parseLong(text);
                integerFormat.setGroupingUsed(text.contains(","));
                return integerFormat.format(value);
            }

            case NUMBER: {
                double value = Double.parseDouble(text);
                int precision = text.length() - text.indexOf(".") - 1;
                numberFormat.setMinimumFractionDigits(precision);
                numberFormat.setMaximumFractionDigits(precision);
                numberFormat.setGroupingUsed(text.contains(","));
                return numberFormat.format(value);
            }

            case PERCENTAGE: {
                // Chopping off the % sign and then re-dividing to get back the actual ratio.
                text = text.substring(0, text.length() - 1);
                double value = Double.parseDouble(text) / 100.0;
                int fractionalSeparator = text.indexOf(".");
                int precision = fractionalSeparator < 0 ? 0 :
                        text.length() - fractionalSeparator - 1;
                percentFormat.setMaximumFractionDigits(precision);
                return percentFormat.format(value);
            }

            case TEXT:
            case SEPARATOR:
                return text;

            default:
                throw new IllegalStateException("Missing case: " + token.getType());
        }
    }
}
