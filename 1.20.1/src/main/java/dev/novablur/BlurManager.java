package dev.novablur;

import dev.novablur.mixin.GameRendererAccessor;
import dev.novablur.mixin.PostEffectProcessorAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

/**
 * Loads and unloads the blur shader. Only one post-processing shader can be active in
 * this Minecraft version, so this keeps track of which one is ours and never replaces someone else's.
 */
public final class BlurManager {
    public enum Kind {
        MOTION("shaders/post/novablur_motion_blur.json");

        final Identifier id;

        Kind(String path) {
            this.id = new Identifier(path);
        }
    }

    private static Kind current;
    private static PostEffectProcessor ours;
    private static Kind failed; // a shader that failed to load is not retried every tick

    private BlurManager() {}

    /** Makes sure the given blur is active and sets its strength uniform. */
    public static void request(MinecraftClient mc, Kind kind, String uniform, float value) {
        GameRenderer gr = mc.gameRenderer;
        if (gr == null) return;

        if (current != kind) {
            PostEffectProcessor active = gr.getPostProcessor();
            if (active != null && active != ours) return; // e.g. spectating a creeper: leave it alone
            if (failed == kind) return;
            if (active != null) gr.disablePostProcessor();
            ((GameRendererAccessor) gr).novablur$loadPostProcessor(kind.id);
            ours = gr.getPostProcessor();
            if (ours == null) {
                failed = kind;
                current = null;
                return;
            }
            current = kind;
        } else if (gr.getPostProcessor() != ours) {
            // the game replaced our shader (respawn, camera change): load it again next tick
            current = null;
            ours = null;
            return;
        }

        for (PostEffectPass pass : ((PostEffectProcessorAccessor) ours).novablur$getPasses()) {
            GlUniform u = pass.getProgram().getUniformByName(uniform);
            if (u != null) u.set(value);
        }
    }

    public static void releaseAll(MinecraftClient mc) {
        if (current != null) release(mc, current);
    }

    private static void release(MinecraftClient mc, Kind kind) {
        if (current != kind) return;
        GameRenderer gr = mc.gameRenderer;
        if (gr != null && gr.getPostProcessor() == ours) gr.disablePostProcessor();
        current = null;
        ours = null;
    }
}
