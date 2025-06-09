package read_mode;

import exceptions.LogException;
import iostream.Invoker;
import java.io.IOException;

/**
 * The {@code ReadMode} interface defines the contract for different reading modes used by the
 * application. Implementing classes are responsible for reading input, processing it, and executing
 * commands.
 */
public interface ReaderMode {
  void executeMode(Invoker invoker, String commandName, String arg)
      throws LogException, IOException;
}
