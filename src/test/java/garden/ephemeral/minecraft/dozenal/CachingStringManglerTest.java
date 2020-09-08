package garden.ephemeral.minecraft.dozenal;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CachingStringManglerTest {

    @Test
    public void testMangle() {
        CachingStringMangler mangler = new CachingStringMangler();
        assertThat(mangler.mangle("test 23"), is("test 1↋"));
    }
}
