package garden.ephemeral.minecraft.dozenal;

public class StringMangler extends Mangler<String> {
    private final StringScanner scanner = new StringScanner();

    @Override
    protected String doMangle(String input) {
        StringBuilder builder = new StringBuilder();
        scanner.scan(input, (text, type) -> {
            String mangled = type.mangle(text);
            builder.append(mangled);
        });
        return builder.toString();
    }
}
