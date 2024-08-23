package com.arlojay.cosmicearth.lib.noise;

public class NoiseDebugString {
    private static int getIndentCount(String string) {
        int spaces = 0;

        for(int i = 0; i < string.length(); i++) {
            var character = string.charAt(i);

            if(character == ' ') spaces++;
            else return spaces / 4;
        }

        return spaces / 4;
    }

    private static String buildStringSubnodeSingle(NoiseNode node, boolean lastNode, Integer index) {
        var builder = new StringBuilder();

        var split = node.buildString().split("\n");
        var pipeText = (" " + (lastNode ? "\\" : "|") + "- ").toCharArray();

        var indexString = index == null ? "" : index.toString();
        for(int i = 0; i < indexString.length(); i++) {
            pipeText[i] = indexString.charAt(i);
        }

        for(int i = 0; i < split.length; i++) {
            var line = split[i].trim().replaceAll("\n", "");

            if(i == 0) {
                builder.append("\n").append(pipeText).append(line);
                continue;
            }
            if(line.length() == 0) continue;

            var indents = getIndentCount(split[i]);

            if(lastNode) {
                builder.append("\n ").append("    ".repeat(indents)).append(line);
            } else {
                builder.append("\n ").append("    ".repeat(indents)).append(" |  ").append(line);
            }
        }
        return builder.toString();
    }
    public static String buildStringSubnode(NoiseNode ...nodes) {
        var builder = new StringBuilder();
        boolean first = true;
        boolean last = false;
        boolean multiple = nodes.length > 1;
        for(int i = 0; i < nodes.length; i++) {
            var node = nodes[i];
            if(!first) builder.append('\n');
            if(i == nodes.length - 1) last = true;
            builder.append(buildStringSubnodeSingle(node, last, multiple ? i : null));
            first = false;
        }
        return builder.toString();
    }
    public static String createPropertyList(Object ...objects) {
        var builder = new StringBuilder();

        boolean readingKey = true;
        boolean first = true;

        for (Object object : objects) {
            if (readingKey) {
                if (!first) builder.append(", ");
                builder.append(object).append(" = ");
            } else {
                builder.append(object);
            }

            readingKey = !readingKey;
            first = false;
        }

        return "[" + builder + "]";
    }
}
