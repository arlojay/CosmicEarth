package com.arlojay.cosmicearth;

import finalforeach.cosmicreach.ui.debug.DebugInfo;
import finalforeach.cosmicreach.ui.debug.DebugItem;

import java.util.HashMap;
import java.util.Map;

public class Debug {
    private static final Map<String, DebugItem> debugItems = new HashMap<>();

    public static void addDebugItem(String id, DebugItem item) {
        if(debugItems.containsKey(id)) DebugInfo.removeDebugItem(debugItems.get(id));
        debugItems.put(id, item);
        DebugInfo.addDebugItem(item);
    }
}
