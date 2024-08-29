package com.arlojay.cosmicearth.lib.noise.loader;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.impl.NoiseError;
import org.hjson.JsonObject;

import java.io.FileNotFoundException;

public interface NoiseNodeFactory {
    NoiseNode create(JsonObject object) throws NoSuchFieldException, FileNotFoundException, NoiseError;
}
