package dev.novablur.gui;

import dev.novablur.NovaBlur;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class BlurScreen extends Screen {
    public BlurScreen() {
        super(Text.literal("Nova Blur"));
    }

    private static String modeName() {
        return NovaBlur.mode == 0 ? "OFF" : "ON";
    }

    @Override
    protected void init() {
        int cx = width / 2, y = height / 2 - 40;
        addDrawableChild(ButtonWidget.builder(Text.literal("Blur: " + modeName()), b -> {
            NovaBlur.mode = NovaBlur.mode == 0 ? 1 : 0;
            NovaBlur.save();
            clearAndInit();
        }).dimensions(cx - 75, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("-"), b -> {
            NovaBlur.strength = Math.max(1, NovaBlur.strength - 1);
            NovaBlur.save();
            clearAndInit();
        }).dimensions(cx - 75, y + 28, 20, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Strength: " + NovaBlur.strength + " / 4"), b -> {})
                .dimensions(cx - 52, y + 28, 104, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> {
            NovaBlur.strength = Math.min(4, NovaBlur.strength + 1);
            NovaBlur.save();
            clearAndInit();
        }).dimensions(cx + 55, y + 28, 20, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(cx - 75, y + 60, 150, 20).build());
    }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float delta) {
        super.render(g, mouseX, mouseY, delta);
        g.drawCenteredTextWithShadow(this.textRenderer, "Nova Blur", width / 2, height / 2 - 70, 0xFFFFFFFF);
    }

    @Override
    public boolean shouldPause() { return false; }
}
