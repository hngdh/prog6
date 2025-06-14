package server.command_manager;

import java.util.TreeMap;
import server.commands.*;

/**
 * The {@code CommandManager} class manages the registration and retrieval of commands. It stores
 * commands in a HashMap for quick access, including properties' getters.
 */
public class CommandManager {
  private final TreeMap<String, Command> commandCollection = new TreeMap<>();

  public CommandManager() {}

  public void init() {
    registerCommand(new Add());
    registerCommand(new Clear());
    registerCommand(new Exit());
    registerCommand(new ExecuteScript());
    registerCommand(new FilterContainsName());
    registerCommand(new Help());
    registerCommand(new Info());
    registerCommand(new MinByCoordinates());
    registerCommand(new Ping());
    registerCommand(new PrintFieldAscendingHouse());
    registerCommand(new Reload());
    registerCommand(new RemoveByID());
    registerCommand(new RemoveFirst());
    registerCommand(new RemoveLower());
    registerCommand(new Save());
    registerCommand(new Show());
    registerCommand(new Sort());
    registerCommand(new Update());
  }

  public void registerCommand(Command command) {
    commandCollection.put(command.getName(), command);
  }

  public Command getCommand(String commandName) {
    return commandCollection.get(commandName);
  }

  public TreeMap<String, Command> getCommandCollection() {
    return commandCollection;
  }

  public boolean isCommand(String commandName) {
    return commandCollection.containsKey(commandName);
  }
}
