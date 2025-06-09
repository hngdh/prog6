package server.iostream;

import common.command_manager.CommandManager;
import common.exceptions.LogException;
import common.io.Printer;
import common.packets.Request;
import server.commands.Command;

/**
 * The {@code Invoker} class is responsible for executing commands. It receives a command name and a
 * request, retrieves the corresponding command from the {@link CommandManager}, sets the {@link
 * Receiver} for the command, pushes the command to the history, and executes the command.
 */
public class Invoker {
    private final CommandManager commandManager;
    private final Receiver receiver;

    public Invoker(CommandManager commandManager, Receiver receiver) {
        this.commandManager = commandManager;
        this.receiver = receiver;
    }

    public void call(String command, Request request) throws LogException {
        Printer.printCondition("> Executing " + command);
        Command cmd = commandManager.getCommand(command);
        cmd.setReceiver(receiver);
        cmd.execute(request);
    }
}
