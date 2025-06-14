package server.iostream;

import common.data_processors.InputReader;
import common.exceptions.LogException;
import common.io.Printer;
import common.packets.Request;
import common.packets.Response;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import server.command_manager.CommandManager;
import server.data.CollectionManager;

/**
 * The {@code Controller} class is the main entry point for processing user commands. It initializes
 * and manages various components, including data_processors reading, command classification,
 * execution, and collection management. It interacts with the user through the console, providing
 * instructions and feedback.
 */
public class Handler {
  private InputReader inputReader;
  private Invoker invoker;
  private Receiver receiver;

  public Handler() {
    Printer.printInfo("Server console. Usable {save, exit, reload}");
  }

  public void prepare(String fileName) {
    try {
      inputReader = new InputReader();
      inputReader.setReader();

      CommandManager commandManager = new CommandManager();
      commandManager.init();
      CollectionManager collectionManager = new CollectionManager(fileName);

      receiver = new Receiver(collectionManager, commandManager);
      invoker = new Invoker(commandManager, receiver);

      collectionManager.loadData();
    } catch (LogException e) {
      Printer.printError(e.toString());
    }
  }

  public void processServer(Request request) throws LogException {
    String command = request.getCommand();
    switch (command) {
      case "save" -> invoker.call(null, new Request("save", null, null));
      case "exit" -> invoker.call(null, new Request("exit", null, null));
      case "reload" -> invoker.call(null, new Request("reload", null, null));
      default -> Printer.printError("Command not supported {save, exit, reload}");
    }
  }

  public Response processClient(SocketAddress port, Request request) {
    if (!request.getCommand().equals("save") && !request.getCommand().equals("reload")) {
      List<String> result = invoker.call(port, request);
      Printer.printResult(result);
      return new Response(new ArrayList<>(), result);
    } else {
      List<String> result = new ArrayList<>();
      result.add("Command not supported");
      return new Response(new ArrayList<>(), result);
    }
  }
}
