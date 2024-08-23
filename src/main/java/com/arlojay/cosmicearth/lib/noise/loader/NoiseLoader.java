package com.arlojay.cosmicearth.lib.noise.loader;

import com.arlojay.cosmicearth.CosmicEarthMod;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.impl.*;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import com.github.puzzle.core.resources.ResourceLocation;
import org.hjson.JsonObject;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class NoiseLoader {
    private static final Map<String, NoiseNodeFactory> noiseNodeFactories = new HashMap<>();

    private static final JsonObject props = new JsonObject();

    public static JsonObject getProps() {
        return props;
    }

    private static ResourceLocation getWorldgenFileLocation(ResourceLocation location) {
        return new ResourceLocation(
                location.namespace,
                "worldgen/" + location.name + ".json"
        );
    }

    public static NoiseNode loadById(String id) throws Exception {
        return loadById(ResourceLocation.fromString(id));
    }

    public static NoiseNode loadById(ResourceLocation id) throws Exception {
        var source = getWorldgenFileLocation(id);
        try {
            return loadLocation(source);
        } catch (Exception exception) {
            CosmicEarthMod.LOGGER.error("Exception whilst loading noise graph " + source + "\n" + exception.getMessage());
            throw exception;
        }
    }

    public static NoiseNode loadByPath(ResourceLocation source) throws Exception {
        try {
            return loadLocation(source);
        } catch (Exception exception) {
            CosmicEarthMod.LOGGER.error("Exception whilst loading noise graph " + source + "\n" + exception.getMessage());
            throw exception;
        }
    }

    public static NoiseNode loadByString(String source) throws Exception {
        try {
            return loadString(source);
        } catch (Exception exception) {
            CosmicEarthMod.LOGGER.error("Exception whilst loading noise graph " + source + "\n" + exception.getMessage());
            throw exception;
        }
    }

    public static NoiseNode loadByObject(JsonObject object) throws Exception {
        try {
            return createNoiseNode(object);
        } catch (Exception exception) {
            CosmicEarthMod.LOGGER.error("Exception whilst loading object noise graph\n" + exception.getMessage());
            throw exception;
        }
    }

    private static NoiseNode loadLocation(ResourceLocation source) throws NoSuchFieldException, FileNotFoundException {
        var sourceFile = PuzzleGameAssetLoader.locateAsset(source.toPath());
        if(sourceFile == null) {
            sourceFile = PuzzleGameAssetLoader.locateAsset(getWorldgenFileLocation(source).toPath());
        }

        if(sourceFile == null) throw new FileNotFoundException("Cannot find noise file " + source);

        var sourceString = sourceFile.readString();

        return loadString(sourceString);
    }

    private static NoiseNode loadString(String sourceString) throws NoSuchFieldException, FileNotFoundException {
        var json = JsonObject.readHjson(sourceString).asObject();

        return createNoiseNode(json);
    }

    public static void registerNoiseNode(String id, NoiseNodeFactory factory) {
        noiseNodeFactories.put(id, factory);
    }

    public static NoiseNode createNoiseNode(JsonObject source) throws NoSuchFieldException, FileNotFoundException {
        var type = source.getString("type", null);
        if(type == null) throw new NoSuchFieldException("Field `type` must exist on all noise nodes");

        if(type.equals("ref")) {
            var locationString = source.getString("file", null);
            if(locationString == null) throw new NoSuchFieldException("Field `file` must exist on `ref` node");
            var id = ResourceLocation.fromString(locationString);

            return loadLocation(id);
        }


        if (!noiseNodeFactories.containsKey(type)) throw new NoSuchFieldException("Cannot find node type " + type);
        var factory = noiseNodeFactories.get(type);
        return factory.create(source);
    }

    public static void registerDefaultNoiseNodes() {
        ErodedNoise.register();
        NoiseGradientTransformer.register();
        NoiseMapper.register();
        NoiseMixer.register();
        NoiseScaler.register();
        NoiseSpline.register();
        OctaveNoise.register();
        SimplexNoiseGenerator.register();
        VoronoiGenerator.register();
        WhiteNoiseGenerator.register();
    }
}
