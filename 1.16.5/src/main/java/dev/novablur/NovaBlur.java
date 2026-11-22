package dev.novablur;

import com.google.gson.Gson;
import dev.novablur.gui.BlurScreen;
import dev.novablur.mixin.MinecraftClientAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class NovaBlur implements ClientModInitializer {
    /** 0 = off, 1 = on (motion blur) */
    public static int mode = 0;
    /** 1..4 */
    public static int strength = 2;

    public static KeyBinding menuKey;

    private static class Data { int mode = 0; int strength = 2; }

    private static Path file() { return FabricLoader.getInstance().getConfigDir().resolve("novablur.json"); }

    private static void load() {
        try {
            Path f = file();
            if (!Files.exists(f)) return;
            Data d = new Gson().fromJson(new String(Files.readAllBytes(f), StandardCharsets.UTF_8), Data.class);
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
            Files.write(file(), new Gson().toJson(d).getBytes(StandardCharsets.UTF_8));
        } catch (Exception ignored) {}
    }

    @Override
    public void onInitializeClient() {
        load();
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.novablur.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_GRAVE_ACCENT, "key.categories.novablur"));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (menuKey.wasPressed()) {
                if (mc.world != null && mc.currentScreen == null) mc.openScreen(new BlurScreen());
            }
            apply(mc);
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(BlurManager::releaseAll);
    }

    private static void apply(MinecraftClient mc) {
        if (mc.world == null || mode == 0) {
            BlurManager.releaseAll(mc);
            return;
        }
        boolean ourMenu = mc.currentScreen instanceof BlurScreen;
        if (mc.currentScreen == null || ourMenu) {
            int fps = Math.max(20, Math.min(500, MinecraftClientAccessor.getCurrentFps()));
            double base = 0.12 * strength + 0.05; // 0.17 .. 0.53
            float s = (float) Math.pow(base, 60.0 / fps);
            BlurManager.request(mc, BlurManager.Kind.MOTION, "BlurStrength", s);
        } else {
            BlurManager.releaseAll(mc);
        }
    }
}
