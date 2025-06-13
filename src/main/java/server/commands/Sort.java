package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import server.iostream.Receiver;

import java.util.List;

public class Sort extends Command {
    private Receiver receiver;

    public Sort() {
        super(
                "sort",
                "",
                "sort collection in natural way",
                CommandTypes.NO_INPUT_NEEDED,
                CommandFormats.WITHOUT_ARG);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public List<String> execute(Request request) {

        return receiver.sort();
    }
}
