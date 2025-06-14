package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import java.util.List;
import server.iostream.Receiver;

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
  public List<String> execute(Request request) {
    receiver.clear();
    return null;
  }
}
