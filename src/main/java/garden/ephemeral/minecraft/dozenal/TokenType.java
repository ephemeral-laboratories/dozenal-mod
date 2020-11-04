package garden.ephemeral.minecraft.dozenal;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.text.ParseException;

enum TokenType {
    TEXT {
        @Override
        @Nonnull
        String mangle(@Nonnull String text) {
            return text;
        }
    },

    NUMBER {
        @Override
        @Nonnull
        String mangle(@Nonnull String text) {
            double value;
            try {
                value = FormatCache.getDecimalNumberFormat().parse(text).doubleValue();
            } catch (ParseException ignored) {
                // Fine.
                return text;
            }
            int precision = text.length() - text.indexOf(".") - 1;
            NumberFormat format = FormatCache.getNumberFormat();
            format.setMinimumFractionDigits(precision);
            format.setMaximumFractionDigits(precision);
            format.setGroupingUsed(text.contains(","));
            return format.format(value);
        }
    },

    INTEGER {
        @Override
        @Nonnull
        String mangle(@Nonnull String text) {
            long value;
            try {
                value = FormatCache.getDecimalIntegerFormat().parse(text).longValue();
            } catch (ParseException ignored) {
                // Fine.
                return text;
            }
            NumberFormat format = FormatCache.getIntegerFormat();
            format.setGroupingUsed(text.contains(","));
            format.setMinimumIntegerDigits(text.startsWith("0") ? text.length() : 0);
            return format.format(value);
        }
    },

    PERCENTAGE {
        @Override
        @Nonnull
        String mangle(@Nonnull String text) {
            // Chopping off the % sign and then re-dividing to get back the actual ratio.
            text = text.substring(0, text.length() - 1);
            double value;
            try {
                value = FormatCache.getDecimalNumberFormat().parse(text).doubleValue();
            } catch (ParseException ignored) {
                // Fine.
                return text;
            }
            value /= 100.0;
            int fractionalSeparator = text.indexOf(".");
            int precision = fractionalSeparator < 0 ? 0 :
                    text.length() - fractionalSeparator - 1;
            NumberFormat format = FormatCache.getPercentFormat();
            format.setMaximumFractionDigits(precision);
            return format.format(value);
        }
    },

    SCIENTIFIC {
        @Override
        @Nonnull
        String mangle(@Nonnull String text) {
            // Chop units off the end
            String unit = text.substring(text.length() - 1);
            text = text.substring(0, text.length() - 1);
            double value;
            try {
                value = FormatCache.getDecimalNumberFormat().parse(text).doubleValue();
            } catch (ParseException ignored) {
                // Fine.
                return text;
            }
            value *= SI_MULTIPLIERS.get(unit);
            int fractionalSeparator = text.indexOf(".");
            // Conventionally many mods chop the .0 off the end of scientific
            // numbers which is annoying but we'll deal with it here.
            // If it looks like 23.1k or 23k then desired precision is 2.
            int precision = fractionalSeparator < 0 ?
                    (text.length() > 2 ? text.length() - 1 : text.length()) :
                    text.length() - 2;
            NumberFormat format = FormatCache.getNumberFormat();
            format.setMinimumFractionDigits(precision);
            format.setMaximumFractionDigits(precision);
            int exponent = value == 0 ? 0 : (int) Math.floor(Math.log(value) / LOG_12);
            String exponentSymbol = exponentSymbolFor(exponent);
            value /= Math.pow(12, exponent);
            return format.format(value) + exponentSymbol;
        }

        /**
         * Generates an SDN abbreviation for the given exponent.
         *
         * @param exponent the exponent.
         * @return the SDN abbreviation thereof.
         */
        @Nonnull
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
    };

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

    @Nonnull
    abstract String mangle(@Nonnull String text);

}
