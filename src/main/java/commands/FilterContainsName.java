package commands;

import enums.CommandFormats;
import enums.CommandTypes;
import iostream.Receiver;
import packets.Request;

public class FilterContainsName extends Command {
  private Receiver receiver;

  public FilterContainsName() {
    super(
        "filter_contains_name",
        "name",
        "display elements with given name",
        CommandTypes.NO_INPUT_NEEDED,
        CommandFormats.WITH_STRING_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute(Request request) {
    receiver.filter_contains_name(request);
  }
}
