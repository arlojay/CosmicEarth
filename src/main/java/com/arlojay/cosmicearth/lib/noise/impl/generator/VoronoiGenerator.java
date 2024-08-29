package com.arlojay.cosmicearth.lib.noise.impl.generator;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseGenerator;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

import java.util.Random;

public class VoronoiGenerator extends NoiseGenerator {
    public enum VoronoiMode { CELL, DISTANCE }

    private static final int stride = 1;

    private final VoronoiMode mode;
    private final double randomness;
    private final Random randomGenerator;

    public static void register() {
        NoiseLoader.registerNoiseNode("voronoi", (JsonObject options) -> {
            double randomness = options.getDouble("randomness", 1d);
            String mode = options.getString("mode", VoronoiMode.CELL.toString());


            return new VoronoiGenerator(
                    NoiseLoader.getProps().getLong("seed", 0L),
                    options.getLong("seed", 0L),
                    randomness,
                    VoronoiMode.valueOf(mode.toUpperCase())
            );
        });
    }


    public VoronoiGenerator(long seed, long seedOffset, VoronoiMode mode) {
        this(seed, seedOffset, 1.0d, mode);
    }
    public VoronoiGenerator(long seed, VoronoiMode mode) {
        this(seed, 0L, 1.0d, mode);
    }
    public VoronoiGenerator(long seed, double randomness, VoronoiMode mode) {
        this(seed, 0L, randomness, mode);
    }
    public VoronoiGenerator(long seed, long seedOffset, double randomness, VoronoiMode mode) {
        super(seed, seedOffset);
        this.mode = mode;
        this.randomness = randomness * (Math.sqrt(3d) / 2d);
        this.randomGenerator = new Random(seed);
    }

    public VoronoiGenerator asCopy() {
        return new VoronoiGenerator(seed, seedOffset, randomness, mode);
    }

    private double random(long seed, double t) {
        randomGenerator.setSeed(seed + Double.doubleToLongBits(t / 110.38907218937722));
        return randomGenerator.nextFloat() * 2d - 1d;
    }

    @Override
    public double sample(double t) {
        double closest = 100d;
        double closestId = 0.5d;
        int it = (int) Math.round(t);

        for(double vt = it - stride; vt <= it + stride; vt++) {
            double id = random(seed, vt + 110.38907218937722);
            double pt = vt + random(seed, id) * randomness;

            var d = (pt - t) * (pt - t);
            if(d < closest) {
                closest = d;
                closestId = id;
            }
        }

        return this.mode.equals(VoronoiMode.CELL) ? closestId : (1d - 2d / Math.exp(closest));
    }

    @Override
    public double sample(double x, double y) {
        double closest = 100d;
        double closestId = 0.5d;
        int ix = (int) Math.round(x);
        int iy = (int) Math.round(y);

        for(double vx = ix - stride; vx <= ix + stride; vx++) {
            for(double vy = iy - stride; vy <= iy + stride; vy++) {
                double id = random(seed, vx + 679.7003017998444 + random(seed, vy + 101.29766833328003) * 614.8610786306203);
                double px = vx + random(seed, id) * randomness;
                double py = vy + random(seed, id + 1d) * randomness;

                var d = (px - x) * (px - x) + (py - y) * (py - y);
                if(d < closest) {
                    closest = d;
                    closestId = id;
                }
            }
        }

        return this.mode.equals(VoronoiMode.CELL) ? closestId : (1d - 2d / Math.exp(closest));
    }

    @Override
    public double sample(double x, double y, double z) {
        double closest = 100d;
        double closestId = 0.5d;
        int ix = (int) Math.round(x);
        int iy = (int) Math.round(y);
        int iz = (int) Math.round(z);

        for(double vx = ix - stride; vx <= ix + stride; vx++) {
            for(double vy = iy - stride; vy <= iy + stride; vy++) {
                for(double vz = iz - stride; vz <= iz + stride; vz++) {
                    double id = random(seed, vx + 370.8698605604355 + random(seed, vy + 394.583012342351 + random(seed, vz + 266.0448425753781) * 535.879996615408) * 855.0495020173028);
                    double px = vx + random(seed, id) * randomness;
                    double py = vy + random(seed, id + 1d) * randomness;
                    double pz = vz + random(seed, id + 2d) * randomness;

                    var d = (px - x) * (px - x) + (py - y) * (py - y) + (pz - z) * (pz - z);
                    if (d < closest) {
                        closest = d;
                        closestId = id;
                    }
                }
            }
        }

        return this.mode.equals(VoronoiMode.CELL) ? closestId : (1d - 2d / Math.exp(closest));
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        double closest = 100d;
        double closestId = 0.5d;
        int ix = (int) Math.round(x);
        int iy = (int) Math.round(y);
        int iz = (int) Math.round(z);
        int iw = (int) Math.round(w);

        for(double vx = ix - stride; vx <= ix + stride; vx++) {
            for(double vy = iy - stride; vy <= iy + stride; vy++) {
                for(double vz = iz - stride; vz <= iz + stride; vz++) {
                    for(double vw = iw - stride; vw <= iw + stride; vw++) {
                        double id = random(seed, vx + 475.51918929852224 + random(seed, vy + 559.6194540791413 + random(seed, vz + 537.5058052033661 + random(seed, vz + 670.3566393736775) * 674.2441465497373) * 938.0612999473237) * 30.238348418657026);
                        double px = vx + random(seed, id) * randomness;
                        double py = vy + random(seed, id + 1d) * randomness;
                        double pz = vz + random(seed, id + 2d) * randomness;
                        double pw = vw + random(seed, id + 3d) * randomness;

                        var d = (px - x) * (px - x) + (py - y) * (py - y) + (pz - z) * (pz - z) + (pw - w) * (pw - w);
                        if (d < closest) {
                            closest = d;
                            closestId = id;
                        }
                    }
                }
            }
        }

        return this.mode.equals(VoronoiMode.CELL) ? closestId : (1d - 2d / Math.exp(closest));
    }

    @Override
    public String buildString() {
        return "@Voronoi" + NoiseDebugString.createPropertyList(
                "seed", seed,
                "seedOffset", seedOffset,
                "randomness", randomness,
                "mode", mode
        );
    }
}
