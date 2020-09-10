package garden.ephemeral.minecraft.dozenal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import garden.ephemeral.dozenal.DozenalNumberFormatProvider;

import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Mangles numeric values in strings into base 12.
 */
public class StringMangler {
    private static final ImmutableMap<String, Integer> SI_MULTIPLIERS = ImmutableMap.of(
            "k", 1_000,
            "K", 1_000,
            "M", 1_000_000,
            "G", 1_000_000_000
    );
    private static final char[] SDN_ABBREVIATIONS = {
            'n', 'u', 'b', 't', 'q', 'p', 'h', 's', 'o', 'e', 'd', 'l'
    };

    private static final double LOG_12 = Math.log(12);

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

            case SCIENTIFIC: {
                // Chop units off the end
                String unit = text.substring(text.length() - 1);
                text = text.substring(0, text.length() - 1);
                double value = Double.parseDouble(text) * SI_MULTIPLIERS.get(unit);
                int fractionalSeparator = text.indexOf(".");
                // Conventionally many mods chop the .0 off the end of scientific
                // numbers which is annoying but we'll deal with it here.
                // If it looks like 23.1k or 23k then desired precision is 2.
                int precision = fractionalSeparator < 0 ?
                        text.length() :
                        text.length() - 2;
                numberFormat.setMinimumFractionDigits(precision);
                numberFormat.setMaximumFractionDigits(precision);
                int exponent = value == 0 ? 0 : (int) Math.floor(Math.log(value) / LOG_12);
                String exponentSymbol = exponentSymbolFor(exponent);
                value /= Math.pow(12, exponent);
                return numberFormat.format(value) + exponentSymbol;
            }

            case TEXT:
            case SEPARATOR:
                return text;

            default:
                throw new IllegalStateException("Missing case: " + token.getType());
        }
    }

    /**
     * Generates an SDN abbreviation for the given exponent.
     *
     * @param exponent the exponent.
     * @return the SDN abbreviation thereof.
     */
    private String exponentSymbolFor(int exponent) {
        StringBuilder builder = new StringBuilder();
        while (exponent > 0) {
            int digit = exponent % 12;
            builder.append(SDN_ABBREVIATIONS[digit]);
            exponent /= 12;
        }
        if (builder.length() == 0) {
            builder.append(SDN_ABBREVIATIONS[0]);
        } else {
            builder.reverse();
        }
        return builder.toString();
    }
}
