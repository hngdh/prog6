package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import server.iostream.Receiver;

public class PrintFieldAscendingHouse extends Command {
    private Receiver receiver;

    public PrintFieldAscendingHouse() {
        super("print_field_ascending_house","","display in ascending order all elements in house's field",CommandTypes.NO_INPUT_NEEDED,CommandFormats.WITHOUT_ARG);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute(Request request) {
        receiver.print_field_ascending_house();
    }
}
