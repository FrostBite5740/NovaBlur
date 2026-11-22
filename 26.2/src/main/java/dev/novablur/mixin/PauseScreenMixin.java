package dev.novablur.mixin;

import dev.novablur.gui.BlurScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void novablur$button(CallbackInfo ci) {
        this.addRenderableWidget(Button.builder(Component.literal("Nova Blur"),
                b -> this.minecraft.gui.setScreen(new BlurScreen())).bounds(6, 6, 80, 20).build());
    }
}
