package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import server.iostream.Receiver;

import java.util.List;

public class Exit extends Command {
    private Receiver receiver;

    public Exit() {
        super("exit", "", "exit the program (without saving)", CommandTypes.NO_INPUT_NEEDED, CommandFormats.WITHOUT_ARG);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public List<String> execute(Request request) {
        receiver.exit();
        return null;
    }
}
