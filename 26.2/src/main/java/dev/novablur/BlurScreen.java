package dev.novablur;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BlurScreen extends Screen {
    public BlurScreen() {
        super(Component.literal("Nova Blur"));
    }

    private static String modeName() {
        return NovaBlur.mode == 0 ? "OFF" : NovaBlur.mode == 1 ? "Constant blur" : "Motion blur";
    }

    @Override
    protected void init() {
        int cx = width / 2;
        addRenderableWidget(Button.builder(Component.literal("Mode: " + modeName()), b -> {
            NovaBlur.mode = (NovaBlur.mode + 1) % 3;
            NovaBlur.save();
            rebuildWidgets();
        }).bounds(cx - 80, height / 2 - 30, 160, 20).build());

        addRenderableWidget(Button.builder(Component.literal("-"), b -> {
            NovaBlur.strength = Math.max(1, NovaBlur.strength - 1);
            NovaBlur.save();
            rebuildWidgets();
        }).bounds(cx - 80, height / 2, 30, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Strength: " + NovaBlur.strength + " / 4"), b -> {})
                .bounds(cx - 48, height / 2, 96, 20).build());

        addRenderableWidget(Button.builder(Component.literal("+"), b -> {
            NovaBlur.strength = Math.min(4, NovaBlur.strength + 1);
            NovaBlur.save();
            rebuildWidgets();
        }).bounds(cx + 50, height / 2, 30, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(cx - 80, height / 2 + 34, 160, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        String t = "Nova Blur";
        g.text(this.font, t, (width - this.font.width(t)) / 2, height / 2 - 55, 0xFFFFFFFF, true);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
