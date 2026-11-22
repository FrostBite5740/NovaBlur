package dev.novablur.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL13;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudStateResetMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void novablur$resetGlState(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        RenderSystem.enableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1f, 1f, 1f, 1f);
    }
}
