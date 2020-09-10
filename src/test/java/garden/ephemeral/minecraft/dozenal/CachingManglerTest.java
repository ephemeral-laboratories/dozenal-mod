package garden.ephemeral.minecraft.dozenal;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CachingManglerTest {

    @Test
    public void testMangle() {
        CachingMangler mangler = new CachingMangler();
        assertThat(mangler.mangle("test 23"), is("test 1↋"));
    }
}
