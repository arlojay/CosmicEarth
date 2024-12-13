package com.arlojay.cosmicearth.worldgen.biome.util;

import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.ClusterBiomeStructure;
import com.arlojay.cosmicearth.worldgen.structure.ClusterStructure;
import com.arlojay.cosmicearth.worldgen.structure.FoliageClusterStructure;
import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import com.arlojay.cosmicearth.worldgen.structure.WorldgenStructure;
import finalforeach.cosmicreach.blocks.BlockState;

import java.util.List;
import java.util.function.Function;

public class StructureSet {
    public static void pebbles(List<BiomeStructure> structures) {
        for(int i = 0; i < 8; i++) {
            final int index = i;
            structures.add(new ClusterBiomeStructure() {
                @Override
                protected double getAverageDistance() {
                    return 96;
                }

                @Override
                protected WorldgenStructure getWorldgenStructure() {
                    return new ClusterStructure(Palettes.instance.pebbles[index], 1, 3, 0.1d) {
                        @Override
                        protected String getId() {
                            return "cluster-pebble-" + index;
                        }

                        @Override
                        public Function<BlockState, Boolean> createGroundChecker() {
                            return bs -> !bs.walkThrough && bs.isOpaque && !bs.isFluid;
                        }

                        @Override
                        public Function<BlockState, Boolean> createAirChecker() {
                            return bs -> bs.hasTag("foliage_replaceable");
                        }
                    };
                }
            });
        }
    }

    public static void flowers(List<BiomeStructure> structures, BlockState[] flowers) {
        for(var blockState : flowers) {
            final var flower = blockState;
            structures.add(new ClusterBiomeStructure() {
                @Override
                protected double getAverageDistance() {
                    return 48;
                }

                @Override
                protected WorldgenStructure getWorldgenStructure() {
                    return new FoliageClusterStructure(flower, 3, 7, 0.1d) {
                        @Override
                        protected String getId() {
                            return "cluster-flower-" + flower.getBlockId();
                        }
                    };
                }
            });
        }
    }

    public static void shortGrass(List<BiomeStructure> structures) {
        foliageCluster(structures, 0.2d, 12, 4, 7, Palettes.instance.short_grass);
    }

    public static void tallGrass(List<BiomeStructure> structures) {
        foliageCluster(structures, 0.1d, 12, 4, 7, Palettes.instance.tall_grass);
    }

    public static void shrub(List<BiomeStructure> structures) {
        foliageCluster(structures, 0.3d, 32, 2, 5, Palettes.instance.shrub);
    }

    public static void foliageCluster(List<BiomeStructure> structures, double density, double distance, double minRadius, double maxRadius, BlockState block) {
        structures.add(new ClusterBiomeStructure() {
            @Override
            protected double getAverageDistance() {
                return distance;
            }

            @Override
            protected WorldgenStructure getWorldgenStructure() {
                return new FoliageClusterStructure(block, minRadius, maxRadius, density) {
                    @Override
                    protected String getId() {
                        return "cluster-generic-" + block.getBlockId();
                    }
                };
            }
        });
    }
}
