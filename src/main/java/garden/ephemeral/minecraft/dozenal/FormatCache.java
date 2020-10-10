package garden.ephemeral.minecraft.dozenal;

import garden.ephemeral.dozenal.DozenalNumberFormatProvider;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;

class FormatCache {
    @Nonnull
    private static final ThreadLocal<NumberFormat> integerFormat;
    @Nonnull
    private static final ThreadLocal<NumberFormat> numberFormat;
    @Nonnull
    private static final ThreadLocal<NumberFormat> percentFormat;
    @Nonnull
    private static final ThreadLocal<NumberFormat> decimalNumberFormat;
    @Nonnull
    private static final ThreadLocal<NumberFormat> decimalIntegerFormat;

    static {
        // Reference the number format provider directly - avoids needing to set locale
        // for the process and also removes the need to install the locale provider.
        NumberFormatProvider provider = new DozenalNumberFormatProvider();
        Locale locale = new Locale("en", "US", "DOZ");
        integerFormat = ThreadLocal.withInitial(() -> provider.getIntegerInstance(locale));
        numberFormat = ThreadLocal.withInitial(() -> provider.getNumberInstance(locale));
        percentFormat = ThreadLocal.withInitial(() -> provider.getPercentInstance(locale));
        decimalNumberFormat = ThreadLocal.withInitial(DecimalFormat::new);
        decimalIntegerFormat = ThreadLocal.withInitial(() -> {
            NumberFormat format = new DecimalFormat();
            format.setParseIntegerOnly(true);
            return format;
        });
    }

    @Nonnull
    static NumberFormat getIntegerFormat() {
        return integerFormat.get();
    }

    @Nonnull
    static NumberFormat getNumberFormat() {
        return numberFormat.get();
    }

    @Nonnull
    static NumberFormat getPercentFormat() {
        return percentFormat.get();
    }

    @Nonnull
    static NumberFormat getDecimalNumberFormat() {
        return decimalNumberFormat.get();
    }

    @Nonnull
    static NumberFormat getDecimalIntegerFormat() {
        return decimalIntegerFormat.get();
    }
}
