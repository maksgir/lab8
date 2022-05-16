package test.client.util;

import java.util.HashSet;
import java.util.Set;

public class ScriptsHistory {
    private static final Set<String> HISTORY_OF_SCRIPTS = new HashSet<>();
    public static Set<String> getHistoryOfScripts() {
        return HISTORY_OF_SCRIPTS;
    }
    public static void addToScriptHistory(String fileName){
        HISTORY_OF_SCRIPTS.add(fileName);
    }
}
