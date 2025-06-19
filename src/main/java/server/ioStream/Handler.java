package server.ioStream;

import common.dataProcessors.InputReader;
import common.dataProcessors.InputSplitter;
import common.exceptions.LogException;
import common.io.Printer;
import common.packets.Request;
import common.packets.Response;
import java.util.ArrayList;
import java.util.List;
import server.commandManager.CommandManager;
import server.data.CollectionManager;

/**
 * The {@code Controller} class is the main entry point for processing user commands. It initializes
 * and manages various components, including file reading, command classification,
 * execution, and collection management. It interacts with the user through the console, providing
 * instructions and feedback.
 */
public class Handler {
  private InputReader inputReader;
  private Invoker invoker;
  private Receiver receiver;
  private CommandManager commandManager;

  public Handler() {
    Printer.printInfo("Server console");
  }

  public void prepare(String fileName) {
    try {
      inputReader = new InputReader();
      inputReader.setReader();
      commandManager = new CommandManager();
      commandManager.init();
      CollectionManager collectionManager = new CollectionManager(fileName);

      receiver = new Receiver(collectionManager, commandManager);
      invoker = new Invoker(commandManager, receiver);

      Printer.printInfo("Usable " + commandManager.getServerCommandString());
      collectionManager.loadData();
    } catch (LogException e) {
      Printer.printError(e.toString());
    }
  }

  public void processServer(String input) {
    String command = InputSplitter.getCommand(input);
    String argument = InputSplitter.getArg(input);
    if (commandManager.isServerCommand(input)) {
      invoker.call(new Request(command, argument, null));
    } else {
      Printer.printError("Command not supported:");
      Printer.printCondition(commandManager.getServerCommandString());
    }
  }

  public Response processClient(Request request) {
    List<String> result;
    result = invoker.call(request);
    Printer.printResult(result);
    return new Response(new ArrayList<>(), result);
  }
}
