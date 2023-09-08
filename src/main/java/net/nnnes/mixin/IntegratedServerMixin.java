package net.nnnes.mixin;

import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.GameMode;
import net.nnnes.RememberLANPortConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("remember-lan-port");

    @Inject(at = @At(value = "RETURN", ordinal = 0), method = "openToLan")
    private void openToLan(GameMode gameMode, boolean cheatsAllowed, int port, CallbackInfoReturnable<Boolean> cir) {
        RememberLANPortConfig cfg = new RememberLANPortConfig();
        try {
            String oldPort = (cfg.fileExists() ? cfg.readPort() : "");
            if (!String.valueOf(port).equals(oldPort)) cfg.writePort(port);
        } catch (IOException e) {
            LOGGER.error("Failed to write config to " + cfg.path);
        }
    }
}