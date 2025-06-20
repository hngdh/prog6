package commands;

import enums.CommandFormats;
import enums.CommandTypes;
import exceptions.LogException;
import iostream.Receiver;
import packets.Request;

public abstract class Command {
  private final String name;
  private final String argument;
  private final String description;
  private final CommandTypes classifier;
  private final CommandFormats format;

  public Command(
      String name,
      String argument,
      String description,
      CommandTypes classifier,
      CommandFormats format) {
    this.name = name;
    this.argument = argument;
    this.description = description;
    this.classifier = classifier;
    this.format = format;
  }

  public abstract void setReceiver(Receiver receiver);

  public abstract void execute(Request request) throws LogException;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getArgument() {
    return argument;
  }

  public String getCommandInfo() {
    return (name + " " + argument).trim() + ": " + description;
  }

  public CommandTypes getCommandClassifier() {
    return classifier;
  }

  public CommandFormats getCommandFormat() {
    return format;
  }
}
