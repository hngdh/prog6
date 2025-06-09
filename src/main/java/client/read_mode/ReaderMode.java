package client.read_mode;

import common.exceptions.LogException;
import server.iostream.Invoker;

import java.io.IOException;

/**
 * The {@code ReadMode} interface defines the contract for different reading modes used by the
 * application. Implementing classes are responsible for reading processors, processing it, and executing
 * commands.
 */
public interface ReaderMode {
    void executeMode(Invoker invoker, String commandName, String arg)
            throws LogException, IOException;
}
