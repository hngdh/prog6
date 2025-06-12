package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import server.iostream.Receiver;

import java.util.List;

public class Add extends Command {
    private Receiver receiver;

    public Add() {
        super("add", "{element}", "add element to collection", CommandTypes.INPUT_NEEDED, CommandFormats.WITHOUT_ARG);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public List<String> execute(Request request) {
        receiver.add(request);
        return null;
    }
}
