package server.ioStream;

import common.io.Printer;
import common.packets.Request;
import java.util.List;
import server.commandManager.CommandManager;
import server.commands.Command;

/**
 * The {@code Invoker} class is responsible for executing commands. It receives a command name and a
 * request, retrieves the corresponding command from the {@link CommandManager}, sets the {@link
 * Receiver} for the command, pushes the command to the history, and executes the command.
 */
public class Invoker {
  private final CommandManager commandManager;
  private final Receiver receiver;

  public Invoker(CommandManager commandManager, Receiver receiver) {
    this.commandManager = commandManager;
    this.receiver = receiver;
  }

  public List<String> call(Request request) {
    Command cmd = commandManager.getCommand(request.getCommand());
    cmd.setReceiver(receiver);
    Printer.printCondition("Executed");
    return cmd.execute(request);
  }
}
