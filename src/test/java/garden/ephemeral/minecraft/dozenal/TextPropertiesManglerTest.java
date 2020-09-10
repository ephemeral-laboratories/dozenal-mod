package garden.ephemeral.minecraft.dozenal;

import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.Style;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class TextPropertiesManglerTest {

    @Test
    public void testMangle() {
        ITextProperties input = ITextProperties.func_240655_a_(
                ITextProperties.func_240652_a_("Line 1"),
                ITextProperties.func_240653_a_("1.6 Damage", Style.EMPTY.setBold(true)));

        TextPropertiesMangler mangler = new TextPropertiesMangler(new StringMangler());
        ITextProperties output = mangler.mangle(input);

        {
            List<String> textSeen = new ArrayList<>();

            output.func_230438_a_(text -> {
                textSeen.add(text);
                return Optional.empty();
            });

            assertThat(textSeen, contains(
                    "Line 1",
                    "1;7 Damage"));
        }

        {
            List<Style> stylesSeen = new ArrayList<>();
            List<String> textSeen = new ArrayList<>();

            output.func_230439_a_((ITextProperties.IStyledTextAcceptor<Void>) (style, text) -> {
                stylesSeen.add(style);
                textSeen.add(text);
                return Optional.empty();
            }, Style.EMPTY.setItalic(true));

            assertThat(stylesSeen, contains(
                    Style.EMPTY.setItalic(true),
                    Style.EMPTY.setItalic(true).setBold(true)));
            assertThat(textSeen, contains(
                    "Line 1",
                    "1;7 Damage"));
        }
    }
}
