package com.arlojay.cosmicearth.lib.performance;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Performance {
    private static final Map<String, Long> times = new HashMap<>();
    private static final Map<String, Long> startTimes = new HashMap<>();
    private static final Map<String, Integer> count = new HashMap<>();

    public static void start(String id) {
        startTimes.put(id, System.nanoTime());
    }
    public static void end(String id) {
        times.put(id, times.getOrDefault(id, 0L) + (System.nanoTime() - startTimes.remove(id)));
        count.put(id, count.getOrDefault(id, 0) + 1);
    }

    public static void report() {
        long _total = 0L;

        for(var id : times.keySet()) {
            _total += times.get(id);
        }

        final long total = _total;

        System.out.printf("==== %s report(s), taking %sns total ====\n", times.size(), total);
        var sorted = times.keySet().stream().sorted(Comparator.comparingDouble(a -> (double) times.get(a) / (double) total)).toList();

        for(var id : sorted) {
            long value = times.get(id);
            int counts = count.get(id);

            double percentage = (double) value / (double) total * 100d;
            double avgTime = (double) value / (double) counts;

            System.out.println(
                    StringUtils.rightPad(id, 48) +
                    " | " +
                    StringUtils.rightPad((value / 1000000d) + "ms", 20) +
                    " | " +
                    StringUtils.rightPad(percentage + "%", 24) +
                    " | " +
                    StringUtils.rightPad((((long) avgTime) / 1000000d) + "ms/op", 16)
            );
        }
    }
}
