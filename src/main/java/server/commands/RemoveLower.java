package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import server.iostream.Receiver;

import java.util.List;

public class RemoveLower extends Command {
    private Receiver receiver;

    public RemoveLower() {
        super("remove_lower", "{element}", "remove elements lower than given element", CommandTypes.INPUT_NEEDED, CommandFormats.WITHOUT_ARG);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public List<String> execute(Request request) {

        return receiver.remove_lower(request);
    }
}
