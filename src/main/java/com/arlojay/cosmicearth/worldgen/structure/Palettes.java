package com.arlojay.cosmicearth.worldgen.structure;

import com.arlojay.cosmicearth.lib.variety.GroupedPalette;
import com.arlojay.cosmicearth.lib.variety.PaletteItem;
import com.arlojay.cosmicearth.lib.variety.RandomPalette;
import finalforeach.cosmicreach.blocks.BlockState;

import java.util.Set;

import static finalforeach.cosmicreach.blocks.MissingBlockStateResult.EXCEPTION;

public class Palettes {
    public static Palettes instance;

    public static Palettes getInstance() { return instance; }

    public Palettes() { instance = this; }


    public final BlockState black_iris = get("cosmicearth:black_iris[default]");
    public final BlockState black_tulip = get("cosmicearth:black_tulip[default]");
    public final BlockState bluebell = get("cosmicearth:bluebell[default]");
    public final BlockState buttercup = get("cosmicearth:buttercup[default]");
    public final BlockState daisy = get("cosmicearth:daisy[default]");
    public final BlockState fire_iris = get("cosmicearth:fire_iris[default]");
    public final BlockState hyacinth = get("cosmicearth:hyacinth[default]");
    public final BlockState narcissus = get("cosmicearth:narcissus[default]");
    public final BlockState orange_pansy = get("cosmicearth:orange_pansy[default]");
    public final BlockState orange_tulip = get("cosmicearth:orange_tulip[default]");
    public final BlockState orchid = get("cosmicearth:orchid[default]");
    public final BlockState pink_lily = get("cosmicearth:pink_lily[default]");
    public final BlockState pink_mimosa = get("cosmicearth:pink_mimosa[default]");
    public final BlockState pink_tulip = get("cosmicearth:pink_tulip[default]");
    public final BlockState purple_iris = get("cosmicearth:purple_iris[default]");
    public final BlockState purple_pansy = get("cosmicearth:purple_pansy[default]");
    public final BlockState red_pansy = get("cosmicearth:red_pansy[default]");
    public final BlockState red_tulip = get("cosmicearth:red_tulip[default]");
    public final BlockState rose = get("cosmicearth:rose[default]");
    public final BlockState violet = get("cosmicearth:violet[default]");
    public final BlockState white_iris = get("cosmicearth:white_iris[default]");
    public final BlockState white_lily = get("cosmicearth:white_lily[default]");
    public final BlockState white_mimosa = get("cosmicearth:white_mimosa[default]");
    public final BlockState white_pansy = get("cosmicearth:white_pansy[default]");
    public final BlockState yellow_lily = get("cosmicearth:yellow_lily[default]");
    public final BlockState yellow_tulip = get("cosmicearth:yellow_tulip[default]");

    public final BlockState[] flowers = new BlockState[] {
            black_iris, black_tulip, bluebell, buttercup, daisy, fire_iris, hyacinth,
            narcissus, orange_pansy, orange_tulip, orchid, pink_lily, pink_mimosa,
            pink_tulip, purple_iris, purple_pansy, red_pansy, red_tulip, rose, violet,
            white_iris, white_lily, white_mimosa, white_pansy, yellow_lily, yellow_tulip,
    };

    public final BlockState short_grass = get("cosmicearth:short_grass[default]");
    public final BlockState tall_grass = get("cosmicearth:tall_grass[default]");
    public final BlockState shrub = get("cosmicearth:shrub[default]");

    public final BlockState cactus = get("cosmicearth:cactus[default]");

    public final BlockState coniferous_leaves = get("cosmicearth:coniferous_leaves[default]");
    public final BlockState deciduous_leaves = get("cosmicearth:deciduous_leaves[default]");
    public final BlockState tree_log = get("base:tree_log[default]");

    public final BlockState[] pebbles = new BlockState[] {
            get("cosmicearth:pebbles[model=0]"),
            get("cosmicearth:pebbles[model=1]"),
            get("cosmicearth:pebbles[model=2]"),
            get("cosmicearth:pebbles[model=3]"),
            get("cosmicearth:pebbles[model=4]"),
            get("cosmicearth:pebbles[model=5]"),
            get("cosmicearth:pebbles[model=6]"),
            get("cosmicearth:pebbles[model=7]"),
    };


    public final BlockState air = get("base:air[default]");
    public final BlockState stone = get("base:stone_basalt[default]");
    public final BlockState gabbro = get("base:stone_gabbro[default]");
    public final BlockState limestone = get("base:stone_limestone[default]");
    public final BlockState grass = get("base:grass[default]");
    public final BlockState grass_full = get("base:grass[type=full]");
    public final BlockState dirt = get("base:dirt[default]");
    public final BlockState sand = get("base:sand[default]");
    public final BlockState water = get("base:water[default]");
    public final BlockState gravel = get("base:stone_gravel[default]");
    public final BlockState magma = get("base:magma[default]");
    public final BlockState snow = get("base:snow[default]");

    public final RandomPalette<BlockState> steepGradientTopsoil = new RandomPalette<>(Set.of(
            new PaletteItem<>(grass, 1d),
            new PaletteItem<>(gravel, 1d),
            new PaletteItem<>(gabbro, 2d)
    ));

    public final RandomPalette<BlockState> steepGradientLoam = new RandomPalette<>(Set.of(
            new PaletteItem<>(gravel, 1d),
            new PaletteItem<>(stone, 1d),
            new PaletteItem<>(gabbro, 2d)
    ));

    public final RandomPalette<BlockState> tundraSoil = new RandomPalette<>(Set.of(
            new PaletteItem<>(gravel, 1d),
            new PaletteItem<>(stone, 1d),
            new PaletteItem<>(snow, 5d)
    ));

    public final GroupedPalette<BlockState> stoneType = new GroupedPalette<>(Set.of(
            new PaletteItem<>(gabbro, -1d),
            new PaletteItem<>(gravel, 1d)
    ));

    private BlockState get(String tag) {
        return BlockState.getInstance(tag, EXCEPTION);
    }
}
