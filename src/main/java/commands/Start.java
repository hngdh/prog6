package commands;

import enums.CommandFormats;
import enums.CommandTypes;
import exceptions.LogException;
import iostream.Receiver;
import packets.Request;

public class Start extends Command {
  private Receiver receiver;

  public Start() {
    super(
        "start", "", "Start the program", CommandTypes.NO_INPUT_NEEDED, CommandFormats.WITHOUT_ARG);
  }

  @Override
  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute(Request request) throws LogException {
    receiver.start();
  }
}
