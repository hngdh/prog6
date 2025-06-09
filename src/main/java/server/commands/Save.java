package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.exceptions.LogException;
import common.packets.Request;
import server.iostream.Receiver;

public class Save extends Command {
    private Receiver receiver;

    public Save() {
        super("save","","save collection to processors",CommandTypes.NO_INPUT_NEEDED,CommandFormats.WITHOUT_ARG);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute(Request request) throws LogException {
        receiver.save();
    }
}
