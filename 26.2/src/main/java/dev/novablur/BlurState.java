package dev.novablur;

import net.minecraft.client.Minecraft;

/** Decides which blur level (0 = none, 1..4) to draw this frame. */
public final class BlurState {
    private BlurState() {}

    public static int levelForFrame(Minecraft mc) {
        if (NovaBlur.mode == 0 || mc.player == null) return 0;
        return NovaBlur.strength;
    }
}
