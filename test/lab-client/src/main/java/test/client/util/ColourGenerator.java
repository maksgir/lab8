package test.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class ColourGenerator {
    private static List<String> colours = new ArrayList<>();

    static {
        colours.add("#3300CC");
        colours.add("#33FF00");
        colours.add("#660000");
        colours.add("#FF0000");
        colours.add("#FFCC00");
        colours.add("#660066");
        colours.add("#00FFFF");
        colours.add("#008080");
        colours.add("#FF00FF");
        colours.add("#00FF00");

    }

    private static TreeMap<String, String> map = new TreeMap<>();


    public static String getColour(String login) {
        Set<String> logins = map.keySet();
        if (logins.contains(login)) {
            return map.get(login);
        } else {
            String colour = getNewColour();
            map.put(login, colour);
            return colour;
        }
    }

    private static String getNewColour() {
        for (String color : colours) {
            if (!map.containsValue(color)) {
                return color;
            }
        }
        return "#000000";

    }

}
