package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import server.iostream.Receiver;

public class MinByCoordinates extends Command {
    private Receiver receiver;

    public MinByCoordinates() {
        super("min_by_coordinates","","display object from collection with minimum coordinate",CommandTypes.NO_INPUT_NEEDED,CommandFormats.WITHOUT_ARG);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute(Request request) {
        receiver.min_by_coordinates();
    }
}
