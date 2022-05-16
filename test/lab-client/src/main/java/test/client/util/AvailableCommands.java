package test.client.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class AvailableCommands {

    public static final Set<String> COMMANDS_WITHOUT_ARGS = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_ID_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_DISTANCE_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_ROUTE_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_ROUTE_ID_ARGS = new HashSet<>();
    public static final String SCRIPT_ARGUMENT_COMMAND;

    static {
        Collections.addAll(COMMANDS_WITHOUT_ARGS,
                "help",
                "info",
                "show",
                "clear",
                "save",
                "remove_head"

        );
        Collections.addAll(COMMANDS_WITH_ID_ARG,
                "remove_by_id"
        );
        Collections.addAll(COMMANDS_WITH_DISTANCE_ARG,
                "remove_any_by_distance",
                "filter_by_distance",
                "filter_greater_than_distance"
        );
        Collections.addAll(COMMANDS_WITH_ROUTE_ARG,
                "add",
                "add_if_min",
                "remove_lower"
        );
        Collections.addAll(COMMANDS_WITH_ROUTE_ID_ARGS,
                "update");
        SCRIPT_ARGUMENT_COMMAND = "execute_script";
    }

    private AvailableCommands() {
    }

}