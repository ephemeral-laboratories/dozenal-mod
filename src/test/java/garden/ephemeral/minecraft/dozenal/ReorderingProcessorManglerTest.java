package garden.ephemeral.minecraft.dozenal;

import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class ReorderingProcessorManglerTest {

    @Test
    public void testMangle() {
        TextComponent input = new StringTextComponent("");
        input.append(new StringTextComponent("Line 1: "));
        input.append(new StringTextComponent("1.6 Damage").modifyStyle(s -> s.setBold(true)));

        ReorderingProcessorMangler mangler = new ReorderingProcessorMangler(new StringMangler());
        IReorderingProcessor output = mangler.mangle(input.func_241878_f());

        List<Style> stylesSeen = new ArrayList<>();
        List<Integer> codePointsSeen = new ArrayList<>();

        output.accept((index, style, codePoint) -> {
            stylesSeen.add(style);
            codePointsSeen.add(codePoint);
            return true;
        });

        Style style1 = Style.EMPTY;
        Style style2 = Style.EMPTY.setBold(true);

        assertThat(stylesSeen, contains(
                style1, style1, style1, style1, style1, style1, style1, style1,
                style2, style2, style2, style2, style2, style2, style2, style2, style2, style2));
        assertThat(codePointsSeen, contains(
                (int) 'L', (int) 'i', (int) 'n', (int) 'e', (int) ' ', (int) '1', (int) ':', (int) ' ',
                (int) '1', (int) ';', (int) '7', (int) ' ',
                (int) 'D', (int) 'a', (int) 'm', (int) 'a', (int) 'g', (int) 'e'));
    }
}
