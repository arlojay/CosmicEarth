package com.arlojay.cosmicearth.worldgen.structure;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.Set;

public class BuildHelper {
    public static void setBlockState(Zone zone, BlockState blockState, double globalX, double globalY, double globalZ) {
        setBlockState(zone, blockState, (int) globalX, (int) globalY, (int) globalZ);
    }
    public static void setBlockState(Zone zone, BlockState blockState, float globalX, float globalY, float globalZ) {
        setBlockState(zone, blockState, (int) globalX, (int) globalY, (int) globalZ);
    }
    public static void setBlockState(Zone zone, BlockState blockState, int globalX, int globalY, int globalZ) {
        int cx = Math.floorDiv(globalX, 16);
        int cy = Math.floorDiv(globalY, 16);
        int cz = Math.floorDiv(globalZ, 16);
        Chunk c = zone.getChunkAtChunkCoords(cx, cy, cz);
        if (c == null) {
            c = new Chunk(cx, cy, cz);
            c.initChunkData();
            c.setGenerated(true);
            zone.addChunk(c);
        }

        int x = globalX - 16 * cx;
        int y = globalY - 16 * cy;
        int z = globalZ - 16 * cz;
        c.setBlockState(blockState, x, y, z);
        c.flagForRemeshing(false);
    }

    public static BlockState getBlockState(Zone zone, int globalX, int globalY, int globalZ) {
        return zone.getBlockState(globalX, globalY, globalZ);
    }
    public static BlockState getBlockState(Zone zone, double globalX, double globalY, double globalZ) {
        return zone.getBlockState((int) globalX, (int) globalY, (int) globalZ);
    }
    public static BlockState getBlockState(Zone zone, float globalX, float globalY, float globalZ) {
        return zone.getBlockState((int) globalX, (int) globalY, (int) globalZ);
    }

    public static void placeDisk(Zone zone, int globalX, int globalY, int globalZ, double size, BlockState block) {
        var sqSize = size * size;

        for(int dx = -(int)size; dx <= (int)size; dx++) {
            for (int dz = -(int) size; dz <= (int) size; dz++) {
                if(dx * dx + dz * dz > sqSize) continue;

                setBlockState(zone, block, globalX + dx, globalY, globalZ + dz);
            }
        }
    }

    public static int findClosestBlockInColumn(Zone zone, BlockState blockState, int globalX, int startingY, int globalZ) {
        int height = 0;

        boolean checkingBelow = false;

        for(int tries = 0; tries < 64; tries++) {
            checkingBelow = !checkingBelow;

            var globalY = startingY + (checkingBelow ? -height : height);
            var checkingBlock = zone.getBlockState(globalX, globalY, globalZ);

            if(checkingBlock == null) continue;

            if (checkingBlock.equals(blockState)) return globalY;

            if(checkingBelow) height++;
        }

        return startingY;
    }

    public static Integer findClosestBlockInColumn(Zone zone, BlockState[] blockStates, int globalX, int startingY, int globalZ) {
        int height = 0;

        boolean checkingBelow = false;

        for(int tries = 0; tries < 64; tries++) {
            checkingBelow = !checkingBelow;

            var globalY = startingY + (checkingBelow ? -height : height);
            var checkingBlock = zone.getBlockState(globalX, globalY, globalZ);

            if(checkingBlock == null) continue;

            for(var blockState : blockStates) {
                if (checkingBlock.equals(blockState)) return globalY;
            }

            if(checkingBelow) height++;
        }

        return null;
    }

    private static final WhiteNoise densityNoise = new WhiteNoise();
    private static void scatterBlock(
            Zone zone, long seed,
            BlockState[] floorMask, BlockState[] airMask,
            int globalX, int globalY, int globalZ,
            double clusterSize, double density,
            BlockState blockState,

            boolean ignoreAir
    ) {
        densityNoise.setSeed(seed);
        double clusterSizeSq = clusterSize * clusterSize;

        int intRadius = (int) Math.ceil(clusterSize);

        for(int dx = -intRadius; dx <= intRadius; dx++) {
            for(int dz = -intRadius; dz <= intRadius; dz++) {
                if(dx * dx + dz * dz > clusterSizeSq) continue;

                var x = globalX + dx;
                var z = globalZ + dz;

                var densitySample = densityNoise.noise2D(x, z) * 0.5d + 0.5d;

                if(density < densitySample) continue;

                var foundY = BuildHelper.findClosestBlockInColumn(zone, floorMask, x, globalY, z);
                if(foundY == null) continue;

                var y = foundY + 1;

                if(!ignoreAir) {
                    boolean isAcceptableLocation = false;

                    for (var airBlock : airMask) {
                        if (zone.getBlockState(x, y, z).equals(airBlock)) isAcceptableLocation = true;
                    }

                    if(!isAcceptableLocation) continue;
                }

                BuildHelper.setBlockState(zone, blockState, x, y, z);
            }
        }
    }

    public static void scatterBlock(
            Zone zone, long seed,
            BlockState[] floorMask, BlockState[] airMask,
            int globalX, int globalY, int globalZ,
            double clusterSize, double density,
            BlockState blockState
    ) {
        scatterBlock(
                zone, seed,
                floorMask, airMask,
                globalX, globalY, globalZ,
                clusterSize, density,
                blockState,
                false
        );
    }

    public static void scatterBlock(
            Zone zone, long seed,
            BlockState[] floorMask,
            int globalX, int globalY, int globalZ,
            double clusterSize, double density,
            BlockState blockState
    ) {
        scatterBlock(
                zone, seed,
                floorMask, floorMask,
                globalX, globalY, globalZ,
                clusterSize, density,
                blockState,
                true
        );
    }


    public static void drawLine(BlockState block, Zone zone, Vector3 from, Vector3 to) {
        drawLine(block, zone, from.x, from.y, from.z, to.x, to.y, to.z, 0f, 0f);
    }
    public static void drawLine(BlockState block, Zone zone, Vector3 from, Vector3 to, float overdrawMin, float overdrawMax) {
        drawLine(block, zone, from.x, from.y, from.z, to.x, to.y, to.z, overdrawMin, overdrawMax);
    }
    public static void drawLine(
            BlockState block, Zone zone,
            float fromX, float fromY, float fromZ,
            float toX, float toY, float toZ,
            float overdrawMin, float overdrawMax
    ) {
        float distance = Vector3.dst(fromX, fromY, fromZ, toX, toY, toZ);
        float invDistance;

        for(float i = -overdrawMin; i < distance + overdrawMax; i++) {
            invDistance = i / distance;
            setBlockState(zone, block,
                    Math.round((toX - fromX) * invDistance + fromX),
                    Math.round((toY - fromY) * invDistance + fromY),
                    Math.round((toZ - fromZ) * invDistance + fromZ)
            );
        }
    }
    public static void drawLine(
            BlockState block, Zone zone,
            float fromX, float fromY, float fromZ,
            float toX, float toY, float toZ,
            Set<BlockState> replaceMask
    ) {
        float distance = Vector3.dst(fromX, fromY, fromZ, toX, toY, toZ);
        float invDistance;

        BlockState bs;
        float bx;
        float by;
        float bz;

        for(float i = 0; i < distance; i++) {
            invDistance = i / distance;
            bs = getBlockState(zone,
                    bx = (toX - fromX) * invDistance + fromX,
                    by = (toY - fromY) * invDistance + fromY,
                    bz = (toZ - fromZ) * invDistance + fromZ
            );
            if(!replaceMask.contains(bs)) continue;
            setBlockState(zone, block, bx, by, bz);
        }
    }

    public static void drawSphere(
            BlockState block, Zone zone,
            float radius,
            float x, float y, float z
    ) {
        int maxRadius = (int)Math.ceil(radius);
        float radiusSqr = radius * radius;

        for(int dx = -maxRadius; dx <= maxRadius; dx++) {
            for(int dy = -maxRadius; dy <= maxRadius; dy++) {
                for (int dz = -maxRadius; dz <= maxRadius; dz++) {
                    if(Vector3.len2(dx, dy, dz) > radiusSqr) continue;
                    setBlockState(zone, block, x + dx, y + dy, z + dz);
                }
            }
        }
    }

    public static void drawSphere(
            BlockState block, Zone zone,
            float radius,
            float x, float y, float z,
            Set<BlockState> replaceMask
    ) {
        int maxRadius = (int)Math.ceil(radius);
        float radiusSqr = radius * radius;

        BlockState bs;

        for(int dx = -maxRadius; dx <= maxRadius; dx++) {
            for(int dy = -maxRadius; dy <= maxRadius; dy++) {
                for (int dz = -maxRadius; dz <= maxRadius; dz++) {
                    if(Vector3.len2(dx, dy, dz) > radiusSqr) continue;
                    bs = getBlockState(zone, x + dx, y + dy, z + dz);
                    if(!replaceMask.contains(bs)) continue;
                    setBlockState(zone, block, x + dx, y + dy, z + dz);
                }
            }
        }
    }
}
