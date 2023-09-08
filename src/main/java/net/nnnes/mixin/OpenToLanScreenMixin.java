package net.nnnes.mixin;

import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.nnnes.RememberLANPortConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(OpenToLanScreen.class)
public class OpenToLanScreenMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("remember-lan-port");
    @Shadow
    private TextFieldWidget portField;

    @Final
    @Shadow
    private static int MIN_PORT;
    @Final
    @Shadow
    private static int MAX_PORT;

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
        RememberLANPortConfig cfg = new RememberLANPortConfig(MIN_PORT, MAX_PORT);
        try {
            if (!cfg.fileExists()) cfg.writeDefault();
            String port = cfg.readPort();
            this.portField.setText(port);
            this.portField.setEditableColor(0x55ff55);
        } catch (IOException e) {
            LOGGER.error("Failed to read config from " + cfg.path);
        }
    }
}