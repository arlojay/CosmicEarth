package com.arlojay.cosmicearth.mixin;

import com.arlojay.cosmicearth.worldgen.EarthZoneGenerator;
import finalforeach.cosmicreach.worldgen.ZoneGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZoneGenerator.class)
public abstract class ZoneGeneratorMixin {
    @Shadow
    public static void registerZoneGenerator(ZoneGenerator zoneGenerator) {}

    @Inject(method = "registerZoneGenerators", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/worldgen/ZoneGenerator;registerZoneGenerator(Lfinalforeach/cosmicreach/worldgen/ZoneGenerator;)V", ordinal = 0))
    private static void registerZoneGenerators(CallbackInfo ci) {
        registerZoneGenerator(new EarthZoneGenerator());
    }
}
