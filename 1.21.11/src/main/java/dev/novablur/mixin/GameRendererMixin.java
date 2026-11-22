package dev.novablur.mixin;

import dev.novablur.BlurState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private Pool pool;

    @Inject(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V",
                    shift = At.Shift.AFTER))
    private void novablur$blur(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        int level = BlurState.levelForFrame(this.client);
        if (level <= 0) return;
        PostEffectProcessor chain = this.client.getShaderLoader().loadPostEffect(
                Identifier.of("novablur", "blur_" + level), DefaultFramebufferSet.MAIN_ONLY);
        if (chain != null) chain.render(this.client.getFramebuffer(), this.pool);
    }
}
