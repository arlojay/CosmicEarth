package com.arlojay.cosmicearth.worldgen;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.ArrayList;
import java.util.Random;

public class OreGenerator {

    private static final WhiteNoise seedGenerator = new WhiteNoise();
    private static final Vector3 tempVector = new Vector3();

    public static void generateChunkMask(ChunkMask chunkMask, int startX, int startY, int startZ, int maxBlocks, long seed) {
        seedGenerator.setSeed(seed);
        var random = new Random(Float.floatToIntBits(seedGenerator.noise3D(startX, startY, startZ)));

        var positions = new ArrayList<Vector3>();
        var setPositions = new ArrayList<Vector3>();

        positions.add(new Vector3(startX & 0xf, startY & 0xf, startZ & 0xf));

        while (positions.size() > 0 && setPositions.size() < maxBlocks) {
            var position = positions.get(0);
            positions.remove(position);
            setPositions.add(position);

            chunkMask.set((int) position.x, (int) position.y, (int) position.z, true);

            // Direction
            switch (random.nextInt(0, 6)) {
                case 0:
                    tempVector.set(position).add(0, 0, 1);
                    if(!setPositions.contains(tempVector) && !positions.contains(tempVector)) {
                        positions.add(tempVector.cpy());
                        break;
                    }
                case 1:
                    tempVector.set(position).add(0, 1, 0);
                    if(!setPositions.contains(tempVector) && !positions.contains(tempVector)) {
                        positions.add(tempVector.cpy());
                        break;
                    }
                case 2:
                    tempVector.set(position).add(1, 0, 0);
                    if(!setPositions.contains(tempVector) && !positions.contains(tempVector)) {
                        positions.add(tempVector.cpy());
                        break;
                    }
                case 3:
                    tempVector.set(position).add(0, 0, -1);
                    if(!setPositions.contains(tempVector) && !positions.contains(tempVector)) {
                        positions.add(tempVector.cpy());
                        break;
                    }
                case 4:
                    tempVector.set(position).add(0, -1, 0);
                    if(!setPositions.contains(tempVector) && !positions.contains(tempVector)) {
                        positions.add(tempVector.cpy());
                        break;
                    }
                case 5:
                    tempVector.set(position).add(-1, 0, 0);
                    if(!setPositions.contains(tempVector) && !positions.contains(tempVector)) {
                        positions.add(tempVector.cpy());
                        break;
                    }
            }
        }
    }
}
