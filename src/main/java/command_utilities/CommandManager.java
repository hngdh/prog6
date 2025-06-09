package command_utilities;

import commands.*;
import java.util.HashMap;

/**
 * The {@code CommandManager} class manages the registration and retrieval of commands. It stores
 * commands in a HashMap for quick access, including properties' getters.
 */
public class CommandManager {
  private final HashMap<String, Command> commandCollection = new HashMap<>();

  public CommandManager() {}

  public void init() {
    registerCommand(new Add());
    registerCommand(new Clear());
    registerCommand(new ExecuteScript());
    registerCommand(new Exit());
    registerCommand(new FilterContainsName());
    registerCommand(new Help());
    registerCommand(new Info());
    registerCommand(new MinByCoordinates());
    registerCommand(new PrintFieldAscendingHouse());
    registerCommand(new RemoveByID());
    registerCommand(new RemoveFirst());
    registerCommand(new RemoveLower());
    registerCommand(new Save());
    registerCommand(new Show());
    registerCommand(new Sort());
    registerCommand(new Update());
    registerCommand(new Start());
  }

  public void registerCommand(Command command) {
    commandCollection.put(command.getName(), command);
  }

  public Command getCommand(String commandName) {
    return commandCollection.get(commandName);
  }

  public HashMap<String, Command> getCommandCollection() {
    return commandCollection;
  }

  public boolean isCommand(String commandName) {
    return commandCollection.containsKey(commandName);
  }
}
