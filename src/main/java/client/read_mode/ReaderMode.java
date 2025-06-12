package client.read_mode;

import client.iostream.Renderer;
import client.network.ClientNetwork;
import common.exceptions.LogException;

import java.io.IOException;

/**
 * The {@code ReadMode} interface defines the contract for different reading modes used by the
 * application. Implementing classes are responsible for reading data_processors, processing it, and executing
 * commands.
 */
public interface ReaderMode {
    void execute(Renderer renderer, ClientNetwork clientNetwork, String commandName, String arg)
            throws LogException, IOException;
}
