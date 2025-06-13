package server.iostream;

import common.exceptions.LogException;
import common.io.Printer;
import common.packets.Request;
import server.command_manager.CommandManager;
import server.commands.Command;

import java.net.SocketAddress;
import java.util.List;

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

    public List<String> call(SocketAddress port, Request request) {
        Printer.printCondition("> Executing " + request.getCommand());
        Command cmd = commandManager.getCommand(request.getCommand());
        cmd.setReceiver(receiver);
        Printer.printCondition("Executed");
        return cmd.execute(request);
    }
}
