package dev.novablur;

import com.google.gson.Gson;
import com.mojang.blaze3d.platform.InputConstants;
import dev.novablur.gui.BlurScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import java.nio.file.Files;
import java.nio.file.Path;

public class NovaBlur implements ClientModInitializer {
    public static final String ID = "novablur";
    /** mode 0 = off, 1 = on (blur grows while you turn the camera) */
    public static int mode = 0;
    /** strength 1..4 */
    public static int strength = 2;

    public static KeyMapping menuKey;

    private static class Data { int mode = 0; int strength = 2; }

    private static Path file() { return FabricLoader.getInstance().getConfigDir().resolve("novablur.json"); }

    private static void load() {
        try {
            Path f = file();
            if (!Files.exists(f)) return;
            Data d = new Gson().fromJson(Files.readString(f), Data.class);
            if (d == null) return;
            mode = d.mode > 0 ? 1 : 0;
            strength = Math.max(1, Math.min(4, d.strength));
        } catch (Exception ignored) {}
    }

    public static void save() {
        try {
            Data d = new Data();
            d.mode = mode;
            d.strength = strength;
            Files.writeString(file(), new Gson().toJson(d));
        } catch (Exception ignored) {}
    }

    @Override
    public void onInitializeClient() {
        load();
        KeyMapping.Category cat = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(ID, "main"));
        menuKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.novablur.menu", InputConstants.Type.KEYSYM, InputConstants.KEY_GRAVE, cat));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (menuKey.consumeClick()) {
                if (mc.player != null && mc.gui.screen() == null) mc.gui.setScreen(new BlurScreen());
            }
        });
    }
}
