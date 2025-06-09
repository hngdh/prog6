package commands;

import enums.CommandFormats;
import enums.CommandTypes;
import iostream.Receiver;
import packets.Request;

public class RemoveLower extends Command {
  private Receiver receiver;

  public RemoveLower() {
    super(
        "remove_lower",
        "{element}",
        "remove elements lower than given element",
        CommandTypes.INPUT_NEEDED,
        CommandFormats.WITHOUT_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute(Request request) {
    receiver.remove_lower(request);
  }
}
