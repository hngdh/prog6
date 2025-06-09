package commands;

import enums.CommandFormats;
import enums.CommandTypes;
import iostream.Receiver;
import packets.Request;

public class Add extends Command {
  private Receiver receiver;

  public Add() {
    super(
        "add",
        "{element}",
        "add element to collection",
        CommandTypes.INPUT_NEEDED,
        CommandFormats.WITHOUT_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute(Request request) {
    receiver.add(request);
  }
}
