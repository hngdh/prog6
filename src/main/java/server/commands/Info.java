package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import java.util.List;
import server.ioStream.Receiver;

public class Info extends Command {
  private Receiver receiver;

  public Info() {
    super(
        "info",
        "",
        "display information about collection",
        CommandTypes.NO_INPUT_NEEDED,
        CommandFormats.WITHOUT_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public List<String> execute(Request request) {
    return receiver.info();
  }
}
