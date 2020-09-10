package garden.ephemeral.minecraft.dozenal;

import java.util.stream.IntStream;

public class Benchmark {
    private static final int N = 10000;
    private static int mixer = 0;

    public static void main(String[] args) {

        StringMangler mangler = new StringMangler();
        time("Mangler", () -> {
            StringManglerTest.realExamples().forEach(arguments -> {
                String input = (String) arguments.get()[0];
                String result = mangler.mangle(input);
                mixer += result.hashCode();
            });
        });

        System.out.println("Ignore this: " + mixer);
    }

    private static void time(String heading, Runnable runnable) {
        System.out.println(heading + ":");
        for (int i = 0; i < 10; i++) {
            double averageTime = IntStream.range(0, N)
                    .mapToLong(ignored -> timeOnce(runnable))
                    .summaryStatistics().getAverage();
            System.out.println(averageTime + "ns");
        }
    }

    private static long timeOnce(Runnable runnable) {
        long t0 = System.nanoTime();
        runnable.run();
        long t1 = System.nanoTime();
        return t1 - t0;
    }
}
