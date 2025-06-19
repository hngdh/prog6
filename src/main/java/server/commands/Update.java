package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import java.util.List;
import server.ioStream.Receiver;

public class Update extends Command {
  private Receiver receiver;

  public Update() {
    super(
        "update",
        "id {element}",
        "update value of element with given id",
        CommandTypes.INPUT_NEEDED,
        CommandFormats.WITH_NUMERAL_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public List<String> execute(Request request) {
    return receiver.update(request);
  }
}
