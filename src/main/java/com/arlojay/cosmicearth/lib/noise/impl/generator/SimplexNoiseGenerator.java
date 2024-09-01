package com.arlojay.cosmicearth.lib.noise.impl.generator;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseGenerator;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import libs.opensimplex2.OpenSimplex2;
import org.hjson.JsonObject;

public class SimplexNoiseGenerator extends NoiseGenerator {
    public static void register() {
        NoiseLoader.registerNoiseNode("simplex",
                (JsonObject options) -> new SimplexNoiseGenerator(
                        NoiseLoader.getProps().getLong("seed", 0L),
                        options.getLong("seed", 0L)
                )
        );
    }

    public SimplexNoiseGenerator(long seed) {
        super(seed, 0L);
    }
    public SimplexNoiseGenerator(long seed, long seedOffset) {
        super(seed, seedOffset);
    }

    public SimplexNoiseGenerator asCopy() {
        return new SimplexNoiseGenerator(seed, seedOffset);
    }

    @Override
    public double sample(double t) {
        return OpenSimplex2.noise2(seed, t + 0.33203521210701537, t + 0.0035065579164312854);
    }

    @Override
    public double sample(double x, double y) {
        return OpenSimplex2.noise2(seed, x + 0.5742492042415408, y + 0.25276704204947187);
    }

    @Override
    public double sample(double x, double y, double z) {
        return OpenSimplex2.noise3_ImproveXZ(seed, x + 0.9959191838034964, y + 0.48376584066748474, z + 0.5406727367495037);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return OpenSimplex2.noise4_ImproveXYZ(seed, x + 0.9535267210365532, y + 0.26090357662901487, z + 0.056998968405176154, w + 0.8028130016170094);
    }

    @Override
    public String buildString() {
        return "@SimplexNoise" + NoiseDebugString.createPropertyList(
                "seed", seed,
                "seedOffset", seedOffset
        );
    }
}
