package dev.novablur;

import dev.novablur.mixin.GameRendererAccessor;
import dev.novablur.mixin.ShaderEffectAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

/** Loads and unloads the blur shaders. Never replaces a shader that belongs to the game (creeper view etc.). */
public final class BlurManager {
    public enum Kind {
        MOTION("shaders/post/novablur_motion_blur.json");

        final Identifier id;

        Kind(String path) { this.id = new Identifier(path); }
    }

    private static Kind current;
    private static ShaderEffect ours;
    private static Kind failed;

    private BlurManager() {}

    public static void request(MinecraftClient mc, Kind kind, String uniform, float value) {
        GameRenderer gr = mc.gameRenderer;
        if (gr == null) return;

        if (current != kind) {
            if (current != null) release(mc, current);
            ShaderEffect active = gr.getShader();
            if (active != null && active != ours) return;
            if (failed == kind) return;
            ((GameRendererAccessor) gr).novablur$loadShader(kind.id);
            ours = gr.getShader();
            if (ours == null) {
                failed = kind;
                current = null;
                return;
            }
            current = kind;
        } else if (gr.getShader() != ours) {
            current = null;
            ours = null;
            return;
        }

        for (PostProcessShader pass : ((ShaderEffectAccessor) ours).novablur$getPasses()) {
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
        if (gr != null && gr.getShader() == ours) gr.disableShader();
        current = null;
        ours = null;
    }
}
