package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import java.util.List;
import server.iostream.Receiver;

public class Show extends Command {
  private Receiver receiver;

  public Show() {
    super(
        "show",
        "",
        "display all collection's elements line by line",
        CommandTypes.NO_INPUT_NEEDED,
        CommandFormats.WITHOUT_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public List<String> execute(Request request) {

    return receiver.show();
  }
}
