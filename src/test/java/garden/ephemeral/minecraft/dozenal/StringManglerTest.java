package garden.ephemeral.minecraft.dozenal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class StringManglerTest {

    public static Stream<Arguments> trivialExamples() {
        return Stream.of(
                arguments("", ""),
                arguments("   ", "   "),
                arguments("23", "1↋"),
                arguments("23.0", "1↋;0"),
                arguments("23%", "29%"),
                arguments("23.0%", "29;1%"),
                arguments("2300", "13↋8"),
                arguments("2,300", "1,3↋8")
        );
    }

    public static Stream<Arguments> basicExamples() {
        return Stream.of(
                arguments("test this", "test this"),
                arguments("test 2.3", "test 2;4"),
                arguments("test 2.3%", "test 3;4%"),
                arguments("test 23", "test 1↋")
        );
    }

    public static Stream<Arguments> scientificExamples() {
        return Stream.of(
                arguments("0k", "0;0n"), // weird edge case but OK
                arguments("23k", "1;14q"),
                arguments("23K", "1;14q"),
                arguments("23.2k", "1;15q"),
                arguments("23M", "7;85h"),
                arguments("23MB", "7;85hB"),
                arguments("23.4M", "7;↊0h"),
                arguments("23G", "4;56e"),
                arguments("23.6G", "4;6↋e"),
                arguments("256MB", "7;19sB"),
                arguments("1024MB", "2;46↋oB")
        );
    }

    public static Stream<Arguments> realExamples() {
        return Stream.of(
                // Inventory tooltips
                arguments(" 0.8 Attack Speed", " 0;↊ Attack Speed"),
                arguments(" 7 Attack Damage", " 7 Attack Damage"),
                arguments("Durability: 48 / 59", "Durability: 40 / 4↋"),
                arguments("NBT: 1 tag(s)", "NBT: 1 tag(s)"),

                // F3 debug screen
                arguments("Minecraft 1.15.2 (MOD_DEV/forge)", "Minecraft 1.15.2 (MOD_DEV/forge)"),
                arguments("19 fps T: 120 vsync fancy-clouds B:2", "17 fps T: ↊0 vsync fancy-clouds B:2"),
                arguments("Integrated server @ 13 ms ticks, 2 tx, 748 rx", "Integrated server @ 11 ms ticks, 2 tx, 524 rx"),
                arguments("C: 309/10000 (s) D: 12, pC: 000, pU: 04, aB: 05", "C: 219/5954 (s) D: 10, pC: 000, pU: 04, aB: 05"),
                arguments("E: 33/102, B: 0", "E: 29/86, B: 0"),
                arguments("P: 217. T: 102", "P: 161. T: 86"),
                arguments("Client Chunk Cache: 841, 550", "Client Chunk Cache: 5↊1, 39↊"),
                arguments("ServerChunkCache: 2458", "ServerChunkCache: 150↊"),
                arguments("minecraft:overworld FC: 0", "minecraft:overworld FC: 0"),
                arguments("XYZ: -231.880 / 69.00000 / 168.975", "XYZ: -173;↊69 / 59;00000 / 120;↋85"),
                arguments("Block: -232 69 168", "Block: -174 59 120"),
                arguments("Chunk: 8 5 8 in -15 4 10", "Chunk: 8 5 8 in -13 4 ↊"),
                arguments("Facing: west (Towards negative X) (128.7 / 27.0)", "Facing: west (Towards negative X) (↊8;8 / 23;0)"),
                arguments("Client Light: 15 (15 sky, 0 block)", "Client Light: 13 (13 sky, 0 block)"),
                arguments("Server Light: (15 sky, 0 block)", "Server Light: (13 sky, 0 block)"),
                arguments("CH S: 68 M: 68", "CH S: 58 M: 58"),
                arguments("SH S: 68 O: 68 M: 68: ML: 62", "SH S: 58 O: 58 M: 58: ML: 52"),
                arguments("Local Difficulty: 1.50 // 0.00 (Day 0)", "Local Difficulty: 1;60 // 0;00 (Day 0)"),
                arguments("Looking at block: -245 61 158", "Looking at block: -185 51 112"),
                arguments("Looking at liquid: -244 62 159", "Looking at liquid: -184 52 113"),
                arguments("Sounds: 2/247 + 1/8", "Sounds: 2/187 + 1/8"),
                arguments("For help: press F3 + Q", "For help: press F3 + Q"),
                arguments("Java: 1.8.0_241 64bit", "Java: 1.8.0_241 54bit"),
                // This 2532/7268MB ends up being trouble. How to identify that the left
                // part should be treated as the same units?
                //arguments("Mem: 34% 2532/7268MB", "Mem: 41% 2532/7268MB"),
                arguments("Allocated: 49% 3612MB", "Allocated: 5↋% 8;498oB"),
                arguments("CPU: 8x Intel(R) Core(TM) i7-7700K CPU @ 4.20GHz", "CPU: 8x Intel(R) Core(TM) i7-7700K CPU @ 9;93oHz"),
                arguments("Display: 854x480 (NVIDIA Corporation)", "Display: 5↋2x340 (NVIDIA Corporation)"),
                // I think you just have to accept this sort of thing if marketing is going
                // to straight-up put a space between the prefix and the number.
                // Thanks NVIDIA.
                arguments("GeForce GTX 1080 Ti/PCIe/SSE2", "GeForce GTX 760 Ti/PCIe/SSE2"),
                arguments("4.6.0 NVIDIA 451.67", "4.6.0 NVIDIA 317;80")
        );
    }

    @ParameterizedTest
    @MethodSource({"trivialExamples", "basicExamples", "scientificExamples", "realExamples"})
    public void testMangling(String input, String expectedResult) {
        StringMangler mangler = new StringMangler();
        String result = mangler.mangle(input);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testMinimumIntegerDigitsReset() {
        StringMangler mangler = new StringMangler();
        assertThat(mangler.mangle("2"), is("2"));
        assertThat(mangler.mangle("02"), is("02"));
        assertThat(mangler.mangle("3"), is("3"));
    }
}
