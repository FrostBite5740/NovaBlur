package dev.novablur.gui;

import dev.novablur.NovaBlur;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class BlurScreen extends Screen {
    public BlurScreen() {
        super(new LiteralText("Nova Blur"));
    }

    private static String modeName() {
        return NovaBlur.mode == 0 ? "OFF" : "ON";
    }

    @Override
    protected void init() {
        int cx = width / 2, y = height / 2 - 40;
        addButton(new ButtonWidget(cx - 75, y, 150, 20, new LiteralText("Blur: " + modeName()), b -> {
            NovaBlur.mode = NovaBlur.mode == 0 ? 1 : 0;
            NovaBlur.save();
            client.openScreen(new BlurScreen());
        }));
        addButton(new ButtonWidget(cx - 75, y + 28, 20, 20, new LiteralText("-"), b -> {
            NovaBlur.strength = Math.max(1, NovaBlur.strength - 1);
            NovaBlur.save();
            client.openScreen(new BlurScreen());
        }));
        addButton(new ButtonWidget(cx - 52, y + 28, 104, 20, new LiteralText("Strength: " + NovaBlur.strength + " / 4"), b -> {}));
        addButton(new ButtonWidget(cx + 55, y + 28, 20, 20, new LiteralText("+"), b -> {
            NovaBlur.strength = Math.min(4, NovaBlur.strength + 1);
            NovaBlur.save();
            client.openScreen(new BlurScreen());
        }));
        addButton(new ButtonWidget(cx - 75, y + 60, 150, 20, new LiteralText("Done"), b -> onClose()));
    }

    @Override
    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        drawCenteredText(m, textRenderer, "Nova Blur", width / 2, height / 2 - 70, 0xFFFFFF);
        super.render(m, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
