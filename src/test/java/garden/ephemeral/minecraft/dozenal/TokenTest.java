package garden.ephemeral.minecraft.dozenal;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TokenTest {

    @Test
    public void testToString() {
        Token token = new Token(TokenType.TEXT, "test");
        assertThat(token, hasToString("Token<TEXT,test>"));
    }
}
