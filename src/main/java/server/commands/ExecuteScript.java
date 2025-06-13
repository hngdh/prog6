package server.commands;

import common.enums.CommandFormats;
import common.enums.CommandTypes;
import common.packets.Request;
import server.iostream.Receiver;

import java.util.List;

public class ExecuteScript extends Command {
    private Receiver receiver;

    public ExecuteScript() {
        super(
                "execute_script",
                "file_name",
                "read and execute script from data_processors. Commands in the script formatted the same as in interactive mode",
                CommandTypes.INPUT_NEEDED,
                CommandFormats.WITH_STRING_ARG);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public List<String> execute(Request request) {
        return null;
    }
}
