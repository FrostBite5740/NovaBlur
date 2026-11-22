package dev.novablur.mixin;

import dev.novablur.gui.BlurScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void novablur$button(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Nova Blur"),
                b -> this.client.setScreen(new BlurScreen())).dimensions(6, 6, 80, 20).build());
    }
}
