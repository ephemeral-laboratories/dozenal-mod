package garden.ephemeral.minecraft.dozenal;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
class Token {
    @Nonnull
    private final TokenType type;

    @Nonnull
    private final String text;

    Token(@Nonnull TokenType type, @Nonnull String text) {
        this.type = type;
        this.text = text;
    }

    @Nonnull
    TokenType getType() {
        return type;
    }

    @Nonnull
    String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Token<" + type + "," + text + ">";
    }
}
