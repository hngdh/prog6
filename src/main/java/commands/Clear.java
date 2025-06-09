package commands;

import enums.CommandFormats;
import enums.CommandTypes;
import iostream.Receiver;
import packets.Request;

public class Clear extends Command {
  private Receiver receiver;

  public Clear() {
    super(
        "clear", "", "clear collection", CommandTypes.NO_INPUT_NEEDED, CommandFormats.WITHOUT_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute(Request request) {
    receiver.clear();
  }
}
