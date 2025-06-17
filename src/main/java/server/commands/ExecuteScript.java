package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import java.util.List;
import server.ioStream.Receiver;

public class ExecuteScript extends Command {
  private Receiver receiver;

  public ExecuteScript() {
    super(
        "execute_script",
        "file_name",
        "read and execute script from dataProcessors. Commands in the script formatted the same as in interactive mode",
        CommandTypes.INPUT_NEEDED,
        CommandFormats.WITH_STRING_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public List<String> execute(Request request) {
    return null;
  }
}
