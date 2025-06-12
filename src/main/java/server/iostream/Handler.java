package server.iostream;

import common.command_manager.CommandManager;
import common.data_processors.input.InputReader;
import common.data_processors.input.InputSplitter;
import common.exceptions.LogException;
import common.io.LogUtil;
import common.io.Printer;
import common.packets.Request;
import common.packets.Response;
import server.data.CollectionManager;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;

/**
 * The {@code Controller} class is the main entry point for processing user commands. It initializes
 * and manages various components, including data_processors reading, command classification, execution, and
 * collection management. It interacts with the user through the console, providing instructions and
 * feedback.
 */
public class Handler {
    private InputReader inputReader;
    private Invoker invoker;
    private Receiver receiver;

    public Handler() {
        Printer.printInfo("Server console. Usable {save, exit}");
    }

    public void prepare(String fileName) {
        try {
            inputReader = new InputReader();
            inputReader.setReader();

            CommandManager commandManager = new CommandManager();
            commandManager.init();
            CollectionManager collectionManager = new CollectionManager(fileName);

            receiver = new Receiver(collectionManager, commandManager);
            invoker = new Invoker(commandManager, receiver);

            collectionManager.loadData();
        } catch (LogException e) {
            Printer.printError(e.toString());
        }
    }

    public void processServer(Request request) throws LogException {
        try {
            String input = inputReader.readLine();
            input = input.trim();
            String command = InputSplitter.getCommand(input);
            switch (command) {
                case "save" -> invoker.call(null, new Request("save", null, null));
                case "exit" -> invoker.call(null, new Request("exit", null, null));
                default -> Printer.printError("Command not supported {save, exit}");
            }
        } catch (IOException e) {
            LogUtil.logServerError(e);
        }
    }

    public Response processClient(SocketAddress port, Request request) throws LogException {
        var result = invoker.call(port, request);
        Printer.printResult(result);
        return new Response(new ArrayList<>(), result);
    }
}
