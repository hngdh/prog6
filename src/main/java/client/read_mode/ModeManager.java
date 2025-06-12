package client.read_mode;

import client.iostream.Renderer;
import client.network.ClientNetwork;
import common.exceptions.LogException;

import java.io.IOException;
import java.util.HashMap;

/**
 * The {@code ModeManager} class manages different reading modes for commands, such as reading from
 * the console or reading from data_processors. It allows associating specific commands with specific {@link
 * ReaderMode} implementations.
 */
public class ModeManager {
    private final HashMap<String, ReaderMode> readModes = new HashMap<>();

    public void init() {
        registerReadMode("execute_script", new FileReaderMode());
        registerReadMode("remove_lower", new ConsoleReaderMode());
        registerReadMode("update", new ConsoleReaderMode());
        registerReadMode("add", new ConsoleReaderMode());
    }

    public void registerReadMode(String command, ReaderMode readMode) {
        readModes.put(command, readMode);
    }

    public void call(Renderer renderer, ClientNetwork clientNetwork, String commandName, String arg) throws LogException, IOException {
        readModes.get(commandName).execute(renderer, clientNetwork, commandName, arg);
    }
}
