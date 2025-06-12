package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.exceptions.LogException;
import common.packets.Request;
import server.iostream.Receiver;

import java.util.List;

public class Start extends Command {
    private Receiver receiver;

    public Start() {
        super("start", "", "Start the program", CommandTypes.NO_INPUT_NEEDED, CommandFormats.WITHOUT_ARG);
    }

    @Override
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public List<String> execute(Request request) throws LogException {
        receiver.start();
        return null;
    }
}
