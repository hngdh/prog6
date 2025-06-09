package io.input;

import command_utilities.CommandManager;
import enums.CommandFormats;
import exceptions.WrongInputFormatException;

/**
 * The {@code FormatChecker} class is responsible for validating the format of user commands. It
 * checks whether a given command has the correct type of argument (or no argument) as defined in
 * command's properties.
 */
public class FormatChecker {
  private final CommandManager commandManager;

  public FormatChecker() {
    this.commandManager = new CommandManager();
    commandManager.init();
  }

  public void checkFormat(String command, String argument) throws WrongInputFormatException {
    if (!commandManager.isCommand(command)) throw new WrongInputFormatException();
    CommandFormats commandFormats = commandManager.getCommand(command).getCommandFormat();
    switch (commandFormats) {
      case WITH_NUMERAL_ARG:
        try {
          int integer = Integer.parseInt(argument);
          if (integer < 0) {
            throw new WrongInputFormatException();
          }
        } catch (NumberFormatException e) {
          throw new WrongInputFormatException();
        }
        break;
      case WITH_STRING_ARG:
        if (argument == null || argument.isEmpty()) throw new WrongInputFormatException();
        break;
      case WITHOUT_ARG:
        if (argument != null && !argument.isEmpty()) throw new WrongInputFormatException();
        break;
    }
  }
}
