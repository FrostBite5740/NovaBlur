package dev.novablur.mixin;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.novablur.BlurState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private RenderTarget mainRenderTarget;
    @Shadow @Final private CrossFrameResourcePool resourcePool;

    @Inject(method = "applyPostEffects", at = @At("RETURN"))
    private void novablur$blur(CallbackInfo ci) {
        int level = BlurState.levelForFrame(this.minecraft);
        if (level <= 0) return;
        PostChain chain = this.minecraft.getShaderManager().getPostChain(
                Identifier.fromNamespaceAndPath("novablur", "blur_" + level), LevelTargetBundle.MAIN_TARGETS);
        if (chain != null) chain.process(this.mainRenderTarget, this.resourcePool);
    }
}
