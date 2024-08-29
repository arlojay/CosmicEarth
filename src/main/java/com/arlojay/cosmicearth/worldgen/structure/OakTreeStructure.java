package com.arlojay.cosmicearth.worldgen.structure;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OakTreeStructure extends WorldgenStructure {
    private static final BlockState airBlock = getBlockStateInstance("base:air[default]");
    private static final BlockState treeLogBlock = getBlockStateInstance("base:tree_log[default]");
    private static final BlockState leavesBlock = getBlockStateInstance("cosmicearth:deciduous_leaves[default]");
    private static final WhiteNoise whiteNoiseTreesH = new WhiteNoise();

    private static final Set<BlockState> leafReplaceMask = new HashSet<>();
    static { leafReplaceMask.add(airBlock); }


    private static final int MIN_CHILD_COUNT = 2;
    private static final int MAX_CHILD_COUNT = 3;
    private static final int LEAF_INDEX = 2;
    private static final int MAX_BRANCH_RECURSION = 3;
    private static final float ROUGHNESS_XZ = 0.5f;
    private static final float ROUGHNESS_Y = 1.0f;
    private static final float XZ_SCALE = 2.0f;
    private static final float Y_BIAS = 0.5f;


    private static final float MIN_LIMB_LENGTH = 3.0f;
    private static final float MAX_LIMB_LENGTH = 4.0f;

    private static final float MIN_LEAF_RADIUS = 3.8f;
    private static final float MAX_LEAF_RADIUS = 4.2f;

    private void generateTree(long seed, Zone zone, Vector3 root, float initialHeight) {
        generateLimb(
                zone, MAX_CHILD_COUNT, 0, new Random(seed),
                0f, initialHeight, 0f,
                root.x, root.y - 1, root.z
        );
    }

    private void generateLimb(Zone zone, int childCount, int index, Random random, float directionX, float directionY, float directionZ, float globalX, float globalY, float globalZ) {

        // Build branch
        BuildHelper.drawLine(
                treeLogBlock, zone,
                globalX, globalY, globalZ,
                globalX + directionX,
                globalY + directionY,
                globalZ + directionZ,
                2f, 2f
        );
        if(index < LEAF_INDEX) {
            BuildHelper.drawLine(
                    treeLogBlock, zone,
                    globalX + 1, globalY, globalZ,
                    globalX + directionX + 1,
                    globalY + directionY,
                    globalZ + directionZ,
                    0f, 2f
            );
            BuildHelper.drawLine(
                    treeLogBlock, zone,
                    globalX - 1, globalY, globalZ,
                    globalX + directionX - 1,
                    globalY + directionY,
                    globalZ + directionZ,
                    0f, 2f
            );
            BuildHelper.drawLine(
                    treeLogBlock, zone,
                    globalX, globalY, globalZ + 1,
                    globalX + directionX,
                    globalY + directionY,
                    globalZ + directionZ + 1,
                    0f, 2f
            );
            BuildHelper.drawLine(
                    treeLogBlock, zone,
                    globalX, globalY, globalZ - 1,
                    globalX + directionX,
                    globalY + directionY,
                    globalZ + directionZ - 1,
                    0f, 2f
            );
        }
        if(index >= LEAF_INDEX) {
            var radius = MathUtils.map(
                    0f, MAX_BRANCH_RECURSION - 1,
                    MAX_LEAF_RADIUS, MIN_LEAF_RADIUS,
                    index
            );

            BuildHelper.drawSphere(
                    leavesBlock, zone, radius,
                    globalX + directionX,
                    globalY + directionY + radius,
                    globalZ + directionZ,
                    leafReplaceMask
            );
        }

        // Divert branch by a factor of randomness in respect to current direction
        if(index != 0) {
            directionX += random.nextFloat(-ROUGHNESS_XZ, ROUGHNESS_XZ);
            directionY += random.nextFloat(-ROUGHNESS_Y, ROUGHNESS_Y);
            directionZ += random.nextFloat(-ROUGHNESS_XZ, ROUGHNESS_XZ);

            var inverseDirectionLength = 1f / Math.sqrt(
                    directionX * directionX +
                            directionY * directionY +
                            directionZ * directionZ
            );
            var branchLength = MathUtils.map(
                    0f, MAX_BRANCH_RECURSION - 1,
                    MAX_LIMB_LENGTH, MIN_LIMB_LENGTH,
                    index
            );
            directionX *= inverseDirectionLength * branchLength * XZ_SCALE;
            directionY *= inverseDirectionLength * branchLength;
            directionZ *= inverseDirectionLength * branchLength * XZ_SCALE;

            directionY += Y_BIAS;
        }

        if(index > MAX_BRANCH_RECURSION) return;

        // Create sub-branches
        for(int i = 0; i < childCount; i++) {
            int nextChildCount = MathUtils.clamp(
                    childCount + (int) Math.signum(random.nextFloat(-1f, 1f)),
                    MIN_CHILD_COUNT, MAX_CHILD_COUNT
            );

            if(index < LEAF_INDEX - 1) nextChildCount = MAX_CHILD_COUNT;

            generateLimb(
                    zone, nextChildCount, index + 1, random,
                    directionX, directionY, directionZ,
                    globalX + directionX,
                    globalY + directionY,
                    globalZ + directionZ
            );
        }
    }

    @Override
    public void generate(long seed, Zone zone, int globalX, int globalY, int globalZ) {
        var height = MathUtils.map(-1f, 1f, 0.5f, 1.2f, whiteNoiseTreesH.noise3D(globalX, globalY, globalZ));
        long randomSeed = seed + (short) ((long) globalX * (long) globalY * (long) globalZ * height);
        generateTree(randomSeed, zone, new Vector3(globalX, globalY, globalZ), MAX_LIMB_LENGTH * height);
    }
}
