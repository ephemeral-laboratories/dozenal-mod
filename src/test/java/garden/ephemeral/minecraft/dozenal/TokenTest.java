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

    @Test
    public void testEquality_Equal() {
        Token token1 = new Token(TokenType.TEXT, "test");
        Token token2 = new Token(TokenType.TEXT, "test");
        assertThat(token2, is(equalTo(token1)));
        assertThat(token2.hashCode(), is(equalTo(token1.hashCode())));
    }

    @Test
    public void testEquality_DifferentType() {
        Token token1 = new Token(TokenType.TEXT, "test");
        Token token2 = new Token(TokenType.NUMBER, "test");
        assertThat(token2, is(not(equalTo(token1))));
    }

    @Test
    public void testEquality_DifferentText() {
        Token token1 = new Token(TokenType.TEXT, "test");
        Token token2 = new Token(TokenType.TEXT, "this");
        assertThat(token2, is(not(equalTo(token1))));
    }
}
