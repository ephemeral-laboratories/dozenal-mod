package garden.ephemeral.minecraft.dozenal;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
public class Token {
    @Nonnull
    private final TokenType type;

    @Nonnull
    private final String text;

    public Token(@Nonnull TokenType type, @Nonnull String text) {
        this.type = type;
        this.text = text;
    }

    @Nonnull
    public TokenType getType() {
        return type;
    }

    @Nonnull
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Token<" + type + "," + text + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Token)) {
            return false;
        }
        Token other = (Token) obj;
        return type.equals(other.type) &&
                text.equals(other.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, text);
    }
}
