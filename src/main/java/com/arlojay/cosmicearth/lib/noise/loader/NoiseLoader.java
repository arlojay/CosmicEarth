package com.arlojay.cosmicearth.lib.noise.loader;

import com.arlojay.cosmicearth.CosmicEarthMod;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.impl.NoiseError;
import com.arlojay.cosmicearth.lib.noise.impl.generator.*;
import com.arlojay.cosmicearth.lib.noise.impl.transformer.*;
import com.badlogic.gdx.files.FileHandle;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class NoiseLoader {
    private static final Map<String, NoiseNodeFactory> noiseNodeFactories = new HashMap<>();

    private static final JsonObject props = new JsonObject();

    public static JsonObject getProps() {
        return props;
    }

    public static NoiseNode createNoiseNode(JsonValue rawSource) throws NoSuchFieldException, FileNotFoundException, NoiseError {
        if(rawSource.isNumber()) {
            return new ConstantValueGenerator(rawSource.asDouble());
        }

        var source = rawSource.asObject();
        var type = source.getString("type", null);
        if(type == null) throw new NoSuchFieldException("Field `type` must exist on all noise nodes");

        if(type.equals("ref")) {
            var locationString = source.getString("file", null);
            if(locationString == null) throw new NoSuchFieldException("Field `file` must exist on `ref` node");
            var id = Identifier.of(locationString);


            try {
                return loadLocation(id);
            } catch (Exception e) {
                throw new NoiseError(e, type);
            }
        }


        if (!noiseNodeFactories.containsKey(type)) throw new NoSuchFieldException("Cannot find node type " + type);
        var factory = noiseNodeFactories.get(type);

        try {
            return factory.create(source);
        } catch (Exception e) {
            throw new NoiseError(e, type);
        }
    }


    public static void registerNoiseNode(String id, NoiseNodeFactory factory) {
        noiseNodeFactories.put(id, factory);
    }

    public static NoiseNode load(String id) throws Exception {
        return load(Identifier.of(id));
    }

    public static NoiseNode load(Identifier id) throws Exception {
        try {
            return loadFile(findSource(id));
        } catch (Exception exception) {
            CosmicEarthMod.LOGGER.error("Exception whilst loading noise graph from id ({})\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public static NoiseNode loadByString(String source) throws Exception {
        try {
            return loadString(source);
        } catch (Exception exception) {
            CosmicEarthMod.LOGGER.error("Exception whilst loading noise graph from string\n{}\n{}", source, exception.getMessage());
            throw exception;
        }
    }

    public static NoiseNode load(JsonValue value) throws Exception {
        try {
            return createNoiseNode(value);
        } catch (Exception exception) {
            CosmicEarthMod.LOGGER.error("Exception whilst loading object noise graph\n{}", exception.getMessage());
            throw exception;
        }
    }


    private static Identifier[] getWorldgenFileLocations(Identifier location) {
        return new Identifier[] {
                location,
                Identifier.of(location.getNamespace() , "worldgen/" + location.getName() + ".hjson")
                ,
                Identifier.of(location.getNamespace() , "worldgen/" + location.getName() + ".jsonc")
                ,
                Identifier.of(location.getNamespace() , "worldgen/" + location.getName() + ".jsonl")
                ,
                Identifier.of(location.getNamespace() , "worldgen/" + location.getName() + ".json")
        };
    }

    private static FileHandle findSource(Identifier id) throws FileNotFoundException {
        FileHandle sourceFile = null;

        for(var file : getWorldgenFileLocations(id)) {
            if(PuzzleGameAssetLoader.assetExists(file)) {
                sourceFile = PuzzleGameAssetLoader.locateAsset(file);
            }
        }

        if(sourceFile == null) {
            throw new FileNotFoundException("Cannot find noise file " + id);
        } else {
            return sourceFile;
        }
    }

    private static NoiseNode loadLocation(Identifier id) throws NoSuchFieldException, FileNotFoundException, NoiseError {
        return loadFile(findSource(id));
    }

    private static NoiseNode loadFile(FileHandle file) throws NoSuchFieldException, FileNotFoundException, NoiseError {
        return loadString(file.readString());
    }

    private static NoiseNode loadString(String sourceString) throws NoSuchFieldException, FileNotFoundException, NoiseError {
        return createNoiseNode(JsonObject.readHjson(sourceString));
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
        NoiseAbsolute.register();
        CoordinateGenerator.register();
        NoiseOperation.register();
        ConstantValueGenerator.register();
        BasicOctaveNoise.register();
    }
}
