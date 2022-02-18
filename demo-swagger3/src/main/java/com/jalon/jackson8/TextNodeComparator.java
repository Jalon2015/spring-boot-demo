package com.jalon.jackson8;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Comparator;

/**
 * <p>
 *
 * </p>
 *
 * @author: syj
 * @date: 2022/2/18
 */
public class TextNodeComparator implements Comparator<JsonNode>
{
    @Override
    public int compare(JsonNode o1, JsonNode o2) {
        if (o1.equals(o2)) {
            return 0;
        }
        if ((o1 instanceof TextNode) && (o2 instanceof TextNode)) {
            String s1 = o1.asText();
            String s2 = o2.asText();
            if (s1.equalsIgnoreCase(s2)) {
                return 0;
            }
        }
        return 1;
    }
}
