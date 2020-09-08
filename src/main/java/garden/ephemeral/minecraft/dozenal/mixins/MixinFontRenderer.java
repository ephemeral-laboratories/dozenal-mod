package garden.ephemeral.minecraft.dozenal.mixins;

import garden.ephemeral.minecraft.dozenal.CachingStringMangler;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {
    private final CachingStringMangler mangler = new CachingStringMangler();

    @ModifyVariable(
            method = {
                    // renderString now delegates to this currently unmapped method
                    "func_238411_a_(Ljava/lang/String;FFIZLnet/minecraft/util/math/vector/Matrix4f;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ZIIZ)I",
                    "getStringWidth(Ljava/lang/String;)I"
            },
            at = @At("HEAD"),
            ordinal = 0)
    public String mangleRenderedStrings(String input) {
        return mangler.mangle(input);
    }
}
