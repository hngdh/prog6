package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import server.iostream.Receiver;

import java.util.List;

public class RemoveByID extends Command {
    private Receiver receiver;

    public RemoveByID() {
        super(
                "remove_by_id",
                "id",
                "remove element from collection by its id",
                CommandTypes.NO_INPUT_NEEDED,
                CommandFormats.WITH_NUMERAL_ARG);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public List<String> execute(Request request) {

        return receiver.remove_by_id(request);
    }
}
