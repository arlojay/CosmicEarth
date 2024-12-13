package com.arlojay.cosmicearth.mixins;

import finalforeach.cosmicreach.server.ServerLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerLauncher.class)
public class ServerLauncherMixin {
    @ModifyVariable(method = "main", at = @At("STORE"), ordinal = 1)
    private static String ModifySelectedZoneId(String selectedZoneId) {
        return "cosmicearth:earth";
    }
}
