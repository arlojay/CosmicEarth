package com.arlojay.cosmicearth.lib.noise.impl;

public class NoiseError extends Exception {
    private final Exception origin;
    private final String message;

    public NoiseError(Exception origin, String message) {
        this.message = message;
        this.origin = origin;
    }

    @Override
    public String getMessage() {
        return "An error occurred whilst parsing a subnode of " + toString();
    }

    public String toString() {
        var name = this.message;
        if(origin instanceof NoiseError) {
            name += " > " + origin;
        } else {
            name += "\n" + origin.getMessage();
        }

        return name;
    }
}
