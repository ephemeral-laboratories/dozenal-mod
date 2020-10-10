package garden.ephemeral.minecraft.dozenal.mixins;

import garden.ephemeral.minecraft.dozenal.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {
    private final Mangler<String> stringMangler = new StringMangler();
    private final Mangler<ITextProperties> textPropertiesMangler =
            new TextPropertiesMangler(stringMangler);
    private final Mangler<IReorderingProcessor> reorderingProcessorMangler =
            new ReorderingProcessorMangler(stringMangler);

    @ModifyVariable(
            method = {
                    // renderString now delegates to this currently unmapped method
                    "func_238411_a_(Ljava/lang/String;FFIZLnet/minecraft/util/math/vector/Matrix4f;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ZIIZ)I",
                    "getStringWidth(Ljava/lang/String;)I"
            },
            at = @At("HEAD"),
            ordinal = 0)
    public @Nullable String mangleString(@Nullable String input) {
        // Wild West, unsure of nullability of the caller.
        if (input == null) {
            return null;
        }
        return stringMangler.mangle(input);
    }

    @ModifyVariable(
            method = {
                    // getStringWidth(ITextProperties)
                    "func_238414_a_(Lnet/minecraft/util/text/ITextProperties;)I",
            },
            at = @At("HEAD"),
            ordinal = 0)
    public @Nullable ITextProperties mangleTextProperties(@Nullable ITextProperties input) {
        // Wild West, unsure of nullability of the caller.
        if (input == null) {
            return null;
        }
        return textPropertiesMangler.mangle(input);
    }

    @ModifyVariable(
            method = {
                    // renderString(IReorderingProcessor, ...)
                    // renderString(ITextProperties, ...) delegates to this
                    "func_238416_a_(Lnet/minecraft/util/IReorderingProcessor;FFIZLnet/minecraft/util/math/vector/Matrix4f;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ZII)I",
                    // getStringWidth(IReorderingProcessor)
                    "func_243245_a(Lnet/minecraft/util/IReorderingProcessor;)I"
            },
            at = @At("HEAD"),
            ordinal = 0)
    public @Nullable IReorderingProcessor mangleReorderingProcessor(@Nullable IReorderingProcessor input) {
        // Wild West, unsure of nullability of the caller.
        if (input == null) {
            return null;
        }
        return reorderingProcessorMangler.mangle(input);
    }
}
