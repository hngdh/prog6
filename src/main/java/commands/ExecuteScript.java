package commands;

import enums.CommandFormats;
import enums.CommandTypes;
import iostream.Receiver;
import packets.Request;

public class ExecuteScript extends Command {
  private Receiver receiver;

  public ExecuteScript() {
    super(
        "execute_script",
        "file_name",
        "read and execute script from file. Commands in the script formatted the same as in interactive mode",
        CommandTypes.INPUT_NEEDED,
        CommandFormats.WITH_STRING_ARG);
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute(Request request) {
    receiver.execute_script();
  }
}
