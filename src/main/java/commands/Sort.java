package commands;

import enums.CommandFormats;
import enums.CommandTypes;
import iostream.Receiver;
import packets.Request;

public class Sort extends Command {
  private Receiver receiver;

  public Sort() {
    super(
        "sort",
        "",
        "sort collection in natural way",
        CommandTypes.NO_INPUT_NEEDED,
        CommandFormats.WITHOUT_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute(Request request) {
    receiver.sort();
  }
}
