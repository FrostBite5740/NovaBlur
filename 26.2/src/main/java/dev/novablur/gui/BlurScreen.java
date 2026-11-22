package dev.novablur.gui;

import dev.novablur.NovaBlur;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BlurScreen extends Screen {
    public BlurScreen() {
        super(Component.literal("Nova Blur"));
    }

    private static String modeName() {
        return NovaBlur.mode == 0 ? "OFF" : "ON";
    }

    @Override
    protected void init() {
        int cx = width / 2, y = height / 2 - 40;
        addRenderableWidget(Button.builder(Component.literal("Blur: " + modeName()), b -> {
            NovaBlur.mode = NovaBlur.mode == 0 ? 1 : 0;
            NovaBlur.save();
            rebuildWidgets();
        }).bounds(cx - 75, y, 150, 20).build());

        addRenderableWidget(Button.builder(Component.literal("-"), b -> {
            NovaBlur.strength = Math.max(1, NovaBlur.strength - 1);
            NovaBlur.save();
            rebuildWidgets();
        }).bounds(cx - 75, y + 28, 20, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Strength: " + NovaBlur.strength + " / 4"), b -> {})
                .bounds(cx - 52, y + 28, 104, 20).build());
        addRenderableWidget(Button.builder(Component.literal("+"), b -> {
            NovaBlur.strength = Math.min(4, NovaBlur.strength + 1);
            NovaBlur.save();
            rebuildWidgets();
        }).bounds(cx + 55, y + 28, 20, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(cx - 75, y + 60, 150, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        String t = "Nova Blur";
        g.text(this.font, t, (width - this.font.width(t)) / 2, height / 2 - 70, 0xFFFFFFFF, true);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
